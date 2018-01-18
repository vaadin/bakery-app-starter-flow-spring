// vaadin-com-generator:exclude
/*
	The line above is a marker for the vaadin.com/start page .zip file generator.
	It ensures that this file is excluded from the generated project package.
*/
package com.vaadin.starter.bakery.app.security;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.vaadin.starter.bakery.backend.data.Role;



/**
 * Allows accessing all views without login for performance testing.
 */
@Configuration
@Order(1)
@Profile("performance-test")
public class PerformanceTestSecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// Not using Spring CSRF here to be able to use plain HTML for the login page
		http.csrf().disable().authorizeRequests()
				// Allow all requests by anonymous users.
				.anyRequest().permitAll().and().anonymous()
				.principal(new User("admin@vaadin.com", "", Arrays.asList(new SimpleGrantedAuthority("admin"))))
				.authorities(Arrays.stream(Role.getAllRoles()).map(SimpleGrantedAuthority::new)
						.collect(Collectors.toList()));
	}

}