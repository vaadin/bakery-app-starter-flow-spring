package com.vaadin.starter.bakery.ui;

import com.vaadin.flow.model.TemplateModel;
import com.vaadin.router.RouterLayout;
import com.vaadin.router.event.AfterNavigationEvent;
import com.vaadin.router.event.AfterNavigationObserver;
import com.vaadin.router.event.BeforeNavigationEvent;
import com.vaadin.router.event.BeforeNavigationObserver;
import com.vaadin.starter.bakery.app.security.SecurityUtils;
import com.vaadin.starter.bakery.ui.exceptions.AccessDeniedException;
import com.vaadin.ui.Tag;
import com.vaadin.ui.common.HtmlImport;
import com.vaadin.ui.polymertemplate.Id;
import com.vaadin.ui.polymertemplate.PolymerTemplate;

@Tag("bakery-app")
@HtmlImport("src/app/bakery-app.html")
public class BakeryApp extends PolymerTemplate<TemplateModel>
		implements RouterLayout, BeforeNavigationObserver, AfterNavigationObserver {

	@Id("navigation")
	private BakeryNavigation navigation;

	@Override
	public void beforeNavigation(BeforeNavigationEvent event) {
		if (!SecurityUtils.isAccessGranted(event.getNavigationTarget())) {
			event.rerouteToError(AccessDeniedException.class);
		}
	}

	@Override
	public void afterNavigation(AfterNavigationEvent event) {
		navigation.onLocationChange(event.getLocation().getPath());
	}
}