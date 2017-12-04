/*
 * Copyright 2000-2017 Vaadin Ltd.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.vaadin.generated.starter.elements;

import com.vaadin.ui.Component;
import com.vaadin.ui.common.HasStyle;
import com.vaadin.ui.common.ComponentSupplier;
import javax.annotation.Generated;
import com.vaadin.ui.Tag;
import com.vaadin.ui.common.HtmlImport;
import com.vaadin.ui.event.Synchronize;
import elemental.json.JsonObject;
import com.vaadin.ui.event.DomEvent;
import com.vaadin.ui.event.ComponentEvent;
import com.vaadin.ui.event.ComponentEventListener;
import com.vaadin.shared.Registration;

@Generated({ "Generator: com.vaadin.generator.ComponentGenerator#1.0-SNAPSHOT",
        "WebComponent: StarterDialog#null", "Flow#1.0-SNAPSHOT" })
@Tag("starter-dialog")
@HtmlImport("frontend://bower_components/starter-elements/starter-dialog.html")
public class GeneratedStarterDialog<R extends GeneratedStarterDialog<R>>
        extends Component implements HasStyle, ComponentSupplier<R> {

    /**
     * <p>
     * Description copied from corresponding location in WebComponent:
     * </p>
     * <p>
     * True if the overlay is currently displayed.
     * <p>
     * This property is synchronized automatically from client side when a
     * 'opened-changed' event happens.
     * </p>
     * 
     * @return the {@code opened} property from the webcomponent
     */
    @Synchronize(property = "opened", value = "opened-changed")
    public boolean isOpened() {
        return getElement().getProperty("opened", false);
    }

    /**
     * <p>
     * Description copied from corresponding location in WebComponent:
     * </p>
     * <p>
     * True if the overlay is currently displayed.
     * </p>
     * 
     * @param opened
     *            the boolean value to set
     */
    public void setOpened(boolean opened) {
        getElement().setProperty("opened", opened);
    }

    /**
     * <p>
     * Description copied from corresponding location in WebComponent:
     * </p>
     * <p>
     * Set to true to disable closing dialog on outside click
     * <p>
     * This property is not synchronized automatically from the client side, so
     * the returned value may not be the same as in client side.
     * </p>
     * 
     * @return the {@code noCloseOnOutsideClick} property from the webcomponent
     */
    public boolean isNoCloseOnOutsideClick() {
        return getElement().getProperty("noCloseOnOutsideClick", false);
    }

    /**
     * <p>
     * Description copied from corresponding location in WebComponent:
     * </p>
     * <p>
     * Set to true to disable closing dialog on outside click
     * </p>
     * 
     * @param noCloseOnOutsideClick
     *            the boolean value to set
     */
    public void setNoCloseOnOutsideClick(boolean noCloseOnOutsideClick) {
        getElement().setProperty("noCloseOnOutsideClick",
                noCloseOnOutsideClick);
    }

    /**
     * <p>
     * Description copied from corresponding location in WebComponent:
     * </p>
     * <p>
     * Set to true to disable closing dialog on Escape press
     * <p>
     * This property is not synchronized automatically from the client side, so
     * the returned value may not be the same as in client side.
     * </p>
     * 
     * @return the {@code noCloseOnEsc} property from the webcomponent
     */
    public boolean isNoCloseOnEsc() {
        return getElement().getProperty("noCloseOnEsc", false);
    }

    /**
     * <p>
     * Description copied from corresponding location in WebComponent:
     * </p>
     * <p>
     * Set to true to disable closing dialog on Escape press
     * </p>
     * 
     * @param noCloseOnEsc
     *            the boolean value to set
     */
    public void setNoCloseOnEsc(boolean noCloseOnEsc) {
        getElement().setProperty("noCloseOnEsc", noCloseOnEsc);
    }

    /**
     * This property is not synchronized automatically from the client side, so
     * the returned value may not be the same as in client side.
     * 
     * @return the {@code theme} property from the webcomponent
     */
    public String getTheme() {
        return getElement().getProperty("theme");
    }

    /**
     * @param theme
     *            the String value to set
     */
    public void setTheme(String theme) {
        getElement().setProperty("theme", theme == null ? "" : theme);
    }

    /**
     * <p>
     * Description copied from corresponding location in WebComponent:
     * </p>
     * <p>
     * FIXME: set focusTrap to true when
     * https://github.com/vaadin/vaadin-overlay/pull/46 merged
     * <p>
     * This property is not synchronized automatically from the client side, so
     * the returned value may not be the same as in client side.
     * </p>
     * 
     * @return the {@code focusTrap} property from the webcomponent
     */
    protected JsonObject protectedGetFocusTrap() {
        return (JsonObject) getElement().getPropertyRaw("focusTrap");
    }

    /**
     * <p>
     * Description copied from corresponding location in WebComponent:
     * </p>
     * <p>
     * FIXME: set focusTrap to true when
     * https://github.com/vaadin/vaadin-overlay/pull/46 merged
     * </p>
     * 
     * @param focusTrap
     *            the JsonObject value to set
     */
    protected void setFocusTrap(JsonObject focusTrap) {
        getElement().setPropertyJson("focusTrap", focusTrap);
    }

    @DomEvent("opened-changed")
    public static class OpenedChangeEvent<R extends GeneratedStarterDialog<R>>
            extends ComponentEvent<R> {
        public OpenedChangeEvent(R source, boolean fromClient) {
            super(source, fromClient);
        }
    }

    /**
     * Adds a listener for {@code opened-changed} events fired by the
     * webcomponent.
     * 
     * @param listener
     *            the listener
     * @return a {@link Registration} for removing the event listener
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Registration addOpenedChangeListener(
            ComponentEventListener<OpenedChangeEvent<R>> listener) {
        return addListener(OpenedChangeEvent.class,
                (ComponentEventListener) listener);
    }
}