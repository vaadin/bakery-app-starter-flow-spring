package com.vaadin.starter.bakery.app.security;

import java.io.Serializable;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

@Component
public class SecuredViewAccessControl implements  Serializable {

	public static boolean isAccessGranted(Class viewClass) {
		Secured viewSecured = AnnotationUtils.findAnnotation(viewClass, Secured.class);
		return isAccessGranted(viewSecured);
	}

	private static boolean isAccessGranted(Secured viewSecured) {
		return SecurityUtils.isAccessGranted(viewSecured);
	}
}
