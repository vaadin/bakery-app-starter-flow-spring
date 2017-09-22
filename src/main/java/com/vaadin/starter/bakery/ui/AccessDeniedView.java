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
