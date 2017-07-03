package com.vaadin.flow.demo.patientportal.app.security;

import java.io.Serializable;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.vaadin.flow.router.View;
import com.vaadin.ui.UI;

@Component
public class SecuredViewAccessControl implements  Serializable {

	private transient WebApplicationContext webApplicationContext = null;


	public boolean isAccessGranted(UI ui, Class<? extends View> viewClass) {
		Secured viewSecured = AnnotationUtils.findAnnotation(viewClass, Secured.class);
		return isAccessGranted(ui, viewSecured);
	}

	private boolean isAccessGranted(UI ui, Secured viewSecured) {
		return SecurityUtils.isAccessGranted(ui, viewSecured);
	}

}
