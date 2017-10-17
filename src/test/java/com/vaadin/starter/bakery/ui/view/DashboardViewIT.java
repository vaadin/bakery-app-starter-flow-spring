package com.vaadin.starter.bakery.ui.view;

import com.vaadin.starter.bakery.AbstractIT;
import com.vaadin.starter.bakery.ui.components.DashboardOrdersCounterElement;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class DashboardViewIT extends AbstractIT {

	private DashboardViewElement openDashboardPage() {
		StoreFrontViewElement storefront = openLoginView().login("admin@vaadin.com", "admin");
		return storefront.getMenu().navigateToDashboard();
	}

	@Test
	public void checkRowsCount() {
		DashboardViewElement dashboardPage = openDashboardPage();
		Assert.assertTrue(dashboardPage.getBoard().getRows().size() == 4);
	}

	@Test
	public void checkCounters() {
		DashboardViewElement dashboardPage = openDashboardPage();
		int count = dashboardPage.getBoard().getRows().get(0).$(DashboardOrdersCounterElement.class).all().size();
		Assert.assertTrue(count == 4);
	}

}
