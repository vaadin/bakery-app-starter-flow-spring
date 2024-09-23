package com.vaadin.starter.bakery.app.security;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;

import com.vaadin.flow.spring.security.AuthenticationContext;
import com.vaadin.starter.bakery.backend.data.entity.User;

/**
 * Provide Beans for the control center security context.
 * These beans might be removed, but needs important changes in code.
 */
@Configuration
@Profile("control-center")
public class SecurityConfigurationCC {

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    CurrentUser currentUser(AuthenticationContext authCtx) {
        User user = new User();
        user.setFirstName(authCtx.getPrincipalName().orElse(null));
        return () -> user;
    }	
}
