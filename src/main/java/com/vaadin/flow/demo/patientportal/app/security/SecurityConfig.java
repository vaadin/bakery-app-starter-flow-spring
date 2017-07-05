package com.vaadin.flow.demo.patientportal.app.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import com.vaadin.flow.demo.patientportal.PatientApplicationConfig;
import com.vaadin.flow.demo.patientportal.backend.data.Role;

@EnableWebSecurity
@Configuration
@Order(20)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private final UserDetailsService userDetailsService;

	private final PasswordEncoder passwordEncoder;

	private final RedirectAuthenticationSuccessHandler successHandler;

	@Autowired
	public SecurityConfig(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder,
			RedirectAuthenticationSuccessHandler successHandler) {
		this.userDetailsService = userDetailsService;
		this.passwordEncoder = passwordEncoder;
		this.successHandler = successHandler;
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
		http.csrf().disable();

		ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry reg = http
			.authorizeRequests();

		// Allow access to static resources ("/VAADIN/**")
		reg = reg.antMatchers("/icons/**").permitAll();
		reg = reg.antMatchers("/fonts/**").permitAll();
		reg = reg.antMatchers("/api/**").permitAll();
		// Require authentication for all URLS ("/**")
		reg = reg.antMatchers("/**").hasAnyAuthority(Role.getAllRoles());
		HttpSecurity sec = reg.and();

		// Allow access to login page without login
		FormLoginConfigurer<HttpSecurity> login = sec.formLogin().permitAll();
		login = login.loginPage(PatientApplicationConfig.LOGIN_URL).loginProcessingUrl(PatientApplicationConfig.LOGIN_PROCESSING_URL)
			.failureUrl(PatientApplicationConfig.LOGIN_FAILURE_URL).successHandler(successHandler);
		login.and().logout().logoutSuccessUrl(PatientApplicationConfig.LOGOUT_URL);
	}


}
