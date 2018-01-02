package com.vaadin.starter.bakery.ui;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.BeforeNavigationEvent;
import com.vaadin.flow.router.BeforeNavigationObserver;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.templatemodel.TemplateModel;
import com.vaadin.starter.bakery.app.security.SecurityUtils;
import com.vaadin.starter.bakery.ui.exceptions.AccessDeniedException;
import com.vaadin.starter.bakery.ui.utils.BakeryConst;

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
		String currentPath = event.getLocation().getFirstSegment().isEmpty() ? BakeryConst.PAGE_DEFAULT
				: event.getLocation().getFirstSegment();
		navigation.onLocationChange(currentPath);
	}
}
