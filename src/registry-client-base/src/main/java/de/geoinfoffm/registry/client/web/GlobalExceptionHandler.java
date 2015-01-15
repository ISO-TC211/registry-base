package de.geoinfoffm.registry.client.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import de.geoinfoffm.registry.core.UnauthorizedException;

@ControllerAdvice
public class GlobalExceptionHandler
{

	public GlobalExceptionHandler() {
		// TODO Auto-generated constructor stub
	}
	
	@ExceptionHandler(UnauthorizedException.class)
	public ModelAndView handleUnauthorizedException(final HttpServletRequest servletRequest, final WebRequest webRequest, final Exception e) {
		ModelAndView mav = new ModelAndView();
        mav.addObject("exception", e);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object principal = auth.getPrincipal();
		if (principal instanceof UserDetails && isLoggedIn((UserDetails)principal)) {
			mav.setViewName("/403");
		}
		else {
	        mav.addObject("targetUrl", servletRequest.getRequestURL());
	        mav.setViewName("redirect:/login");
		}

        return mav;
	}

	private boolean isLoggedIn(UserDetails user) {
		return user.isEnabled() && user.isAccountNonExpired() && user.isAccountNonLocked() && user.isCredentialsNonExpired();
	}
}
