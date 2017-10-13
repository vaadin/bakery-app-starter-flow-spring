package com.vaadin.starter.bakery.ui.view;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.vaadin.starter.bakery.AbstractIT;

public class LoginIT extends AbstractIT {

	@Test
	public void loginWorks() {
		LoginViewElement loginView = openLoginView();
		assertEquals("Email", loginView.getLogin().getLabel());
		loginView.login("barista@vaadin.com", "barista");
	}

}