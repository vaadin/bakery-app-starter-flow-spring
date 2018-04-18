package com.vaadin.starter.bakery.ui.components;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.DomEvent;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.shared.Registration;

@Tag("confirm-dialog")
@HtmlImport("src/components/confirm-dialog.html")
public class ConfirmDialog extends Dialog {

	public String getCaption() {
		return getElement().getProperty("caption");
	}

	public void setCaption(String caption) {
		getElement().setProperty("caption", caption == null ? "" : caption);
	}

	public void setMessage(String message) {
		getElement().setProperty("message", message == null ? "" : message);
	}

	public void setOkText(String okText) {
		getElement().setProperty("okText", okText == null ? "" : okText);
	}

	public void setCancelText(String cancelText) {
		getElement().setProperty("cancelText", cancelText == null ? "" : cancelText);
	}

	@DomEvent("cancel-click")
	public static class CancelClickEvent extends ComponentEvent<ConfirmDialog> {
		public CancelClickEvent(ConfirmDialog source, boolean fromClient) {
			super(source, fromClient);
		}
	}

	public Registration addCancelClickListener(
			ComponentEventListener<CancelClickEvent> listener) {
		return addListener(CancelClickEvent.class, listener);
	}

	@DomEvent("ok-click")
	public static class OkClickEvent extends ComponentEvent<ConfirmDialog> {
		public OkClickEvent(ConfirmDialog source, boolean fromClient) {
			super(source, fromClient);
		}
	}

	public Registration addOkClickListener(
			ComponentEventListener<OkClickEvent> listener) {
		return addListener(OkClickEvent.class, listener);
	}
}
