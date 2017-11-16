package com.vaadin.starter.bakery.app.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;

import com.vaadin.starter.bakery.backend.data.Role;
import com.vaadin.starter.bakery.ui.utils.BakeryConst;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	private static final String LOGIN_PROCESSING_URL = "/login";
	private static final String LOGIN_FAILURE_URL = "/login?error";
	private static final String LOGIN_URL = "/login";
	private static final String LOGOUT_SUCCESS_URL = "/" + BakeryConst.PAGE_STOREFRONT;

	private final UserDetailsService userDetailsService;

	@Autowired
	public SecurityConfiguration(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	/**
	 * The password encoder to use when encrypting passwords.
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		super.configure(auth);
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// Not using Spring CSRF here to be able to use plain HTML for the login page
		http.csrf().disable()
				.requestCache().requestCache(new CustomRequestCache())
				.and().authorizeRequests()
					.requestMatchers(this::isFrameworkInternalRequest).permitAll()
					.anyRequest().hasAnyAuthority(Role.getAllRoles())
				.and().formLogin()
					.loginPage(LOGIN_URL).permitAll()
					.loginProcessingUrl(LOGIN_PROCESSING_URL)
					.failureUrl(LOGIN_FAILURE_URL)
					.successHandler(new SavedRequestAwareAuthenticationSuccessHandler())
				.and().logout()
					.logoutSuccessUrl(LOGOUT_SUCCESS_URL);
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring()
		.antMatchers(
				// Vaadin Flow static resources
				"/VAADIN/**",

				// the standard favicon URI
				"/favicon.ico",

				// development-mode static resources
				"/bower_components/**",
				"/icons/**",
				"/images/**",
				"/src/**",
				"/manifest.json",
				"/app.js",
				"/sw.js",
				"/offline-page.html",

				// development-mode webjars
				"/webjars/**",

				// production-mode static resources
				"/build/**",
				"/frontend-es5/**",
				"/frontend-es6/**");
	}

	private boolean isFrameworkInternalRequest(HttpServletRequest request) {
		final String FRAMEWORK_INTERNAL_REQUEST_PARAMETER = "v-r";
		final String HEARTBEAT_PARAMETER_VALUE = "heartbeat";
		final String UIDL_PARAMETER_VALUE = "uidl";
		final String parameterValue = request.getParameter(FRAMEWORK_INTERNAL_REQUEST_PARAMETER);
		return parameterValue != null
				&& (parameterValue.equals(HEARTBEAT_PARAMETER_VALUE) || parameterValue.equals(UIDL_PARAMETER_VALUE));
	}

	class CustomRequestCache extends HttpSessionRequestCache {
		@Override
		public void saveRequest(HttpServletRequest request, HttpServletResponse response) {
			if (!isFrameworkInternalRequest(request)) {
				super.saveRequest(request, response);
			}
		}

	}
}
