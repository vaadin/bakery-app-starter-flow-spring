package com.vaadin.starter.bakery.tb.elements.ui;

import com.vaadin.testbench.HasElementQuery;

public interface HasApp extends HasElementQuery {

	default BakeryAppElement getApp() {
		return $(BakeryAppElement.class).onPage().first();
	}

	default BakeryNavigationElement getMenu() {
		return getApp().getMenu();
	}

}
