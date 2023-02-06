package com.vaadin.starter.bakery.app.security;

import com.vaadin.flow.spring.security.VaadinWebSecurity;
import com.vaadin.starter.bakery.backend.data.entity.User;
import com.vaadin.starter.bakery.backend.repositories.UserRepository;
import com.vaadin.starter.bakery.ui.views.login.LoginView;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Configures spring security, doing the following:
 * <li>Bypass security checks for static resources,</li>
 * <li>Restrict access to the application, allowing only logged in users,</li>
 * <li>Set up the login form,</li>
 * <li>Configures the {@link UserDetailsServiceImpl}.</li>
 *
 */
@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends VaadinWebSecurity {

	/**
	 * The password encoder to use when encrypting passwords.
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public CurrentUser currentUser(UserRepository userRepository) {
		final String username = SecurityUtils.getUsername();
		User user = username != null
				? userRepository.findByEmailIgnoreCase(username)
				: null;
		return () -> user;
	}

	/**
	 * Require login to access internal pages and configure login form.
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests().requestMatchers(new AntPathRequestMatcher("/robots.txt")).permitAll();
		http.authorizeHttpRequests().requestMatchers(new AntPathRequestMatcher("/icons/**")).permitAll();
		http.authorizeHttpRequests().requestMatchers(new AntPathRequestMatcher("/images/**")).permitAll();
		http.authorizeHttpRequests().requestMatchers(new AntPathRequestMatcher("/h2-console/**")).permitAll();
		super.configure(http);
		setLoginView(http, LoginView.class);
	}
}
