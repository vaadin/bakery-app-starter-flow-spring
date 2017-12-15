package com.vaadin.starter.bakery.ui;

import com.vaadin.flow.model.TemplateModel;
import com.vaadin.router.ParentLayout;
import com.vaadin.router.RouterLayout;
import com.vaadin.router.event.BeforeNavigationEvent;
import com.vaadin.router.event.BeforeNavigationObserver;
import com.vaadin.starter.bakery.app.security.SecurityUtils;
import com.vaadin.starter.bakery.ui.exceptions.AccessDeniedException;
import com.vaadin.ui.Tag;
import com.vaadin.ui.common.HtmlImport;
import com.vaadin.ui.polymertemplate.Id;
import com.vaadin.ui.polymertemplate.PolymerTemplate;

@Tag("bakery-layout")
@HtmlImport("src/app/bakery-layout.html")
@ParentLayout(BakeryApp.class)
public class BakeryLayout extends PolymerTemplate<BakeryLayout.Model> implements RouterLayout, BeforeNavigationObserver {

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
		if (!path.isEmpty()) {
			getModel().setPage(path);
		}
		navigation.updateNavigationBar();
	}
}