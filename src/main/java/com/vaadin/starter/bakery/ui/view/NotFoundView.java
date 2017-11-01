package com.vaadin.starter.bakery.ui.view;

import com.vaadin.flow.dom.ElementFactory;
import com.vaadin.router.NavigationHandler;
import com.vaadin.router.PageTitle;
import com.vaadin.router.Route;
import com.vaadin.router.RouterLink;
import com.vaadin.router.event.NavigationEvent;
import com.vaadin.starter.bakery.ui.BakeryApp;
import com.vaadin.starter.bakery.ui.utils.BakeryConst;
import com.vaadin.ui.Component;
import com.vaadin.ui.Composite;
import com.vaadin.ui.Text;
import com.vaadin.ui.html.Div;

import javax.servlet.http.HttpServletResponse;

@Route(value = BakeryConst.PAGE_NOTFOUND, layout = BakeryApp.class)
@PageTitle(BakeryConst.TITLE_NOT_FOUND)
public class NotFoundView extends Composite<Div> implements NavigationHandler {

	public NotFoundView() {
		// This should be simply `new RouterLink("Go to the front page.", StorefrontView.class)`
		// but due to the Flow issue https://github.com/vaadin/flow/issues/1476, there needs to be a workaround.
		// The workaround can be removed after upgrading to the official flow-spring plugin (see BFF-335)
		RouterLink link = Component.from(
				ElementFactory.createRouterLink("", "Go to the front page."),
				RouterLink.class);
		getContent().add(new Text("Oops you hit a 404. "), link);
	}

	@Override
	public int handle(NavigationEvent event) {
		return HttpServletResponse.SC_NOT_FOUND;
	}
}
