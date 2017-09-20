package com.vaadin.starter.bakery.ui;

import com.vaadin.annotations.HtmlImport;
import com.vaadin.annotations.Tag;
import com.vaadin.flow.router.LocationChangeEvent;
import com.vaadin.flow.router.View;
import com.vaadin.flow.template.PolymerTemplate;
import com.vaadin.flow.template.model.TemplateModel;
import com.vaadin.hummingbird.ext.spring.annotations.ParentView;
import com.vaadin.hummingbird.ext.spring.annotations.Route;
import com.vaadin.starter.bakery.ui.utils.BakeryConst;

import javax.servlet.http.HttpServletResponse;

@Tag("access-denied")
@HtmlImport("context://src/admin/access-denied.html")
@ParentView(BakeryApp.class)
@Route(BakeryConst.ACCESS_DENIED)
public class AccessDeniedView extends PolymerTemplate<TemplateModel> implements View {
	@Override
	public void onLocationChange(LocationChangeEvent locationChangeEvent) {
		locationChangeEvent.setStatusCode(HttpServletResponse.SC_FORBIDDEN);
	}
}
