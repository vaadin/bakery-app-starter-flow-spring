package com.vaadin.starter.bakery.ui.view;

import com.vaadin.starter.bakery.AbstractIT;
import com.vaadin.starter.bakery.ui.components.DashboardLCounterLabelElement;
import org.junit.Assert;
import org.junit.Test;

public class DashboardViewIT extends AbstractIT {

	private DashboardViewElement openDashboardPage() {
		StorefrontViewElement storefront = openLoginView().login("admin@vaadin.com", "admin");
		return storefront.getMenu().navigateToDashboard();
	}

	@Test
	public void checkRowsCount() {
		DashboardViewElement dashboardPage = openDashboardPage();
		Assert.assertEquals(4, dashboardPage.getBoard().getRows().size());
	}

	@Test
	public void checkCounters() {
		DashboardViewElement dashboardPage = openDashboardPage();
		int numLabels = dashboardPage.getBoard().getRows().get(0).$(DashboardLCounterLabelElement.class).all().size();
		Assert.assertEquals(4, numLabels);
	}

}
