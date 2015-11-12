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
package de.geoinfoffm.registry.core.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import de.geoinfoffm.registry.core.CharacterStringAdapter;
import de.geoinfoffm.registry.core.PasswordEncodingService;
import de.geoinfoffm.registry.core.PasswordEncodingServiceImpl;

/**
 * Persistence class for {@link RegistryUser}.<br>
 * <br>
 * Contains JPA annotations for object-relational mapping and JAXB annotations for
 * XML serialization.
 * 
 * @author Florian Esser
 *
 */
@Access(AccessType.FIELD)
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Audited @Entity 
public class RegistryUser extends Actor implements UserDetails
{
	private static final long serialVersionUID = -8753622285231353417L;

	@Transient
	private PasswordEncodingService passwordEncoder = new PasswordEncodingServiceImpl();
	
	@Size(min = 2, max = 255)
	@XmlJavaTypeAdapter(CharacterStringAdapter.class)
	@Column(columnDefinition = "text")
	private String name;

	@ManyToOne
	@JoinColumn(name = "organization_uuid")
	private Organization organization;

	@NotEmpty
	@Email
	@XmlJavaTypeAdapter(CharacterStringAdapter.class)
	@Column(columnDefinition = "text")
	private String emailAddress;

	@NotEmpty
	@XmlJavaTypeAdapter(CharacterStringAdapter.class)
	private String passwordHash;

	@NotEmpty
	@XmlJavaTypeAdapter(CharacterStringAdapter.class)
	private String preferredLanguage;

//	@OneToMany(mappedBy = "actor", cascade = CascadeType.ALL)
//	private List<Authorization> authorizations;

	@NotNull
	private Boolean isActive;
	
	@Transient
	private boolean isMembershipApproved;

	@Type(type = "pg-uuid")	
	private UUID confirmationToken;
	
	protected RegistryUser() {
		
	}
	
	public RegistryUser(String name, String emailAddress, String password, String preferredLanguage, Organization organization,
			boolean isActive) {

		super();
		this.name = name;
		this.emailAddress = emailAddress;
		this.setPassword(password);
		this.preferredLanguage = preferredLanguage;
		this.organization = organization;
		this.isActive = isActive;
	}

	public RegistryUser(UUID id, String name, String emailAddress, String passwordHash, String preferredLanguage, Organization organization,
			boolean isActive) {

		super(id);
		this.name = name;
		this.emailAddress = emailAddress;
		this.passwordHash = passwordHash;
		this.preferredLanguage = preferredLanguage;
		this.organization = organization;
		this.isActive = isActive;
	}

	/**
	 * @return the name
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	/**
	 * @return the emailAddress
	 */
	public String getEmailAddress() {
		return emailAddress;
	}

	/**
	 * @param emailAddress the emailAddress to set
	 */
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	/**
	 * @return the passwordHash
	 */
	public String getPasswordHash() {
		return passwordHash;
	}

	/**
	 * @param passwordHash the passwordHash to set
	 */
	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}
	
	public void setPassword(String password) {
		this.passwordHash = new String(passwordEncoder.encode(password));
	}

	/**
	 * @return the preferredLanguage
	 */
	public String getPreferredLanguage() {
		return preferredLanguage;
	}

	/**
	 * @param preferredLanguage the preferredLanguage to set
	 */
	public void setPreferredLanguage(String preferredLanguage) {
		this.preferredLanguage = preferredLanguage;
	}

//	/**
//	 * @return the organization
//	 */
//	public Organization getOrganization() {
//		return organization;
//	}
//
//	/**
//	 * @param organization the organization to set
//	 */
//	public void setOrganization(Organization organization) {
//		this.organization = organization;
//	}

	/**
	 * @return the isActive
	 */
	public boolean isActive() {
		return this.isActive;
	}

	/**
	 * @param isRegistryPointOfContact the isRegistryPointOfContact to set
	 */
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> result = new ArrayList<GrantedAuthority>();

		for (Role role : this.getRoles()) {
			result.add(new SimpleGrantedAuthority(role.getName()));
		}
		
		return result;
	}

	@Override
	public String getPassword() {
		return this.passwordHash;
	}

	@Override
	public String getUsername() {
		return this.getUuid().toString();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return this.isActive() && this.isMembershipApproved();
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public String toString() {
		return String.format("{%s} %s (%s)", this.getClass().getCanonicalName(), this.getName(), this.getEmailAddress());
	}

	public UUID getConfirmationToken() {
		return confirmationToken;
	}

	public void setConfirmationToken(UUID confirmationToken) {
		this.confirmationToken = confirmationToken;
	}

	public boolean isConfirmed() {
		return this.confirmationToken == null;
	}
	
	public boolean isMembershipApproved() {
		return this.isMembershipApproved;
	}
	
	public void setMembershipApproved(boolean isApproved) {
		this.isMembershipApproved = isApproved;
	}
	
}
