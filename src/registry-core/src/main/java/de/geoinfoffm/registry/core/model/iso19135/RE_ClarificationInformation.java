/**
 * Copyright (c) 2014, German Federal Agency for Cartography and Geodesy
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *     * Redistributions of source code must retain the above copyright
 *     	 notice, this list of conditions and the following disclaimer.

 *     * Redistributions in binary form must reproduce the above
 *     	 copyright notice, this list of conditions and the following
 *       disclaimer in the documentation and/or other materials
 *       provided with the distribution.

 *     * The names "German Federal Agency for Cartography and Geodesy",
 *       "Bundesamt für Kartographie und Geodäsie", "BKG", "GDI-DE",
 *       "GDI-DE Registry" and the names of other contributors must not
 *       be used to endorse or promote products derived from this
 *       software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE GERMAN
 * FEDERAL AGENCY FOR CARTOGRAPHY AND GEODESY BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING
 * IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */
package de.geoinfoffm.registry.core.model.iso19135;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Embedded;
import javax.persistence.Entity;

import org.hibernate.envers.Audited;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.bespire.LoggerFactory;
import de.geoinfoffm.registry.core.model.iso19103.CharacterString;

@Access(AccessType.FIELD)
@DiscriminatorValue("RE_ClarificationInformation")
@Audited @Entity
public class RE_ClarificationInformation extends RE_ProposalManagementInformation {
	
	private static Logger logger = LoggerFactory.make();

	@Embedded
	@AttributeOverride(name = "value", column = @Column(name = "proposedChange", length = 2000))
	private CharacterString proposedChange;

	public RE_ClarificationInformation() {
		new Object();
	}

	/**
	 * @return the proposedChange
	 */
	public CharacterString getProposedChange() {
		return proposedChange;
	}

	/**
	 * @param proposedChange the proposedChange to set
	 */
	public void setProposedChange(CharacterString proposedChange) {
		this.proposedChange = proposedChange;
	}

	/**
	 * @param proposedChanges
	 * @return
	 */
	public static String toJson(Map<String, List<String>> proposedChanges) {
		StringWriter proposedChangesWriter = new StringWriter();
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.writeValue(proposedChangesWriter, proposedChanges);
		}
		catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage(), e);
		}
		String proposedChangesJson = proposedChangesWriter.toString();
		return proposedChangesJson;
	}
	
	public static Map<String, List<String>> fromJson(String json) {
		ObjectMapper jsonMapper = new ObjectMapper();
		try {
			Map<String, List<String>> proposedChangesMap = jsonMapper.readValue(json, Map.class);
			return proposedChangesMap;
		}
		catch (IOException e) {
			logger.error(">>>> The following JSON string could not be decoded:");
			logger.error(json);
			logger.error(">>>> cause:");
			logger.error(e.getMessage(), e);
			
			return new HashMap<String, List<String>>();
		}
	}

	public void finalize() throws Throwable {
		super.finalize();
	}

	/* (non-Javadoc)
	 * @see de.geoinfoffm.registry.core.model.iso19135.RE_ProposalManagementInformation#onDispositionAccepted()
	 */
	@Override
	protected void onDispositionAccepted() {
		applyProposedChanges();
	}

	public void applyProposedChanges() {
		String proposedChangesJson = CharacterString.asString(this.getProposedChange());
		ObjectMapper jsonMapper = new ObjectMapper();
		try {
			RE_RegisterItem item = this.getItem();
			
			Map<String, Object> proposedChanges = jsonMapper.readValue(proposedChangesJson, Map.class);
			for (String property : proposedChanges.keySet()) {
				PropertyDescriptor pd = BeanUtils.getPropertyDescriptor(item.getClass(), property);
				if (pd == null) {
					throw new RuntimeException(String.format("Proposed change contains unknown property %s", property));
				}
				
				Object newValue;
				if (pd.getPropertyType().equals(CharacterString.class)) {
					newValue = new CharacterString((String)getSingleValue(proposedChanges, property));
					pd.getWriteMethod().invoke(item, newValue);
				}
				else if (pd.getPropertyType().equals(String.class)) {
					newValue = getSingleValue(proposedChanges, property);
					pd.getWriteMethod().invoke(item, newValue);
				}
				else if (Collection.class.isAssignableFrom(pd.getPropertyType())) {
					Collection<CharSequence> oldValue = (Collection<CharSequence>)pd.getReadMethod().invoke(item);
					oldValue.clear();
					Collection<CharSequence> newValues = (Collection<CharSequence>)proposedChanges.get(property); 
					for (CharSequence value : newValues) {
						oldValue.add(value);
					}
				}
				else {
					throw new RuntimeException(String.format("Property type %s not supported.", pd.getPropertyType().getCanonicalName()));
				}
			}
		}
		catch (IOException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	private Object getSingleValue(Map<String, Object> proposedChanges, String property) {
		Object newValue;
		newValue = proposedChanges.get(property);
		if (newValue instanceof Collection) {
			Collection collection = (Collection)newValue;
			if (collection.size() > 1) {
				throw new RuntimeException(String.format("Multiple values provided for single-valued property %s", property));
			}
			else if (collection.isEmpty()) {
				newValue = null;
			}
			else {
				newValue = collection.iterator().next();
			}
		}
		return newValue;
	}
}//end RE_ClarificationInformation