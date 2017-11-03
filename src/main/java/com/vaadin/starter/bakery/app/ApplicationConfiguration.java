package com.vaadin.starter.bakery.app;

import java.util.Locale;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Configuration;

/**
 * Spring boot application configuration class.
 *
 */
@Configuration
public class ApplicationConfiguration {

	@PostConstruct
	private void init() {
		Locale.setDefault(Locale.Category.FORMAT, Locale.US);
	}

}
