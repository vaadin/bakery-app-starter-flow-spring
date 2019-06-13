package com.vaadin.starter.bakery.testbench;

import org.junit.jupiter.api.Assertions;
import com.vaadin.testbench.addons.junit5.extensions.unittest.VaadinTest;

import com.vaadin.starter.bakery.testbench.elements.components.DashboardLCounterLabelElement;
import com.vaadin.starter.bakery.testbench.elements.ui.DashboardViewElement;
import com.vaadin.starter.bakery.testbench.elements.ui.StorefrontViewElement;

public class DashboardViewIT extends AbstractIT<DashboardViewElement> {

	@Override
	protected DashboardViewElement openView() {
		StorefrontViewElement storefront = openLoginView().login("admin@vaadin.com", "admin");
		return storefront.getMenu().navigateToDashboard();
	}

	@VaadinTest
	public void checkRowsCount() {
		DashboardViewElement dashboardPage = openView();
		Assertions.assertEquals(4, dashboardPage.getBoard().getRows().size());
	}

	@VaadinTest
	public void checkCounters() {
		DashboardViewElement dashboardPage = openView();
		int numLabels = dashboardPage.getBoard().getRows().get(0).$(DashboardLCounterLabelElement.class).all().size();
		Assertions.assertEquals(4, numLabels);
	}
}
