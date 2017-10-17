package com.vaadin.starter.bakery.ui.view;

import com.vaadin.starter.bakery.elements.PaperTabElement;
import com.vaadin.starter.bakery.ui.UsersViewElement;
import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.elementsbase.Element;

@Element("bakery-navigation")
public class BakeryNavigationElement extends TestBenchElement {

	public UsersViewElement navigateToUsers() {
		$(PaperTabElement.class).attribute("page-id", "users").first().click();
		return $(UsersViewElement.class).onPage().waitForFirst();
	}

	public ProductsViewElement navigateToProducts() {
		$(PaperTabElement.class).attribute("page-id", "products").first().click();
		return $(ProductsViewElement.class).onPage().waitForFirst();
	}

}
