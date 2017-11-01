package com.vaadin.starter.bakery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;

/**
 * Spring boot web application initializer.
 */
@SpringBootApplication
/*
 * To disable web security:
 *
 * @SpringBootApplication(exclude = {
 * org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration.
 * class })
 */
public class Application extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
