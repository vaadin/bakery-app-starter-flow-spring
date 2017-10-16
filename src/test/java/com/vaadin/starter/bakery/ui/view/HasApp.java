package com.vaadin.starter.bakery.ui.view;

import com.vaadin.starter.bakery.ui.BakeryAppElement;
import com.vaadin.testbench.HasElementQuery;

public interface HasApp extends HasElementQuery {

	default BakeryAppElement getApp() {
		return $(BakeryAppElement.class).onPage().first();
	}

	default BakeryNavigationElement getMenu() {
		return getApp().getMenu();
	}

}
