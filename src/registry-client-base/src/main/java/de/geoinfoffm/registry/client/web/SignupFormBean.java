/**
 * Copyright (c) 2014, German Federal Agency for Cartography and Geodesy
 * All rights reserved.
 * <p>
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 * <p>
 * * Redistributions in binary form must reproduce the above
 * copyright notice, this list of conditions and the following
 * disclaimer in the documentation and/or other materials
 * provided with the distribution.
 * <p>
 * * The names "German Federal Agency for Cartography and Geodesy",
 * "Bundesamt für Kartographie und Geodäsie", "BKG", "GDI-DE",
 * "GDI-DE Registry" and the names of other contributors must not
 * be used to endorse or promote products derived from this
 * software without specific prior written permission.
 * <p>
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
package de.geoinfoffm.registry.client.web;

import de.geoinfoffm.registry.api.soap.CreateRegistryUserRequest;
import de.geoinfoffm.registry.client.web.recaptcha.ValidReCaptcha;
import de.geoinfoffm.registry.validators.FieldMatch;
import de.geoinfoffm.registry.validators.UniqueUserEmail;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;


@FieldMatch(first = "password", second = "confirmPassword", message = "The password fields must match")
public class SignupFormBean {

    @NotBlank
    @Length(min = 2, message = "Name too short - minimum length is 2 characters")
    private String name;

    @Email
    @NotBlank
    @UniqueUserEmail(message = "form.validation.unavailable.user.emailAddress")
    private String emailAddress;

    @NotEmpty
    private String password;

    @NotEmpty
    private String confirmedPassword;

    private String preferredLanguage;
    private String organizationUuid;
    private boolean organizationNotListed;

    @ValidReCaptcha
    private String reCaptchaResponse;

    public SignupFormBean() {
        // TODO Auto-generated constructor stub
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmedPassword() {
        return confirmedPassword;
    }

    public void setConfirmedPassword(String confirmedPassword) {
        this.confirmedPassword = confirmedPassword;
    }

    public String getPreferredLanguage() {
        return preferredLanguage;
    }

    public void setPreferredLanguage(String preferredLanguage) {
        this.preferredLanguage = preferredLanguage;
    }

    public String getOrganizationUuid() {
        return organizationUuid;
    }

    public void setOrganizationUuid(String organizationUuid) {
        this.organizationUuid = organizationUuid;
    }

    public boolean isOrganizationNotListed() {
        return organizationNotListed;
    }

    public void setOrganizationNotListed(boolean organizationNotListed) {
        this.organizationNotListed = organizationNotListed;
    }

//	public CreateGdiRegistryUserRequest toRegistrationDTO() {
//		CreateGdiRegistryUserRequest result = new CreateGdiRegistryUserRequest();
//		result.setName(this.name);
//		result.setEmailAddress(this.emailAddress);
//		result.setPassword(this.password);
//		result.setPreferredLanguage(this.preferredLanguage);
//		result.setOrganizationUuid(this.organizationUuid);
//		result.setActive(true);
//		
//		return result;
//	}

    public CreateRegistryUserRequest toRegistrationDTO() {
        CreateRegistryUserRequest result = new CreateRegistryUserRequest();
        result.setName(this.name);
        result.setEmailAddress(this.emailAddress);
        result.setPassword(this.password);
        result.setOrganizationUuid(this.organizationUuid);
        result.setPreferredLanguage(this.preferredLanguage);
        result.setActive(true);

        return result;
    }

    public String getReCaptchaResponse() {
        return reCaptchaResponse;
    }

    public void setReCaptchaResponse(String reCaptchaResponse) {
        this.reCaptchaResponse = reCaptchaResponse;
    }
}
