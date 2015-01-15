package de.geoinfoffm.registry.client.web;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.util.StringUtils;

public class BasePathUrlAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler implements AuthenticationFailureHandler
{

	public BasePathUrlAuthenticationFailureHandler() {
		super();
		this.setRedirectStrategy(new BasePathRedirectStrategy());
	}

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
		String failureUrl;
		
		String targetUrl = request.getParameter("targetUrl");
		if (StringUtils.isEmpty(targetUrl)) {
			failureUrl = "/login?error";
		}
		else {
			failureUrl = "/login?error&targetUrl=" + URLEncoder.encode(targetUrl, "UTF-8");
		}
		
        logger.debug("Redirecting to " + failureUrl);
        this.getRedirectStrategy().sendRedirect(request, response, failureUrl);
	}
	
	
}
