package com.vaadin.starter.bakery.ui.view;

import com.vaadin.starter.bakery.AbstractIT;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

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
		StoreFrontViewElement storefront = loginView.login("barista@vaadin.com", "barista");
		storefront.getMenu().getLogoutButton().click();

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