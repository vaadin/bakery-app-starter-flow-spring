package com.vaadin.starter.bakery.tb;

import static org.junit.Assert.assertEquals;

import org.junit.Assert;
import org.junit.Test;

import com.vaadin.starter.bakery.tb.elements.ui.DashboardViewElement;
import com.vaadin.starter.bakery.tb.elements.ui.LoginViewElement;
import com.vaadin.starter.bakery.tb.elements.ui.StorefrontViewElement;

public class LoginIT extends AbstractIT {

	@Test
	public void loginWorks() {
		LoginViewElement loginView = openLoginView();
		assertEquals("Email", loginView.getLogin().getLabel());
		loginView.login("barista@vaadin.com", "barista");
	}

	@Test
	public void logout() {
		LoginViewElement loginView = openLoginView();
		StorefrontViewElement storefront = loginView.login("barista@vaadin.com", "barista");
		storefront.getMenu().logout();
		Assert.assertTrue(getDriver().getCurrentUrl().endsWith("login"));
	}

	@Test
	public void loginToNotDefaultUrl() {
		LoginViewElement loginView = openLoginView(getDriver(), APP_URL + "dashboard");
		loginView.getLogin().setValue("admin@vaadin.com");
		loginView.getPassword().setValue("admin");
		loginView.getSignIn().click();
		DashboardViewElement dashboard = $(DashboardViewElement.class).onPage().waitForFirst();
		Assert.assertNotNull(dashboard);
	}

}