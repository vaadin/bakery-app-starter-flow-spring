package com.vaadin.starter.bakery.ui.views.errors;

import javax.servlet.http.HttpServletResponse;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.dom.ElementFactory;
import com.vaadin.flow.router.*;
import com.vaadin.starter.bakery.ui.MainView;
import com.vaadin.starter.bakery.ui.exceptions.AccessDeniedException;
import com.vaadin.starter.bakery.ui.utils.BakeryConst;

@Tag("access-denied-view")
@ParentLayout(MainView.class)
@PageTitle(BakeryConst.TITLE_ACCESS_DENIED)
public class AccessDeniedView extends Component implements HasErrorParameter<AccessDeniedException> {

	public AccessDeniedView() {
		getElement().appendChild(new H1("Access to this page is denied").getElement(),
				ElementFactory.createRouterLink("", "Go to the front page."));
	}

	@Override
	public int setErrorParameter(BeforeEnterEvent beforeEnterEvent, ErrorParameter<AccessDeniedException> errorParameter) {
		return HttpServletResponse.SC_FORBIDDEN;
	}
}
