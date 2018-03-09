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

		// Add service worker
		response.getDocument().body().appendElement("script")
				.text("if ('serviceWorker' in navigator) navigator.serviceWorker.register('sw.js')");

		final Element head = response.getDocument().head();

		// manifest needs to be prepended before scripts or it won't be loaded
		head.prepend("<meta name=\"theme-color\" content=\"#227aef\">");
		head.prepend("<link rel=\"manifest\" href=\"manifest.json\">");

		addFavIconTags(head);
		addViewportTag(head);
	}

	private void addFavIconTags(Element head) {
		head.append("<link rel=\"shortcut icon\" href=\"icons/favicon.ico\">");
		head.append("<link rel=\"icon\" sizes=\"192x192\" href=\"icons/icon-192.png\">");
		head.append("<link rel=\"icon\" sizes=\"96x96\" href=\"icons/icon-96.png\">");
		head.append("<link rel=\"apple-touch-icon\" sizes=\"192x192\" href=\"icons/icon-192.png\">");
		head.append("<link rel=\"apple-touch-icon\" sizes=\"96x96\" href=\"icons/icon-96.png\">");
	}

	private void addViewportTag(Element head) {
		String viewport = "width=device-width, minimum-scale=1, initial-scale=1, user-scalable=yes";
		head.append("<meta name=\"viewport\" content=\"" + viewport + "\">");
	}

}
