package com.vaadin.flow.demo.patientportal.ui;

import com.vaadin.annotations.HtmlImport;
import com.vaadin.annotations.Tag;
import com.vaadin.flow.demo.patientportal.ui.utils.BakeryConst;
import com.vaadin.flow.router.HasChildView;
import com.vaadin.flow.router.LocationChangeEvent;
import com.vaadin.flow.router.View;
import com.vaadin.flow.template.PolymerTemplate;
import com.vaadin.flow.template.model.TemplateModel;
import com.vaadin.hummingbird.ext.spring.annotations.UIScope;

/**
 * Created by viktor on 03/07/2017.
 */
@Tag("bakery-app")
@HtmlImport("frontend://src/app/bakery-app.html")
@UIScope
public class BakeryApp extends PolymerTemplate<BakeryApp.Model> implements HasChildView {

	View childView;

	public interface Model extends TemplateModel {
		void setPage(String page);
	}

	@Override
	public void onLocationChange(LocationChangeEvent locationChangeEvent) {
		getModel().setPage(locationChangeEvent.getLocation().getPath());
		if (!"".equals(locationChangeEvent.getLocation().getPath()))
			return;

		locationChangeEvent.getUI().navigateTo(BakeryConst.PAGE_DEFAULT);
	}

	@Override
	public void setChildView(View childView) {
		if (this.childView != null)
			getElement().removeChild(this.childView.getElement());

		getElement().appendChild(childView.getElement());
		this.childView = childView;
	}
}