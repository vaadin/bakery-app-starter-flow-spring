package com.vaadin.starter.bakery.ui;

import com.vaadin.annotations.HtmlImport;
import com.vaadin.annotations.Tag;
import com.vaadin.annotations.Title;
import com.vaadin.flow.router.LocationChangeEvent;
import com.vaadin.flow.router.View;
import com.vaadin.flow.template.PolymerTemplate;
import com.vaadin.flow.template.model.TemplateModel;
import com.vaadin.hummingbird.ext.spring.annotations.ParentView;
import com.vaadin.hummingbird.ext.spring.annotations.Route;
import com.vaadin.starter.bakery.ui.utils.BakeryConst;

import javax.servlet.http.HttpServletResponse;

import static com.vaadin.starter.bakery.ui.utils.BakeryConst.TITLE_NOT_FOUND;

@Tag("bakery-404")
@HtmlImport("context://src/404/bakery-404.html")
@Route(value = BakeryConst.PAGE_NOTFOUND)
@Title(TITLE_NOT_FOUND)
@ParentView(BakeryApp.class)
public class NotFoundView extends PolymerTemplate<TemplateModel> implements View {
	@Override
	public void onLocationChange(LocationChangeEvent locationChangeEvent) {
		locationChangeEvent.setStatusCode(HttpServletResponse.SC_NOT_FOUND);
	}
}
