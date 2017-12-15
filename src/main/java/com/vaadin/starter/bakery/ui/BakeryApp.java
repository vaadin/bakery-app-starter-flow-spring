package com.vaadin.starter.bakery.ui;

import com.vaadin.flow.model.TemplateModel;
import com.vaadin.router.RouterLayout;
import com.vaadin.server.InitialPageSettings;
import com.vaadin.server.PageConfigurator;
import com.vaadin.shared.ui.Dependency;
import com.vaadin.ui.Tag;
import com.vaadin.ui.common.HtmlImport;
import com.vaadin.ui.polymertemplate.PolymerTemplate;

@Tag("bakery-app")
@HtmlImport("src/app/bakery-app.html")
public class BakeryApp extends PolymerTemplate<TemplateModel> implements RouterLayout, PageConfigurator {

	@Override
	public void configurePage(InitialPageSettings settings) {
		if (settings.getAfterNavigationEvent().getLocation().getFirstSegment().equals("login")) {
			// Force login page to use Shady DOM to avoid problems with browsers and
			// password managers not supporting shadow DOM
			settings.addInlineWithContents(InitialPageSettings.Position.PREPEND,
					"window.customElements=window.customElements||{};" +
							"window.customElements.forcePolyfill=true;" +
							"window.ShadyDOM={force:true};", Dependency.Type.JAVASCRIPT);
		}
	}
}