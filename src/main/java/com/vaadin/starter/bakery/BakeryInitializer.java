package com.vaadin.starter.bakery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.WebApplicationInitializer;

/**
 * Spring boot web appplication initializer.
 * <p>
 * The initializer registers the {@link BakeryServlet} Vaadin servlet.
 *
 * @author Vaadin Ltd
 *
 */
@SpringBootApplication
/*
 * To disable we security:
 *
 * @SpringBootApplication(exclude = {
 * org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration.
 * class })
 */
public class BakeryInitializer extends SpringBootServletInitializer implements WebApplicationInitializer {

	public static void main(String[] args) {
		SpringApplication.run(BakeryInitializer.class, args);
	}

	@Bean
	public ServletRegistrationBean servletRegistrationBean() {
		return new ServletRegistrationBean(new BakeryServlet(), "/*");
	}

}
