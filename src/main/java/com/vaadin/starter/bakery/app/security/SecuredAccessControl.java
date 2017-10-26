package com.vaadin.starter.bakery.app.security;

import java.io.Serializable;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

@Component
public class SecuredAccessControl implements Serializable {

	public static boolean isAccessGranted(Class clazz) {
		Secured secured = AnnotationUtils.findAnnotation(clazz, Secured.class);
		return isAccessGranted(secured);
	}

	private static boolean isAccessGranted(Secured secured) {
		return SecurityUtils.isAccessGranted(secured);
	}
}
