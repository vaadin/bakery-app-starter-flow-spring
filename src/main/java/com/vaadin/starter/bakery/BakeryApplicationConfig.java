package com.vaadin.starter.bakery;

import java.util.Locale;
import javax.annotation.PostConstruct;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.vaadin.starter.bakery.backend.data.entity.User;
import com.vaadin.starter.bakery.backend.util.LocalDateJpaConverter;
import com.vaadin.starter.bakery.repositories.UserRepository;

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
@ComponentScan({
    "com.vaadin.starter.bakery",
    "com.vaadin.starter.bakery.backend.service",
    "com.vaadin.starter.bakery.app",
    "com.vaadin.starter.bakery.app.security" })
@EnableJpaRepositories(basePackageClasses = { UserRepository.class})
@EntityScan(basePackageClasses={User.class, LocalDateJpaConverter.class})
public class BakeryApplicationConfig extends WebSecurityConfigurerAdapter {

    public static final String APP_URL = "/";
    public static final String LOGIN_URL = "/login";
    public static final String LOGOUT_URL = "/login?logout";
    public static final String LOGIN_FAILURE_URL = "/login?error";
    public static final String LOGIN_PROCESSING_URL = "/login";

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @PostConstruct
    private void init() {
        Locale.setDefault(Locale.Category.FORMAT, Locale.US);
    }
}
