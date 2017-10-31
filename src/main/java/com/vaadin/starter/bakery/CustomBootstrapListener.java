package com.vaadin.starter.bakery;

import com.vaadin.server.BootstrapListener;
import com.vaadin.server.BootstrapPageResponse;
import org.jsoup.nodes.Element;

public class CustomBootstrapListener implements BootstrapListener {
	public void modifyBootstrapPage(BootstrapPageResponse response) {
		final Element head = response.getDocument().head();
		if ("/login".equals(response.getRequest().getPathInfo())) {
			// Force login page to use Shady DOM to avoid problems with browsers and
			// password managers not supporting shadow DOM
			head.prepend(
					"<script type='text/javascript'>window.customElements.forcePolyfill=true;window.ShadyDOM = {force:true};</script>");
		}
		addFavIconTags(head);
		injectInlineCustomStyles(head);
	}

	private void addFavIconTags(Element head) {
		head.append("<link rel=\"shortcut icon\" href=\"icons/favicon.ico\">");
		head.append("<link rel=\"icon\" sizes=\"192x192\" href=\"icons/icon-192.png\">");
		head.append("<link rel=\"icon\" sizes=\"96x96\" href=\"icons/icon-96.png\">");
		head.append("<link rel=\"apple-touch-icon\" sizes=\"192x192\" href=\"icons/icon-192.png\">");
		head.append("<link rel=\"apple-touch-icon\" sizes=\"96x96\" href=\"icons/icon-96.png\">");
	}

	private void injectInlineCustomStyles(Element head) {
		head.append(
				"<meta name=\"viewport\" content=\"width=device-width, minimum-scale=1, initial-scale=1, user-scalable=yes\">");
		head.append("<!-- Add any global styles for body, document, etc. -->\n" +
                "    <custom-style>\n" +
                "      <style is=\"custom-style\" include=\"valo-colors valo-typography\">\n" +
                "        html,\n" +
                "        body {\n" +
                "          height: 100%;\n" +
                "        }\n" +
                "\n" +
                "        body {\n" +
                "          margin: 0;\n" +
                "          background: var(--valo-shade-10pct);\n" +
                "        }\n" +
                "\n" +
                "        bakery-app {\n" +
                "          height: 100%;\n" +
                "        }\n" +
                "      </style>\n" +
                "    </custom-style>");
	}
}
