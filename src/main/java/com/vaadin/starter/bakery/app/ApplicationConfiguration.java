package com.vaadin.starter.bakery.app;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.PostConstruct;
import java.util.Locale;

/**
 * Spring boot application configuration class.
 * <p>
 * For now it does:
 * <ul>
 * <li>Disables HTTP security (gives access to anyone to any page)</li>
 * </ul>
 *
 */
@Configuration
public class ApplicationConfiguration {

	public static final String LOGIN_URL = "/login";
	public static final String LOGOUT_URL = "/login?logout";
	public static final String LOGIN_FAILURE_URL = "/login?error";
	public static final String LOGIN_PROCESSING_URL = "/login";

	/**
	 * The password encoder to use when encrypting passwords.
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@PostConstruct
	private void init() {
		Locale.setDefault(Locale.Category.FORMAT, Locale.US);
	}

}
