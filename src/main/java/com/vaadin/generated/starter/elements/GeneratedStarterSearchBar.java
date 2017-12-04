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
import com.vaadin.ui.event.DomEvent;
import com.vaadin.ui.event.ComponentEvent;
import com.vaadin.ui.event.ComponentEventListener;
import com.vaadin.shared.Registration;

@Generated({ "Generator: com.vaadin.generator.ComponentGenerator#1.0-SNAPSHOT",
        "WebComponent: SearchBarElement#null", "Flow#1.0-SNAPSHOT" })
@Tag("starter-search-bar")
@HtmlImport("frontend://bower_components/starter-elements/starter-search-bar.html")
public class GeneratedStarterSearchBar<R extends GeneratedStarterSearchBar<R>>
        extends Component implements HasStyle, ComponentSupplier<R> {

    /**
     * This property is not synchronized automatically from the client side, so
     * the returned value may not be the same as in client side.
     * 
     * @return the {@code fieldPlaceholder} property from the webcomponent
     */
    public String getFieldPlaceholder() {
        return getElement().getProperty("fieldPlaceholder");
    }

    /**
     * @param fieldPlaceholder
     *            the String value to set
     */
    public void setFieldPlaceholder(String fieldPlaceholder) {
        getElement().setProperty("fieldPlaceholder",
                fieldPlaceholder == null ? "" : fieldPlaceholder);
    }

    /**
     * This property is not synchronized automatically from the client side, so
     * the returned value may not be the same as in client side.
     * 
     * @return the {@code fieldValue} property from the webcomponent
     */
    public String getFieldValue() {
        return getElement().getProperty("fieldValue");
    }

    /**
     * @param fieldValue
     *            the String value to set
     */
    public void setFieldValue(String fieldValue) {
        getElement().setProperty("fieldValue",
                fieldValue == null ? "" : fieldValue);
    }

    /**
     * This property is not synchronized automatically from the client side, so
     * the returned value may not be the same as in client side.
     * 
     * @return the {@code fieldIcon} property from the webcomponent
     */
    public String getFieldIcon() {
        return getElement().getProperty("fieldIcon");
    }

    /**
     * @param fieldIcon
     *            the String value to set
     */
    public void setFieldIcon(String fieldIcon) {
        getElement().setProperty("fieldIcon",
                fieldIcon == null ? "" : fieldIcon);
    }

    /**
     * This property is not synchronized automatically from the client side, so
     * the returned value may not be the same as in client side.
     * 
     * @return the {@code buttonIcon} property from the webcomponent
     */
    public String getButtonIcon() {
        return getElement().getProperty("buttonIcon");
    }

    /**
     * @param buttonIcon
     *            the String value to set
     */
    public void setButtonIcon(String buttonIcon) {
        getElement().setProperty("buttonIcon",
                buttonIcon == null ? "" : buttonIcon);
    }

    /**
     * This property is not synchronized automatically from the client side, so
     * the returned value may not be the same as in client side.
     * 
     * @return the {@code buttonText} property from the webcomponent
     */
    public String getButtonText() {
        return getElement().getProperty("buttonText");
    }

    /**
     * @param buttonText
     *            the String value to set
     */
    public void setButtonText(String buttonText) {
        getElement().setProperty("buttonText",
                buttonText == null ? "" : buttonText);
    }

    /**
     * This property is not synchronized automatically from the client side, so
     * the returned value may not be the same as in client side.
     * 
     * @return the {@code showCheckbox} property from the webcomponent
     */
    public boolean isShowCheckbox() {
        return getElement().getProperty("showCheckbox", false);
    }

    /**
     * @param showCheckbox
     *            the boolean value to set
     */
    public void setShowCheckbox(boolean showCheckbox) {
        getElement().setProperty("showCheckbox", showCheckbox);
    }

    /**
     * This property is not synchronized automatically from the client side, so
     * the returned value may not be the same as in client side.
     * 
     * @return the {@code checkboxText} property from the webcomponent
     */
    public String getCheckboxText() {
        return getElement().getProperty("checkboxText");
    }

    /**
     * @param checkboxText
     *            the String value to set
     */
    public void setCheckboxText(String checkboxText) {
        getElement().setProperty("checkboxText",
                checkboxText == null ? "" : checkboxText);
    }

    /**
     * This property is not synchronized automatically from the client side, so
     * the returned value may not be the same as in client side.
     * 
     * @return the {@code checkboxChecked} property from the webcomponent
     */
    public boolean isCheckboxChecked() {
        return getElement().getProperty("checkboxChecked", false);
    }

    /**
     * @param checkboxChecked
     *            the boolean value to set
     */
    public void setCheckboxChecked(boolean checkboxChecked) {
        getElement().setProperty("checkboxChecked", checkboxChecked);
    }

    /**
     * This property is not synchronized automatically from the client side, so
     * the returned value may not be the same as in client side.
     * 
     * @return the {@code clearText} property from the webcomponent
     */
    public String getClearText() {
        return getElement().getProperty("clearText");
    }

    /**
     * @param clearText
     *            the String value to set
     */
    public void setClearText(String clearText) {
        getElement().setProperty("clearText",
                clearText == null ? "" : clearText);
    }

    /**
     * This property is not synchronized automatically from the client side, so
     * the returned value may not be the same as in client side.
     * 
     * @return the {@code showExtraFilters} property from the webcomponent
     */
    public boolean isShowExtraFilters() {
        return getElement().getProperty("showExtraFilters", false);
    }

    /**
     * @param showExtraFilters
     *            the boolean value to set
     */
    public void setShowExtraFilters(boolean showExtraFilters) {
        getElement().setProperty("showExtraFilters", showExtraFilters);
    }

    public void clearSearch() {
        getElement().callFunction("clearSearch");
    }

    @DomEvent("button-click")
    public static class ButtonClickEvent<R extends GeneratedStarterSearchBar<R>>
            extends ComponentEvent<R> {
        public ButtonClickEvent(R source, boolean fromClient) {
            super(source, fromClient);
        }
    }

    /**
     * Adds a listener for {@code button-click} events fired by the
     * webcomponent.
     * 
     * @param listener
     *            the listener
     * @return a {@link Registration} for removing the event listener
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Registration addButtonClickListener(
            ComponentEventListener<ButtonClickEvent<R>> listener) {
        return addListener(ButtonClickEvent.class,
                (ComponentEventListener) listener);
    }

    @DomEvent("field-value-changed")
    public static class FieldValueChangeEvent<R extends GeneratedStarterSearchBar<R>>
            extends ComponentEvent<R> {
        public FieldValueChangeEvent(R source, boolean fromClient) {
            super(source, fromClient);
        }
    }

    /**
     * Adds a listener for {@code field-value-changed} events fired by the
     * webcomponent.
     * 
     * @param listener
     *            the listener
     * @return a {@link Registration} for removing the event listener
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Registration addFieldValueChangeListener(
            ComponentEventListener<FieldValueChangeEvent<R>> listener) {
        return addListener(FieldValueChangeEvent.class,
                (ComponentEventListener) listener);
    }

    @DomEvent("checkbox-checked-changed")
    public static class CheckboxCheckedChangeEvent<R extends GeneratedStarterSearchBar<R>>
            extends ComponentEvent<R> {
        public CheckboxCheckedChangeEvent(R source, boolean fromClient) {
            super(source, fromClient);
        }
    }

    /**
     * Adds a listener for {@code checkbox-checked-changed} events fired by the
     * webcomponent.
     * 
     * @param listener
     *            the listener
     * @return a {@link Registration} for removing the event listener
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Registration addCheckboxCheckedChangeListener(
            ComponentEventListener<CheckboxCheckedChangeEvent<R>> listener) {
        return addListener(CheckboxCheckedChangeEvent.class,
                (ComponentEventListener) listener);
    }
}