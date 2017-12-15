package com.vaadin.starter.bakery.ui;

import com.vaadin.flow.model.TemplateModel;
import com.vaadin.router.RouterLayout;
import com.vaadin.server.InitialPageSettings;
import com.vaadin.server.PageConfigurator;
import com.vaadin.shared.ui.Dependency;
import com.vaadin.ui.Tag;
import com.vaadin.ui.common.HtmlImport;
import com.vaadin.ui.polymertemplate.PolymerTemplate;

import java.util.HashMap;
import java.util.Map;

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

		settings.setViewport("width=device-width, minimum-scale=1, initial-scale=1, user-scalable=yes");

		// manifest needs to be prepended before scripts or it won't be loaded
		settings.addMetaTag(InitialPageSettings.Position.PREPEND, "theme-color", "#227aef");
		Map<String, String> attributes = new HashMap<>();
		attributes.put("rel", "manifest");
		settings.addLink(InitialPageSettings.Position.PREPEND, "/manifest.json", attributes);

		// add favicons
		attributes.clear();
		attributes.put("rel", "shortcut icon");
		settings.addLink(InitialPageSettings.Position.PREPEND, "icons/favicon.ico", attributes);

		attributes.clear();
		attributes.put("rel", "icon");
		attributes.put("sizes", "192x192");
		settings.addLink(InitialPageSettings.Position.PREPEND, "icons/icon-192.png", attributes);

		attributes.clear();
		attributes.put("rel", "icon");
		attributes.put("sizes", "96x96");
		settings.addLink(InitialPageSettings.Position.PREPEND, "icons/icon-96.png", attributes);

		attributes.clear();
		attributes.put("rel", "apple-touch-icon");
		attributes.put("sizes", "512x512");
		settings.addLink(InitialPageSettings.Position.PREPEND, "icons/icon-512.png", attributes);

		attributes.clear();
		attributes.put("rel", "apple-touch-icon");
		attributes.put("sizes", "192x192");
		settings.addLink(InitialPageSettings.Position.PREPEND, "icons/icon-192.png", attributes);

		attributes.clear();
		attributes.put("rel", "apple-touch-icon");
		attributes.put("sizes", "96x96");
		settings.addLink(InitialPageSettings.Position.PREPEND, "icons/icon-96.png", attributes);

		// add the Valo theme to the main document
		settings.addInlineWithContents(
				"<custom-style>" +
							"<style is=\"custom-style\" include=\"valo-color valo-typography\"></style>" +
						"</custom-style>",
				Dependency.Type.HTML_IMPORT);
	}
}