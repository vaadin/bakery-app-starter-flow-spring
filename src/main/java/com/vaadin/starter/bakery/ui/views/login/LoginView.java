package com.vaadin.starter.bakery.ui.views.login;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.starter.bakery.app.security.SecurityUtils;
import com.vaadin.starter.bakery.ui.utils.BakeryConst;
import com.vaadin.starter.bakery.ui.views.storefront.StorefrontView;

@Route
@PageTitle("###Bakery###")
@JsModule("./styles/shared-styles.js")
@Viewport(BakeryConst.VIEWPORT)
public class LoginView extends LoginOverlay
	implements AfterNavigationObserver, BeforeEnterObserver {

	public LoginView() {
		LoginI18n i18n = LoginI18n.createDefault();
		i18n.setHeader(new LoginI18n.Header());
		i18n.getHeader().setTitle("###Bakery###");
		i18n.getHeader().setDescription(
			"admin@vaadin.com + admin\n" + "barista@vaadin.com + barista");
		i18n.setAdditionalInformation(null);
		i18n.setForm(new LoginI18n.Form());
		i18n.getForm().setSubmit("Sign in");
		i18n.getForm().setTitle("Sign in");
		i18n.getForm().setUsername("Email");
		i18n.getForm().setPassword("Password");
		setI18n(i18n);
		setForgotPasswordButtonVisible(false);
		setAction("login");
		setOpened(true);
	}

	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		if (SecurityUtils.isUserLoggedIn()) {
			// Needed manually to change the URL because of https://github.com/vaadin/flow/issues/4189
			UI.getCurrent().getPage().getHistory().replaceState(null, "");
			event.rerouteTo(StorefrontView.class);
		}
	}

	@Override
	public void afterNavigation(AfterNavigationEvent event) {
		setError(
			event.getLocation().getQueryParameters().getParameters().containsKey(
				"error"));
	}

}
