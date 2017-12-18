package com.vaadin.starter.bakery.ui.view.login;

import com.vaadin.flow.model.TemplateModel;
import com.vaadin.router.PageTitle;
import com.vaadin.router.Route;
import com.vaadin.router.event.AfterNavigationEvent;
import com.vaadin.router.event.AfterNavigationObserver;
import com.vaadin.server.InitialPageSettings;
import com.vaadin.server.PageConfigurator;
import com.vaadin.shared.ui.Dependency;
import com.vaadin.ui.Tag;
import com.vaadin.ui.common.HtmlImport;
import com.vaadin.ui.polymertemplate.PolymerTemplate;

@Tag("bakery-login")
@HtmlImport("src/login/bakery-login.html")
@Route(value = "login")
@PageTitle("###Bakery###")
public class LoginView extends PolymerTemplate<LoginView.Model> implements PageConfigurator, AfterNavigationObserver {

	@Override
	public void configurePage(InitialPageSettings settings) {
		// Force login page to use Shady DOM to avoid problems with browsers and
		// password managers not supporting shadow DOM
		settings.addInlineWithContents(InitialPageSettings.Position.PREPEND,
				"window.customElements=window.customElements||{};" +
						"window.customElements.forcePolyfill=true;" +
						"window.ShadyDOM={force:true};", Dependency.Type.JAVASCRIPT);
	}

	@Override
	public void afterNavigation(AfterNavigationEvent event) {
		boolean error = event.getLocation().getQueryParameters().getParameters().containsKey("error");
		getModel().setError(error);
	}

	public interface Model extends TemplateModel {
		void setError(boolean error);
	}

}
