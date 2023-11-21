package com.vaadin.starter.bakery.ui.views.errors;

import jakarta.servlet.http.HttpServletResponse;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.dom.ElementFactory;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.ErrorParameter;
import com.vaadin.flow.router.AccessDeniedException;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.ParentLayout;
import com.vaadin.flow.router.RouteAccessDeniedError;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.starter.bakery.ui.MainView;
import com.vaadin.starter.bakery.ui.utils.BakeryConst;

@ParentLayout(MainView.class)
@PageTitle(BakeryConst.TITLE_NOT_FOUND)
public class CustomRouteNotFoundError extends RouteAccessDeniedError {

	public CustomRouteNotFoundError() {
		RouterLink link = Component.from(
				ElementFactory.createRouterLink("", "Go to the front page."),
				RouterLink.class);
		getElement().appendChild(new Text("Oops you hit a 404. ").getElement(), link.getElement());
	}

	@Override
	public int setErrorParameter(BeforeEnterEvent event, ErrorParameter<AccessDeniedException> parameter) {
		return HttpServletResponse.SC_NOT_FOUND;
	}
}
