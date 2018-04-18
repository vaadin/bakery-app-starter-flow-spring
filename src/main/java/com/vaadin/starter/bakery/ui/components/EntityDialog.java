package com.vaadin.starter.bakery.ui.components;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dialog.Dialog;

public class EntityDialog extends Dialog {

    public void setThemeOnAttach(String theme) {
        addAttachListener(e ->
                UI.getCurrent().getPage().executeJavaScript("$0.$.overlay.setAttribute('theme', $1);",
                        getElement(), theme));
    }
}
