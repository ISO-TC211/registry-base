package de.geoinfoffm.registry.core.model.iso19135;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Map;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Embedded;
import javax.persistence.Entity;

import org.codehaus.jackson.map.ObjectMapper;
import org.hibernate.envers.Audited;
import org.springframework.beans.BeanUtils;

import de.geoinfoffm.registry.core.model.iso19103.CharacterString;

@Access(AccessType.FIELD)
@DiscriminatorValue("RE_ClarificationInformation")
@Audited @Entity
public class RE_ClarificationInformation extends RE_ProposalManagementInformation {

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
	public static String toJson(Map<String, String[]> proposedChanges) {
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
	
	public static Map<String, String[]> fromJson(String json) {
		ObjectMapper jsonMapper = new ObjectMapper();
		try {
			Map<String, String[]> proposedChangesMap = jsonMapper.readValue(json, Map.class);
			return proposedChangesMap;
		}
		catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage(), e);
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
					newValue = new CharacterString((String)proposedChanges.get(property));
					pd.getWriteMethod().invoke(item, newValue);
				}
				else if (pd.getPropertyType().equals(String.class)) {
					newValue = proposedChanges.get(property);
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
}//end RE_ClarificationInformation