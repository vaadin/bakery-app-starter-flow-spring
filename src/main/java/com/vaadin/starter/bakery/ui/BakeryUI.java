package com.vaadin.starter.bakery.ui;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinRequest;
import com.vaadin.starter.bakery.app.security.SecurityUtils;
import com.vaadin.starter.bakery.ui.components.OfflineBanner;
import com.vaadin.starter.bakery.ui.exceptions.AccessDeniedException;
import com.vaadin.starter.bakery.ui.views.login.LoginView;

public class BakeryUI extends UI {

	protected void init(VaadinRequest request) {
		add(new OfflineBanner());
		addBeforeEnterListener(event -> {
			final boolean accessGranted =
				SecurityUtils.isAccessGranted(event.getNavigationTarget());
			if (!accessGranted) {
				if (SecurityUtils.isUserLoggedIn()) {
					event.rerouteToError(AccessDeniedException.class);
				}
				else {
					event.rerouteTo(LoginView.class);
				}
			}
		});
	}
}
