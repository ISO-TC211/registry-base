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
package de.geoinfoffm.registry.api;

import java.util.Comparator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;

import javax.persistence.Transient;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * The class RegisterName.
 *
 * @author Florian Esser
 */
public class RegisterName
{
	private UUID uuid;
	private String name;
	private String alias;
	private RegisterName parentRegister;
	private SortedMap<RegisterName, UUID> subregisters;
	
	public RegisterName(UUID uuid, String name, String alias) {
		this.uuid = uuid;
		this.name = name;
		this.alias = alias;
	}
	
	public UUID getUuid() {
		return uuid;
	}
	
	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public String getName() {
		return name;
	}

	public String getAlias() {
		return alias;
	}
	
	public RegisterName getParentRegister() {
		return parentRegister;
	}

	public void setParentRegister(RegisterName parentRegister) {
		this.parentRegister = parentRegister;
	}

	public Map<RegisterName, UUID> getSubregisters() {
		if (this.subregisters == null) {
			this.subregisters = new TreeMap<>(new Comparator<RegisterName>() {
				@Override
				public int compare(RegisterName o1, RegisterName o2) {
					if (o1 == null && o2 == null) {
						return 0;
					}
					else if (o1 == null) {
						return -1;
					}
					else if (o2 == null) {
						return 1;
					}
					
					return o1.getName().compareTo(o2.getName());
				}
			});
		}
		
		return this.subregisters;
	}
	
	public boolean isTopLevelRegister() {
		return this.parentRegister == null;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}

		if (o == this) {
			return true;
		}

		if (!o.getClass().equals(this.getClass())) {
			return false;
		}

		RegisterName other = (RegisterName)o;

		if (this.getName() == null || other.getName() == null) {
			// Entities without a name cannot be compared
			return false;
		}

		return other.getUuid().toString().equals(this.getUuid().toString());
	}
	
	@Transient
	@Override
	public int hashCode() {
		if (name != null) {
			return name.hashCode();
		}
		else {
			return HashCodeBuilder.reflectionHashCode(this, false);
		}
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
	
}
