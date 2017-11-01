package com.vaadin.starter.bakery.ui;

import com.vaadin.router.Route;
import com.vaadin.router.event.BeforeNavigationEvent;
import com.vaadin.router.event.BeforeNavigationObserver;
import com.vaadin.starter.bakery.ui.view.storefront.StorefrontView;
import com.vaadin.ui.html.Div;

@Route(value = "", layout = BakeryApp.class)
public class DefaultView extends Div implements BeforeNavigationObserver {

	@Override
	public void beforeNavigation(BeforeNavigationEvent event) {
		event.rerouteTo(StorefrontView.class);
	}
}
