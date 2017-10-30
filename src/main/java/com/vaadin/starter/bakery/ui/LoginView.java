package com.vaadin.starter.bakery.ui;

import com.vaadin.flow.model.TemplateModel;
import com.vaadin.router.PageTitle;
import com.vaadin.router.Route;
import com.vaadin.router.event.AfterNavigationEvent;
import com.vaadin.router.event.AfterNavigationObserver;
import com.vaadin.ui.Tag;
import com.vaadin.ui.common.HtmlImport;
import com.vaadin.ui.polymertemplate.PolymerTemplate;

@Tag("bakery-login")
@HtmlImport("src/login/bakery-login.html")
@Route(value = "login", layout = BakeryApp.class)
@PageTitle("###Bakery###")
public class LoginView extends PolymerTemplate<LoginView.Model> implements AfterNavigationObserver {

@Override
public void afterNavigation(AfterNavigationEvent event) {
	boolean error = event.getLocation().getQueryParameters().getParameters().containsKey("error");
	getModel().setError(error);
}

	public interface Model extends TemplateModel {
		void setError(boolean error);
	}

}
