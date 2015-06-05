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
package de.geoinfoffm.registry.client;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import de.geoinfoffm.registry.api.AbstractEventListener;
import de.geoinfoffm.registry.api.security.PasswordResetRequest;
import de.geoinfoffm.registry.api.security.PasswordResetRequestedEvent;
import de.geoinfoffm.registry.client.web.ClientConfiguration;
import de.geoinfoffm.registry.core.model.RegistryUser;

/**
 *
 * @author Florian Esser
 */
@Component
public class PasswordResetRequestedEventListener extends AbstractEventListener implements ApplicationListener<PasswordResetRequestedEvent>
{
	private Logger logger = LoggerFactory.getLogger(PasswordResetRequestedEventListener.class);

	public PasswordResetRequestedEventListener() {
	}

	@Override
	public void onApplicationEvent(PasswordResetRequestedEvent event) {
		if (event.isAnnotated(this.getClass())) return;

		logger.debug("Received event: Password reset request - {}", event.getSource().toString());

		final PasswordResetRequest reset = event.getSource();
		final RegistryUser user = reset.getUser();

		if (!user.isConfirmed() || !user.isActive()) return;

		final String mailBaseUrl = ClientConfiguration.getMailBaseUrl(); 				
		final String passwordResetUrl = mailBaseUrl + "reset-password?token=" + reset.getToken() + "&mail=" + user.getEmailAddress();

		Map<String, Object> model = new HashMap<String, Object>();
        model.put("passwordResetUrl", passwordResetUrl);

        try {
         	this.sendMailToUser(user, "mail.subject.password.reset", "mailtemplates/password_reset", 
        			ClientConfiguration.getMailBaseUrl(), model);
        }
    	catch (Throwable t) {
    		logger.error("Sending mail failed: " + t.getMessage(), t);
    	}
		
		event.annotate(this.getClass());
	}

}
