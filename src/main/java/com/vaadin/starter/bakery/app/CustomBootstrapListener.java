package com.vaadin.starter.bakery.app;

import org.jsoup.nodes.Element;

import com.vaadin.flow.server.BootstrapListener;
import com.vaadin.flow.server.BootstrapPageResponse;

/**
 * Modifies the Vaadin bootstrap page (the HTTP repoponse) in order to
 * <ul>
 *  <li>add links to favicons</li>
 *  <li>add a link to the web app manifest</li>
 *  <li>set the viewport</li>
 *  <li>define the global styles for the main document (initialize the Vaadin Lumo theme)</li>
 * </ul>
 */
public class CustomBootstrapListener implements BootstrapListener {
	@Override
	public void modifyBootstrapPage(BootstrapPageResponse response) {

		// Add service worker if app is in production mode
		if (response.getSession().getService().getDeploymentConfiguration()
				.isProductionMode()) {
			response.getDocument().body().appendElement("script")
					.attr("src", "sw-register.js")
					.attr("async", "true")
					.attr("defer", "true");

		}

		final Element head = response.getDocument().head();

		// manifest needs to be prepended before scripts or it won't be loaded
		head.prepend("<meta name=\"theme-color\" content=\"#227aef\">");
		head.prepend("<link rel=\"manifest\" href=\"manifest.json\">");

		addFavIconTags(head);
		addViewportTag(head);
		injectInlineCustomStyles(head);
	}

	private void addFavIconTags(Element head) {
		head.append("<link rel=\"shortcut icon\" href=\"icons/favicon.ico\">");
		head.append("<link rel=\"icon\" sizes=\"512x512\" href=\"icons/icon-512.png\">");
		head.append("<link rel=\"icon\" sizes=\"192x192\" href=\"icons/icon-192.png\">");
		head.append("<link rel=\"icon\" sizes=\"96x96\" href=\"icons/icon-96.png\">");
		head.append("<link rel=\"apple-touch-icon\" sizes=\"512x512\" href=\"icons/icon-512.png\">");
		head.append("<link rel=\"apple-touch-icon\" sizes=\"192x192\" href=\"icons/icon-192.png\">");
		head.append("<link rel=\"apple-touch-icon\" sizes=\"96x96\" href=\"icons/icon-96.png\">");
	}

	private void addViewportTag(Element head) {
		String viewport = "width=device-width, minimum-scale=1, initial-scale=1, user-scalable=yes";
		head.append("<meta name=\"viewport\" content=\"" + viewport + "\">");
	}

	private void injectInlineCustomStyles(Element head) {
		head.append("<!-- Add any global styles for body, document, etc. -->\n" +
				"    <custom-style>\n" +
				"      <style is=\"custom-style\" include=\"lumo-color lumo-typography\">\n" +
				"        html {\n" +
				"          background: var(--lumo-shade-10pct);\n" +
				"        }\n" +
				"      </style>\n" +
				"    </custom-style>");
	}
}
