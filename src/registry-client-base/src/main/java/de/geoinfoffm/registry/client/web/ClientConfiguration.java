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
package de.geoinfoffm.registry.client.web;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import de.geoinfoffm.registry.core.CoreConfiguration;

public class ClientConfiguration 
{
	private static final String CONFIG_FILE = "client.configuration.xml";
	
	private static Properties cachedConfiguration;
	
	public Properties configuration() {
		Properties properties = CoreConfiguration.loadPropertiesFromFile(CONFIG_FILE);
		
		if (properties == null) {
			properties = createDefaultProperties();
			saveProperties(properties, CONFIG_FILE);
		}

		return properties;
	}

	/**
	 * @return a default set of properties.
	 */
	private Properties createDefaultProperties() {
		Properties properties = new Properties();
		properties.setProperty("basePath", "");
		
		return properties;
	}
	
	/**
	 * @param properties
	 * @param propertiesFile
	 */
	private void saveProperties(Properties properties, String propertiesFile) {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(propertiesFile);
			properties.storeToXML(fos, "", "UTF-8");
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			try {
				fos.close();
			}
			catch (IOException e) {
				// Ignore
			}
		}
	}
	
	private static Properties instance() {
		if (cachedConfiguration == null) {
			ClientConfiguration instance = new ClientConfiguration();
			cachedConfiguration = instance.configuration();
		}

		return cachedConfiguration;
	}
	
	public static String getBasePath() {
		String basePath = instance().getProperty("basePath");
		if (basePath.startsWith("~")) {
			basePath = basePath.substring(1);
		}
		
		return basePath;
	}
	
	public static boolean isSignupEnabled() {
		return instance().getProperty("signup.enabled", "false").equals("true");		
	}
		
	public static boolean isSendConfirmationMails() {
		return instance().getProperty("signup.sendConfirmationMails", "false").equals("true");
	}

	public static String getSignupConfirmationUrl() {
		String confirmationUrl = instance().getProperty("signup.confirmationUrl");
		
		return confirmationUrl;
	}
}
