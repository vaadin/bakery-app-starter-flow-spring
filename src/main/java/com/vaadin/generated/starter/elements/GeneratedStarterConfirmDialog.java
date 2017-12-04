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
import elemental.json.JsonObject;
import com.vaadin.ui.event.Synchronize;
import com.vaadin.ui.event.DomEvent;
import com.vaadin.ui.event.ComponentEvent;
import com.vaadin.ui.event.ComponentEventListener;
import com.vaadin.shared.Registration;

@Generated({ "Generator: com.vaadin.generator.ComponentGenerator#1.0-SNAPSHOT",
        "WebComponent: ConfirmDialogElement#null", "Flow#1.0-SNAPSHOT" })
@Tag("starter-confirm-dialog")
@HtmlImport("frontend://bower_components/starter-elements/starter-confirm-dialog.html")
public class GeneratedStarterConfirmDialog<R extends GeneratedStarterConfirmDialog<R>>
        extends Component implements HasStyle, ComponentSupplier<R> {

    /**
     * This property is not synchronized automatically from the client side, so
     * the returned value may not be the same as in client side.
     * 
     * @return the {@code caption} property from the webcomponent
     */
    public String getCaption() {
        return getElement().getProperty("caption");
    }

    /**
     * @param caption
     *            the String value to set
     */
    public void setCaption(String caption) {
        getElement().setProperty("caption", caption == null ? "" : caption);
    }

    /**
     * This property is not synchronized automatically from the client side, so
     * the returned value may not be the same as in client side.
     * 
     * @return the {@code message} property from the webcomponent
     */
    public String getMessage() {
        return getElement().getProperty("message");
    }

    /**
     * @param message
     *            the String value to set
     */
    public void setMessage(String message) {
        getElement().setProperty("message", message == null ? "" : message);
    }

    /**
     * This property is not synchronized automatically from the client side, so
     * the returned value may not be the same as in client side.
     * 
     * @return the {@code okText} property from the webcomponent
     */
    public String getOkText() {
        return getElement().getProperty("okText");
    }

    /**
     * @param okText
     *            the String value to set
     */
    public void setOkText(String okText) {
        getElement().setProperty("okText", okText == null ? "" : okText);
    }

    /**
     * This property is not synchronized automatically from the client side, so
     * the returned value may not be the same as in client side.
     * 
     * @return the {@code cancelText} property from the webcomponent
     */
    public String getCancelText() {
        return getElement().getProperty("cancelText");
    }

    /**
     * @param cancelText
     *            the String value to set
     */
    public void setCancelText(String cancelText) {
        getElement().setProperty("cancelText",
                cancelText == null ? "" : cancelText);
    }

    /**
     * This property is not synchronized automatically from the client side, so
     * the returned value may not be the same as in client side.
     * 
     * @return the {@code callback} property from the webcomponent
     */
    protected JsonObject protectedGetCallback() {
        return (JsonObject) getElement().getPropertyRaw("callback");
    }

    /**
     * @param callback
     *            the JsonObject value to set
     */
    protected void setCallback(JsonObject callback) {
        getElement().setPropertyJson("callback", callback);
    }

    /**
     * This property is synchronized automatically from client side when a
     * 'opened-changed' event happens.
     * 
     * @return the {@code opened} property from the webcomponent
     */
    @Synchronize(property = "opened", value = "opened-changed")
    public boolean isOpened() {
        return getElement().getProperty("opened", false);
    }

    /**
     * @param opened
     *            the boolean value to set
     */
    public void setOpened(boolean opened) {
        getElement().setProperty("opened", opened);
    }

    @DomEvent("cancel-click")
    public static class CancelClickEvent<R extends GeneratedStarterConfirmDialog<R>>
            extends ComponentEvent<R> {
        public CancelClickEvent(R source, boolean fromClient) {
            super(source, fromClient);
        }
    }

    /**
     * Adds a listener for {@code cancel-click} events fired by the
     * webcomponent.
     * 
     * @param listener
     *            the listener
     * @return a {@link Registration} for removing the event listener
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Registration addCancelClickListener(
            ComponentEventListener<CancelClickEvent<R>> listener) {
        return addListener(CancelClickEvent.class,
                (ComponentEventListener) listener);
    }

    @DomEvent("ok-click")
    public static class OkClickEvent<R extends GeneratedStarterConfirmDialog<R>>
            extends ComponentEvent<R> {
        public OkClickEvent(R source, boolean fromClient) {
            super(source, fromClient);
        }
    }

    /**
     * Adds a listener for {@code ok-click} events fired by the webcomponent.
     * 
     * @param listener
     *            the listener
     * @return a {@link Registration} for removing the event listener
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Registration addOkClickListener(
            ComponentEventListener<OkClickEvent<R>> listener) {
        return addListener(OkClickEvent.class,
                (ComponentEventListener) listener);
    }

    @DomEvent("opened-changed")
    public static class OpenedChangeEvent<R extends GeneratedStarterConfirmDialog<R>>
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