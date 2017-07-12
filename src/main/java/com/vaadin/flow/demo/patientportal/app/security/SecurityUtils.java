package com.vaadin.flow.demo.patientportal.app.security;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.vaadin.ui.UI;

/**
 * SecurityUtils takes care of all such static operations that have to do with
 * security and querying rights from different beans of the UI.
 *
 */
public class SecurityUtils {

	private SecurityUtils() {
		// Util methods only
	}

	/**
	 * Gets the user name of the currently signed in user.
	 *
	 * @return the user name of the current user or <code>null</code> if the
	 *         user has not signed in
	 */
	public static String getUsername() {
		SecurityContext context = SecurityContextHolder.getContext();
		UserDetails userDetails = (UserDetails) context.getAuthentication().getPrincipal();
		return userDetails.getUsername();
	}

	/**
	 * Check if currently signed-in user is in the role with the given role
	 * name.
	 *
	 * @param role
	 *            the role to check for
	 * @return <code>true</code> if user is in the role, <code>false</code>
	 *         otherwise
	 */
	public static boolean isCurrentUserInRole(String role) {
		return getUserRoles().stream().filter(roleName -> roleName.equals(Objects.requireNonNull(role))).findAny()
				.isPresent();
	}

	/**
	 * Gets the roles the currently signed-in user belongs to.
	 *
	 * @return a set of all roles the currently signed-in user belongs to.
	 */
	public static Set<String> getUserRoles() {
		SecurityContext context = SecurityContextHolder.getContext();
		return context.getAuthentication().getAuthorities().stream().map(GrantedAuthority::getAuthority)
				.collect(Collectors.toSet());
	}

	/**
	 * Checks if access is granted for the current user for the given secured
	 * view within the given ui.
	 *
	 * @param viewSecured
	 * @return true if access is granted, false otherwise.
	 */
	public static boolean isAccessGranted(Secured viewSecured) {
		if (viewSecured == null) {
			return true;
		}

		return Arrays.asList(viewSecured.value()).stream().anyMatch(SecurityUtils::isCurrentUserInRole);
	}

}
