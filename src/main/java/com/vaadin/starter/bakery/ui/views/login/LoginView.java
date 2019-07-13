package com.vaadin.starter.bakery.ui.views.login;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.CssImport;
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
@CssImport(value = "./styles/theme-for-vaadin-chart.css", themeFor = "vaadin-chart", include = "vaadin-chart-default-theme")
@CssImport(value = "./styles/theme-for-vaadin-app-layout.css", themeFor = "vaadin-app-layout")
@CssImport(value = "./styles/theme-for-vaadin-crud.css", themeFor = "vaadin-crud")
@CssImport(value = "./styles/theme-for-vaadin-dialog-overlay.css", themeFor = "vaadin-dialog-overlay")
@CssImport(value = "./styles/theme-for-vaadin-grid.css", themeFor = "vaadin-grid")
@CssImport(value = "./styles/theme-for-vaadin-login-overlay-wrapper.css", themeFor = "vaadin-login-overlay-wrapper")
@CssImport(value = "./styles/theme-for-vaadin-text-field.css", themeFor = "vaadin-text-field")
@CssImport(value = "./styles/module-shared-styles.css", id = "shared-styles")
@CssImport(value = "./styles/shared-styles.css")
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
