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

@Tag("bakery-login")
@HtmlImport("context://src/login/bakery-login.html")
@Route("login")
@Title("###Bakery###")
@ParentView(BakeryApp.class)
public class LoginView extends PolymerTemplate<LoginView.Model> implements View {

	@Override
	public void onLocationChange(LocationChangeEvent locationChangeEvent) {
		boolean error = locationChangeEvent.getQueryParameters().containsKey("error");
		getModel().setError(error);
	}

	public interface Model extends TemplateModel {
		void setError(boolean error);
	}

}
