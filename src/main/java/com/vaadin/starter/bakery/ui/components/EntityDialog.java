package com.vaadin.starter.bakery.ui.components;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dialog.Dialog;

public class EntityDialog extends Dialog {

    public void setThemeOnAttach(String theme) {
        // There is no way for theming the dialog overlay but using private API
        addAttachListener(e ->
                UI.getCurrent().getPage().executeJavaScript("$0.$.overlay.setAttribute('theme', $1);",
                        getElement(), theme));
    }
}
