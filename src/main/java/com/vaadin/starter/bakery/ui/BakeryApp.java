package com.vaadin.starter.bakery.ui;

import com.vaadin.annotations.HtmlImport;
import com.vaadin.annotations.Id;
import com.vaadin.annotations.JavaScript;
import com.vaadin.annotations.Tag;
import com.vaadin.flow.router.HasChildView;
import com.vaadin.flow.router.LocationChangeEvent;
import com.vaadin.flow.router.View;
import com.vaadin.flow.template.PolymerTemplate;
import com.vaadin.flow.template.model.TemplateModel;
import com.vaadin.hummingbird.ext.spring.annotations.UIScope;
import com.vaadin.shared.ui.LoadMode;
import com.vaadin.starter.bakery.ui.utils.BakeryConst;
import com.vaadin.ui.AttachEvent;
import com.vaadin.ui.UI;

@Tag("bakery-app")
@HtmlImport("frontend://src/app/bakery-app.html")
@JavaScript(value = "frontend://service-worker-loader.js", loadMode = LoadMode.LAZY)
@UIScope
public class BakeryApp extends PolymerTemplate<BakeryApp.Model> implements HasChildView {

	// TODO(vlukashov, 2017.08.11): Remove this once https://github.com/vaadin/flow/issues/2152 is closed.
	// when bundling is enabled the Flow's sub-template feature does not work
	@Id("navigation")
	private BakeryNavigation navigation;

	private View childView;

	public interface Model extends TemplateModel {
		void setPage(String page);
	}

	@Override
	protected void onAttach(AttachEvent attachEvent) {
		super.onAttach(attachEvent);

		// TODO(vlukashov, 2017.08.11): Remove this once https://github.com/vaadin/flow/issues/1969 is closed.
		// ensure the app shell bundle is loaded before any others
		UI.getCurrent().getPage().addHtmlImport("frontend://src/app/bakery-app.html");
	}

	@Override
	public void onLocationChange(LocationChangeEvent locationChangeEvent) {
		String path = locationChangeEvent.getLocation().getFirstSegment();
		if (path.isEmpty()) {
			locationChangeEvent.getUI().navigateTo(BakeryConst.PAGE_DEFAULT);
		} else {
			getModel().setPage(path);
		}
	}

	@Override
	public void setChildView(View childView) {
		if (this.childView != null) {
			getElement().removeChild(this.childView.getElement());
		}
		getElement().appendChild(childView.getElement());
		this.childView = childView;
	}
}