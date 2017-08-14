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

@Tag("user-login")
@HtmlImport("frontend://src/login/user-login.html")
@Route("login")
//@ParentView(BakeryApp.class)
public class LoginView extends PolymerTemplate<TemplateModel> implements View{

	@Override
	public void onLocationChange(LocationChangeEvent locationChangeEvent) {
		boolean error = locationChangeEvent.getQueryParameters().containsKey("error");
		getElement().setProperty("error", error);
	}

	@Override
	public String getTitle(LocationChangeEvent locationChangeEvent) {

		return "###Bakery###";
	}

}
