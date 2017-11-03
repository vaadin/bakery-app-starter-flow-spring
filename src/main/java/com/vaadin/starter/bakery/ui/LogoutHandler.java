package com.vaadin.starter.bakery.ui;

import com.vaadin.router.Route;
import com.vaadin.router.event.AfterNavigationEvent;
import com.vaadin.router.event.AfterNavigationObserver;
import com.vaadin.ui.History;
import com.vaadin.ui.UI;
import com.vaadin.ui.html.Div;

@Route(value = "logout", layout = BakeryApp.class)
public class LogoutHandler extends Div implements AfterNavigationObserver {

	@Override
	public void afterNavigation(AfterNavigationEvent event) {
		UI ui = getUI().get();
		History history = ui.getPage().getHistory();

		// Reload the page after invalidating the session will redirect
		// to login page
		history.go(0);
	}

}
