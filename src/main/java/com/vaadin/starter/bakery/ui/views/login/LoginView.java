package com.vaadin.starter.bakery.ui.views.login;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.InitialPageSettings;
import com.vaadin.flow.server.PageConfigurator;
import com.vaadin.flow.templatemodel.TemplateModel;
import com.vaadin.starter.bakery.app.security.SecurityUtils;
import com.vaadin.starter.bakery.ui.components.BakeryCookieConsent;
import com.vaadin.starter.bakery.ui.utils.BakeryConst;
import com.vaadin.starter.bakery.ui.views.storefront.StorefrontView;

@Route
@PageTitle("###Bakery###")
@HtmlImport("styles/shared-styles.html")
@Viewport(BakeryConst.VIEWPORT)
public class LoginView extends VerticalLayout implements PageConfigurator, AfterNavigationObserver, BeforeEnterObserver {

	private LoginOverlay login = new LoginOverlay();

	public LoginView() {
		getElement().appendChild(new BakeryCookieConsent().getElement(), login.getElement());

		LoginI18n i18n = LoginI18n.createDefault();
		i18n.setHeader(new LoginI18n.Header());
		i18n.getHeader().setTitle("###Bakery###");
		i18n.getHeader().setDescription(
				"admin@vaadin.com + admin\n" +
				"barista@vaadin.com + barista");
		i18n.setAdditionalInformation(null);
		i18n.setForm(new LoginI18n.Form());
		i18n.getForm().setSubmit("Sign in");
		i18n.getForm().setTitle("Sign in");
		i18n.getForm().setUsername("Email");
		i18n.getForm().setPassword("Password");
		login.setI18n(i18n);
		login.getElement().setAttribute("hide-forgot-password-button", true);
		login.setAction("login");
		login.setOpened(true);
	}

	@Override
	public void configurePage(InitialPageSettings settings) {
		// Force login page to use Shady DOM to avoid problems with browsers and
		// password managers not supporting shadow DOM
		settings.addInlineWithContents(InitialPageSettings.Position.PREPEND,
				"if(window.customElements) {" +
						"window.customElements.forcePolyfill=true;" +
						"window.ShadyDOM={force:true}};", InitialPageSettings.WrapMode.JAVASCRIPT);
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
		login.setError(event.getLocation().getQueryParameters().getParameters().containsKey("error"));
	}

	public interface Model extends TemplateModel {
		void setError(boolean error);
	}

}
