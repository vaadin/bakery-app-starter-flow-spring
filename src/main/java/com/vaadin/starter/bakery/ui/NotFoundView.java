package com.vaadin.starter.bakery.ui;

import com.vaadin.flow.model.TemplateModel;
import com.vaadin.flow.router.LocationChangeEvent;
import com.vaadin.flow.router.View;
import com.vaadin.hummingbird.ext.spring.annotations.ParentView;
import com.vaadin.hummingbird.ext.spring.annotations.Route;
import com.vaadin.starter.bakery.ui.utils.BakeryConst;
import com.vaadin.ui.Tag;
import com.vaadin.ui.common.HtmlImport;
import com.vaadin.ui.polymertemplate.PolymerTemplate;

import javax.servlet.http.HttpServletResponse;

@Tag("bakery-404")
@HtmlImport("context://src/404/bakery-404.html")
@Route(value = BakeryConst.PAGE_NOTFOUND)
@ParentView(BakeryApp.class)
public class NotFoundView extends PolymerTemplate<TemplateModel> implements View {
	@Override
	public void onLocationChange(LocationChangeEvent locationChangeEvent) {
		locationChangeEvent.setStatusCode(HttpServletResponse.SC_NOT_FOUND);
	}
}
