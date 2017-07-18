package com.vaadin.starter.bakery.ui;

import com.vaadin.annotations.HtmlImport;
import com.vaadin.annotations.JavaScript;
import com.vaadin.annotations.Tag;
import com.vaadin.flow.router.HasChildView;
import com.vaadin.flow.router.LocationChangeEvent;
import com.vaadin.flow.router.View;
import com.vaadin.flow.template.PolymerTemplate;
import com.vaadin.flow.template.model.TemplateModel;
import com.vaadin.hummingbird.ext.spring.annotations.UIScope;
import com.vaadin.starter.bakery.ui.utils.BakeryConst;

@Tag("bakery-app")
@HtmlImport("frontend://src/app/bakery-app.html")
@JavaScript("frontend://resources/service-worker-loader.js")
@UIScope
public class BakeryApp extends PolymerTemplate<BakeryApp.Model> implements HasChildView {

	private View childView;

	public interface Model extends TemplateModel {
		void setPage(String page);
	}

	@Override
	public void onLocationChange(LocationChangeEvent locationChangeEvent) {
		String path = locationChangeEvent.getLocation().getPath();
		getModel().setPage(path);
		if (path.isEmpty()) {
			locationChangeEvent.getUI().navigateTo(BakeryConst.PAGE_DEFAULT);
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