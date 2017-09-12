package com.vaadin.starter.bakery.ui.view;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.vaadin.starter.bakery.AbstractIT;

public class LoginIT extends AbstractIT {

	@Test
	public void logingWorks() {
		LoginViewElement loginView = openLoginView();
		assertEquals("Email", loginView.getLoginLabel());
		loginView.login("barista@vaadin.com", "barista");
	}

}