package com.vaadin.starter.bakery.app.security;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.vaadin.starter.bakery.ui.views.errors.AccessDeniedView;
import com.vaadin.starter.bakery.ui.views.errors.CustomRouteNotFoundError;
import com.vaadin.starter.bakery.ui.views.login.LoginView;

/**
 * SecurityUtils takes care of all such static operations that have to do with
 * security and querying rights from different beans of the UI.
 *
 */
public final class SecurityUtils {

	private SecurityUtils() {
		// Util methods only
	}

	/**
	 * Gets the user name of the currently signed in user.
	 *
	 * @return the user name of the current user or <code>null</code> if the user
	 *         has not signed in
	 */
	public static String getUsername() {
		SecurityContext context = SecurityContextHolder.getContext();
		if (context != null && context.getAuthentication() != null) {
			Object principal = context.getAuthentication().getPrincipal();
			if(principal instanceof UserDetails) {
				UserDetails userDetails = (UserDetails) context.getAuthentication().getPrincipal();
				return userDetails.getUsername();
			}
		}
		// Anonymous or no authentication.
		return null;
	}

	/**
	 * Checks if access is granted for the current user for the given secured view,
	 * defined by the view class.
	 *
	 * @param securedClass View class
	 * @return true if access is granted, false otherwise.
	 */
	public static boolean isAccessGranted(Class<?> securedClass) {
		final boolean publicView = LoginView.class.equals(securedClass)
			|| AccessDeniedView.class.equals(securedClass)
			|| CustomRouteNotFoundError.class.equals(securedClass);

		// Always allow access to public views
		if (publicView) {
			return true;
		}

		Authentication userAuthentication = SecurityContextHolder.getContext().getAuthentication();

		// All other views require authentication
		if (!isUserLoggedIn(userAuthentication)) {
			return false;
		}

		// Allow if no roles are required.
		Secured secured = AnnotationUtils.findAnnotation(securedClass, Secured.class);
		if (secured == null) {
			return true;
		}

		List<String> allowedRoles = Arrays.asList(secured.value());
		return userAuthentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
				.anyMatch(allowedRoles::contains);
	}

	/**
	 * Checks if the user is logged in.
	 *
	 * @return true if the user is logged in. False otherwise.
	 */
	public static boolean isUserLoggedIn() {
		return isUserLoggedIn(SecurityContextHolder.getContext().getAuthentication());
	}

	private static boolean isUserLoggedIn(Authentication authentication) {
		return authentication != null
			&& !(authentication instanceof AnonymousAuthenticationToken);
	}

}
