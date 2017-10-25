package com.vaadin.starter.bakery.ui;

import com.vaadin.flow.model.TemplateModel;
import com.vaadin.router.RouterLayout;
import com.vaadin.router.event.BeforeNavigationEvent;
import com.vaadin.router.event.BeforeNavigationListener;
import com.vaadin.spring.annotation.VaadinSessionScope;
import com.vaadin.starter.bakery.ui.utils.BakeryConst;
import com.vaadin.ui.Tag;
import com.vaadin.ui.UI;
import com.vaadin.ui.event.AttachEvent;
import com.vaadin.ui.common.HtmlImport;
import com.vaadin.ui.polymertemplate.Id;
import com.vaadin.ui.polymertemplate.PolymerTemplate;

@Tag("bakery-app")
@HtmlImport("src/app/bakery-app.html")
@VaadinSessionScope
public class BakeryApp extends PolymerTemplate<BakeryApp.Model> implements RouterLayout, BeforeNavigationListener {

	// TODO(vlukashov, 2017.08.11): Remove this once https://github.com/vaadin/flow/issues/2152 is closed.
	// when bundling is enabled the Flow's sub-template feature does not work
	@Id("navigation")
	private BakeryNavigation navigation;

	public interface Model extends TemplateModel {
		void setPage(String page);
	}

	@Override
	protected void onAttach(AttachEvent attachEvent) {
		super.onAttach(attachEvent);

		// TODO(vlukashov, 2017.08.11): Remove this once https://github.com/vaadin/flow/issues/1969 is closed.
		// ensure the app shell bundle is loaded before any others
		UI.getCurrent().getPage().addHtmlImport("context://src/app/bakery-app.html");
	}

	@Override
	public void beforeNavigation(BeforeNavigationEvent event) {
		String path = event.getLocation().getFirstSegment();
		if (path.isEmpty()) {
			event.rerouteTo(BakeryConst.PAGE_DEFAULT);
		} else {
			getModel().setPage(path);
		}
		navigation.updateUser();
	}
}