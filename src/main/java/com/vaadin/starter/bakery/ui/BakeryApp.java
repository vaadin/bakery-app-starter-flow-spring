package com.vaadin.starter.bakery.ui;

import com.vaadin.flow.model.TemplateModel;
import com.vaadin.router.RouterLayout;
import com.vaadin.router.event.BeforeNavigationEvent;
import com.vaadin.router.event.BeforeNavigationObserver;
import com.vaadin.starter.bakery.app.security.SecurityUtils;
import com.vaadin.starter.bakery.ui.components.BakeryNavigation;
import com.vaadin.starter.bakery.ui.exceptions.AccessDeniedException;
import com.vaadin.starter.bakery.ui.utils.BakeryConst;
import com.vaadin.ui.Tag;
import com.vaadin.ui.common.HtmlImport;
import com.vaadin.ui.polymertemplate.Id;
import com.vaadin.ui.polymertemplate.PolymerTemplate;

@Tag("bakery-app")
@HtmlImport("src/app/bakery-app.html")
public class BakeryApp extends PolymerTemplate<BakeryApp.Model> implements RouterLayout, BeforeNavigationObserver {

	@Id("navigation")
	private BakeryNavigation navigation;

	public interface Model extends TemplateModel {
		void setPage(String page);
	}

	@Override
	public void beforeNavigation(BeforeNavigationEvent event) {
		if (!SecurityUtils.isAccessGranted(event.getNavigationTarget())) {
			event.rerouteToError(AccessDeniedException.class);
			return;
		}
		String path = event.getLocation().getFirstSegment();
		if (path.isEmpty()) {
			event.rerouteTo(BakeryConst.PAGE_DEFAULT);
		} else {
			getModel().setPage(path);
		}
		navigation.updateNavigationBar();
	}
}