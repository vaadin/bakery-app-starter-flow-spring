package com.vaadin.starter.bakery.testbench;

import static org.junit.Assert.assertEquals;

import org.junit.Assert;
import org.junit.Test;

import com.vaadin.starter.bakery.testbench.elements.ui.LoginViewElement;
import com.vaadin.starter.bakery.testbench.elements.ui.StorefrontViewElement;

public class LoginIT extends AbstractIT<LoginViewElement> {

	@Test
	public void loginWorks() {
		LoginViewElement loginView = openLoginView();
		assertEquals("Email", loginView.getUsernameLabel());
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
		// FIXME since V18 login from dashboard does not work redirect correctly in IT's 
		// though it works when using the app normally.
		StorefrontViewElement dashboard = loginView.login("admin@vaadin.com", "admin", StorefrontViewElement.class);
		Assert.assertNotNull(dashboard);
	}

	@Test
	public void openLoginAfterLoggedIn() {
		loginToNotDefaultUrl();
		// Navigating to /login after user is logged in will forward to storefront view
		driver.get(APP_URL + "login");
		$(StorefrontViewElement.class).onPage().waitForFirst();
		Assert.assertTrue($(LoginViewElement.class).all().isEmpty());
	}

	@Override
	protected LoginViewElement openView() {
		return openLoginView();
	}

}