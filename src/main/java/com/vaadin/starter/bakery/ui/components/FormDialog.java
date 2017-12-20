package com.vaadin.starter.bakery.ui.components;

import com.vaadin.shared.Registration;
import com.vaadin.ui.Component;
import com.vaadin.ui.Tag;
import com.vaadin.ui.common.HtmlImport;
import com.vaadin.ui.event.ComponentEvent;
import com.vaadin.ui.event.ComponentEventListener;
import com.vaadin.ui.event.DomEvent;
import com.vaadin.ui.event.Synchronize;

import elemental.json.JsonObject;

@Tag("form-dialog")
@HtmlImport("src/elements/form-dialog.html")
public class FormDialog extends Component {

	@Synchronize(property = "opened", value = "opened-changed")
	public boolean isOpened() {
		return getElement().getProperty("opened", false);
	}

	public void setOpened(boolean opened) {
		getElement().setProperty("opened", opened);
	}

	public boolean isNoCloseOnOutsideClick() {
		return getElement().getProperty("noCloseOnOutsideClick", false);
	}

	public void setNoCloseOnOutsideClick(boolean noCloseOnOutsideClick) {
		getElement().setProperty("noCloseOnOutsideClick",
				noCloseOnOutsideClick);
	}

	public boolean isNoCloseOnEsc() {
		return getElement().getProperty("noCloseOnEsc", false);
	}

	public void setNoCloseOnEsc(boolean noCloseOnEsc) {
		getElement().setProperty("noCloseOnEsc", noCloseOnEsc);
	}

	public String getTheme() {
		return getElement().getProperty("theme");
	}

	public void setTheme(String theme) {
		getElement().setProperty("theme", theme == null ? "" : theme);
	}

	protected JsonObject protectedGetFocusTrap() {
		return (JsonObject) getElement().getPropertyRaw("focusTrap");
	}

	protected void setFocusTrap(JsonObject focusTrap) {
		getElement().setPropertyJson("focusTrap", focusTrap);
	}

	@DomEvent("opened-changed")
	public static class OpenedChangeEvent extends ComponentEvent<FormDialog> {
		public OpenedChangeEvent(FormDialog source, boolean fromClient) {
			super(source, fromClient);
		}
	}

	public Registration addOpenedChangeListener(ComponentEventListener<OpenedChangeEvent> listener) {
		return addListener(OpenedChangeEvent.class, listener);
	}
}

