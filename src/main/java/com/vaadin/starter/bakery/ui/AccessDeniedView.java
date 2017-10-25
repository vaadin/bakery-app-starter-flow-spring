package com.vaadin.starter.bakery.ui;

import com.vaadin.flow.model.TemplateModel;
import com.vaadin.router.ErrorParameter;
import com.vaadin.router.HasErrorParameter;
import com.vaadin.router.NavigationHandler;
import com.vaadin.router.Route;
import com.vaadin.router.PageTitle;
import com.vaadin.router.event.BeforeNavigationEvent;
import com.vaadin.router.event.NavigationEvent;
import com.vaadin.starter.bakery.ui.exceptions.AccessDeniedException;
import com.vaadin.starter.bakery.ui.utils.BakeryConst;
import com.vaadin.ui.Tag;
import com.vaadin.ui.common.HtmlImport;
import com.vaadin.ui.polymertemplate.PolymerTemplate;

import javax.servlet.http.HttpServletResponse;

@Tag("access-denied")
@HtmlImport("context://src/admin/access-denied.html")
@Route(value = BakeryConst.ACCESS_DENIED, layout = BakeryApp.class)
@PageTitle(BakeryConst.TITLE_ACCESS_DENIED)
public class AccessDeniedView extends PolymerTemplate<TemplateModel> implements NavigationHandler, HasErrorParameter<AccessDeniedException> {

	@Override
	public int handle(NavigationEvent event) {
		return HttpServletResponse.SC_FORBIDDEN;
	}

	@Override
	public int setErrorParameter(BeforeNavigationEvent beforeNavigationEvent,
			ErrorParameter<AccessDeniedException> errorParameter) {
		return 403;
	}
}
