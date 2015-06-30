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

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.View;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;

public class RegistryClientViewResolver extends ThymeleafViewResolver
{
    private static final Logger vrlogger = LoggerFactory.getLogger(RegistryClientViewResolver.class);

	public RegistryClientViewResolver() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected View createView(String viewName, Locale locale) throws Exception {
        if (!canHandle(viewName, locale)) {
            vrlogger.trace("[GDIREG] View \"{}\" cannot be handled by RegistryClientViewResolver. Passing on to the next resolver in the chain.", viewName);
            return null;
        }
        if (viewName.startsWith(REDIRECT_URL_PREFIX)) {
            vrlogger.trace("[GDIREG] View \"{}\" is a redirect, and will not be handled directly by RegistryClientViewResolver.", viewName);
            final String redirectUrl = viewName.substring(REDIRECT_URL_PREFIX.length());
//          return new BasePathRedirectView(redirectUrl, isRedirectContextRelative(), isRedirectHttp10Compatible());
          return new BasePathRedirectView(redirectUrl, false, isRedirectHttp10Compatible());
        }
        if (viewName.startsWith(FORWARD_URL_PREFIX)) {
        	throw new RuntimeException("Prefix 'forward:' not yet implemented");
//            vrlogger.trace("[GDIREG] View \"{}\" is a forward, and will not be handled directly by RegistryClientViewResolver.", viewName);
//            final String forwardUrl = viewName.substring(FORWARD_URL_PREFIX.length());
//            return new InternalResourceView(forwardUrl);
        }
        vrlogger.trace("[GDIREG] View {} will be handled by RegistryClientViewResolver and a " +
        		"{} instance will be created for it", viewName, this.getViewClass().getSimpleName());
        return loadView(viewName, locale);
	}
}
