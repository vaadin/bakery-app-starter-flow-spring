package com.vaadin.starter.bakery;

import com.vaadin.external.jsoup.nodes.Element;
import com.vaadin.server.BootstrapListener;
import com.vaadin.server.BootstrapPageResponse;

public class CustomBootstrapListener implements BootstrapListener {
    public void modifyBootstrapPage(BootstrapPageResponse response) {
        injectInlineCustomStyles(response);
    }

    private void injectInlineCustomStyles(BootstrapPageResponse response) {
        final Element head = response.getDocument().head();
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
