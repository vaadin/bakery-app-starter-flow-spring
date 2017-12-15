package com.vaadin.starter.bakery.ui.view;

import com.vaadin.starter.bakery.ui.BakeryLayoutElement;
import com.vaadin.testbench.HasElementQuery;

public interface HasBakeryLayout extends HasElementQuery {

	default BakeryLayoutElement getBakeryLayout() {
		return $(BakeryLayoutElement.class).onPage().first();
	}

	default BakeryNavigationElement getMenu() {
		return getBakeryLayout().getMenu();
	}

}
