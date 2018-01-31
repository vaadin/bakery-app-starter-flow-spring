package com.vaadin.starter.bakery.testbench.elements.components;

import com.vaadin.starter.bakery.testbench.elements.core.PaperTabElement;
import com.vaadin.starter.bakery.testbench.elements.ui.DashboardViewElement;
import com.vaadin.starter.bakery.testbench.elements.ui.LoginViewElement;
import com.vaadin.starter.bakery.testbench.elements.ui.ProductsViewElement;
import com.vaadin.starter.bakery.testbench.elements.ui.UsersViewElement;
import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.elementsbase.Element;

@Element("app-navigation")
public class BakeryNavigationElement extends TestBenchElement {

	public DashboardViewElement navigateToDashboard() {
		return navigateTo(1, DashboardViewElement.class);
	}

	public UsersViewElement navigateToUsers() {
		return navigateTo(2, UsersViewElement.class);
	}

	public ProductsViewElement navigateToProducts() {
		return navigateTo(3, ProductsViewElement.class);
	}

	public LoginViewElement logout() {
		$(PaperTabElement.class).last().click();
		return $(LoginViewElement.class).onPage().waitForFirst();
	}

	private <T extends TestBenchElement> T navigateTo(int index, Class<T> landingPage) {
		$(PaperTabElement.class).get(index).click();
		return $(landingPage).onPage().waitForFirst();
	}
}
