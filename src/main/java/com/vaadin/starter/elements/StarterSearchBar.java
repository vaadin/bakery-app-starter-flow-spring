package com.vaadin.starter.elements;

import com.vaadin.shared.Registration;
import com.vaadin.ui.Component;
import com.vaadin.ui.Tag;
import com.vaadin.ui.common.HtmlImport;
import com.vaadin.ui.event.ComponentEvent;
import com.vaadin.ui.event.ComponentEventListener;
import com.vaadin.ui.event.DomEvent;

/**
 * Java implementation of the polymer element
 * `starter-elements/starter-search-bar`
 * 
 * TODO: this hand-written class should be replaced by an auto-generated one
 * when flow provides a maven plugin for generating java classes for 3rd party
 * polymer elements.
 */
@Tag("starter-search-bar")
@HtmlImport("frontend://bower_components/starter-elements/starter-search-bar.html")
public class StarterSearchBar extends Component {

	public String getFieldPlaceholder() {
		return getElement().getProperty("fieldPlaceholder");
	}

	public void setFieldPlaceholder(String fieldPlaceholder) {
		getElement().setProperty("fieldPlaceholder",
				fieldPlaceholder == null ? "" : fieldPlaceholder);
	}

	public String getFieldValue() {
		return getElement().getProperty("fieldValue");
	}

	public void setFieldValue(String fieldValue) {
		getElement().setProperty("fieldValue",
				fieldValue == null ? "" : fieldValue);
	}

	public String getFieldIcon() {
		return getElement().getProperty("fieldIcon");
	}

	public void setFieldIcon(String fieldIcon) {
		getElement().setProperty("fieldIcon",
				fieldIcon == null ? "" : fieldIcon);
	}

	public String getButtonIcon() {
		return getElement().getProperty("buttonIcon");
	}

	public void setButtonIcon(String buttonIcon) {
		getElement().setProperty("buttonIcon",
				buttonIcon == null ? "" : buttonIcon);
	}

	public String getButtonText() {
		return getElement().getProperty("buttonText");
	}

	public void setButtonText(String buttonText) {
		getElement().setProperty("buttonText",
				buttonText == null ? "" : buttonText);
	}

	public boolean isShowCheckbox() {
		return getElement().getProperty("showCheckbox", false);
	}

	public void setShowCheckbox(boolean showCheckbox) {
		getElement().setProperty("showCheckbox", showCheckbox);
	}

	public String getCheckboxText() {
		return getElement().getProperty("checkboxText");
	}

	public void setCheckboxText(String checkboxText) {
		getElement().setProperty("checkboxText",
				checkboxText == null ? "" : checkboxText);
	}

	public boolean isCheckboxChecked() {
		return getElement().getProperty("checkboxChecked", false);
	}

	public void setCheckboxChecked(boolean checkboxChecked) {
		getElement().setProperty("checkboxChecked", checkboxChecked);
	}

	public String getClearText() {
		return getElement().getProperty("clearText");
	}

	public void setClearText(String clearText) {
		getElement().setProperty("clearText",
				clearText == null ? "" : clearText);
	}

	public boolean isShowExtraFilters() {
		return getElement().getProperty("showExtraFilters", false);
	}

	public void setShowExtraFilters(boolean showExtraFilters) {
		getElement().setProperty("showExtraFilters", showExtraFilters);
	}

	public void clearSearch() {
		getElement().callFunction("clearSearch");
	}

	@DomEvent("button-click")
	public static class ButtonClickEvent extends ComponentEvent {
		public ButtonClickEvent(StarterSearchBar source, boolean fromClient) {
			super(source, fromClient);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Registration addButtonClickListener(ComponentEventListener listener) {
		return addListener(ButtonClickEvent.class,
				listener);
	}

	@DomEvent("field-value-changed")
	public static class FieldValueChangeEvent extends ComponentEvent {
		public FieldValueChangeEvent(StarterSearchBar source, boolean fromClient) {
			super(source, fromClient);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Registration addFieldValueChangeListener(ComponentEventListener listener) {
		return addListener(FieldValueChangeEvent.class,
				listener);
	}

	@DomEvent("checkbox-checked-changed")
	public static class CheckboxCheckedChangeEvent extends ComponentEvent {
		public CheckboxCheckedChangeEvent(StarterSearchBar source, boolean fromClient) {
			super(source, fromClient);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Registration addCheckboxCheckedChangeListener(ComponentEventListener listener) {
		return addListener(CheckboxCheckedChangeEvent.class,
				listener);
	}
}