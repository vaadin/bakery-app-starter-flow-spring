package com.vaadin.starter.bakery.app.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;

import com.vaadin.starter.bakery.BakeryApplicationConfig;
import com.vaadin.starter.bakery.backend.data.Role;

@EnableWebSecurity
@Configuration
@Order(20)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private final UserDetailsService userDetailsService;

	private final PasswordEncoder passwordEncoder;

	@Autowired
	public SecurityConfig(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
		this.userDetailsService = userDetailsService;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		super.configure(auth);
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// Not using Spring CSRF here to be able to use plain HTML for the login
		// page
		http.csrf().disable().requestCache().requestCache(new CustomRequestCache()).and().authorizeRequests()
				.anyRequest().hasAnyAuthority(Role.getAllRoles()).and().formLogin()
				.loginPage(BakeryApplicationConfig.LOGIN_URL).permitAll()
				.loginProcessingUrl(BakeryApplicationConfig.LOGIN_PROCESSING_URL)
				.failureUrl(BakeryApplicationConfig.LOGIN_FAILURE_URL)
				.successHandler(new SavedRequestAwareAuthenticationSuccessHandler()).and().logout()
				.logoutSuccessUrl(BakeryApplicationConfig.LOGOUT_URL);
	}

	@Override
	public void configure(WebSecurity web) throws Exception {

		web.ignoring()
				.antMatchers("/resources/**", "/icons/**", "/fonts/**", "/api/**", "/manifest.json",
						"/service-worker.js", "/bower_components/**", "/VAADIN/**", "/favico.ico",
						"/src/login/bakery-login.html", "/src/app/**","/src/elements/**", "/es5/**", "/es6/**")
				.and().ignoring().requestMatchers(this::isHeartbeat);
	}

	private boolean isHeartbeat(HttpServletRequest request) {
		final String HEARTBEAT_PARAMETER = "v-r";
		final String HEARTBEAT_PARAMETER_VALUE = "heartbeat";
		return HEARTBEAT_PARAMETER_VALUE.equals(request.getParameter(HEARTBEAT_PARAMETER));
	}

	class CustomRequestCache extends HttpSessionRequestCache {
		@Override
		public void saveRequest(HttpServletRequest request, HttpServletResponse response) {
			if (!isHeartbeat(request)) {
				super.saveRequest(request, response);
			}
		}

	}
}
