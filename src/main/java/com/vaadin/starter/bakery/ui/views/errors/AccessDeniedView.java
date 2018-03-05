package com.vaadin.starter.bakery.ui.views.errors;

import javax.servlet.http.HttpServletResponse;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.ErrorParameter;
import com.vaadin.flow.router.HasErrorParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.ParentLayout;
import com.vaadin.flow.templatemodel.TemplateModel;
import com.vaadin.starter.bakery.ui.MainView;
import com.vaadin.starter.bakery.ui.exceptions.AccessDeniedException;
import com.vaadin.starter.bakery.ui.utils.BakeryConst;

@Tag("access-denied-view")
@HtmlImport("src/views/errors/access-denied-view.html")
@ParentLayout(MainView.class)
@PageTitle(BakeryConst.TITLE_ACCESS_DENIED)
public class AccessDeniedView extends PolymerTemplate<TemplateModel> implements HasErrorParameter<AccessDeniedException> {

	@Override
	public int setErrorParameter(BeforeEnterEvent beforeEnterEvent, ErrorParameter<AccessDeniedException> errorParameter) {
		return HttpServletResponse.SC_FORBIDDEN;
	}
}
