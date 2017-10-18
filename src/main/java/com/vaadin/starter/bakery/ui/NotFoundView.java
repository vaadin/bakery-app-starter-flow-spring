package com.vaadin.starter.bakery.ui;

import com.vaadin.flow.dom.ElementFactory;
import com.vaadin.flow.router.LocationChangeEvent;
import com.vaadin.flow.router.View;
import com.vaadin.hummingbird.ext.spring.annotations.ParentView;
import com.vaadin.hummingbird.ext.spring.annotations.Route;
import com.vaadin.router.RouterLink;
import com.vaadin.router.Title;
import com.vaadin.starter.bakery.ui.utils.BakeryConst;
import com.vaadin.ui.Component;
import com.vaadin.ui.Composite;
import com.vaadin.ui.Text;
import com.vaadin.ui.html.Div;

import javax.servlet.http.HttpServletResponse;

@Route(value = BakeryConst.PAGE_NOTFOUND)
@Title(BakeryConst.TITLE_NOT_FOUND)
@ParentView(BakeryApp.class)
public class NotFoundView extends Composite<Div> implements View {

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
	public void onLocationChange(LocationChangeEvent locationChangeEvent) {
		locationChangeEvent.setStatusCode(HttpServletResponse.SC_NOT_FOUND);
	}
}
