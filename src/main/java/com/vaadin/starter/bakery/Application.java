package com.vaadin.starter.bakery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.vaadin.starter.bakery.app.security.SecurityConfiguration;
import com.vaadin.starter.bakery.backend.data.entity.User;
import com.vaadin.starter.bakery.backend.repositories.UserRepository;
import com.vaadin.starter.bakery.backend.service.UserService;
import com.vaadin.starter.bakery.ui.BakeryApp;

/**
 * Spring boot web application initializer.
 */

/*
 * To disable web security:
 *
 * @SpringBootApplication(exclude = {
 * org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration.
 * class })
 */
@SpringBootApplication(scanBasePackageClasses = { SecurityConfiguration.class, BakeryApp.class, Application.class,
		UserService.class })
@EnableJpaRepositories(basePackageClasses = { UserRepository.class })
@EntityScan(basePackageClasses = { User.class })
public class Application extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(Application.class);
	}
}
