package com.vaadin.starter.bakery.ui.components;

import com.vaadin.flow.model.TemplateModel;
import com.vaadin.shared.Registration;
import com.vaadin.starter.bakery.ui.messages.Message;
import com.vaadin.ui.Tag;
import com.vaadin.ui.button.Button;
import com.vaadin.ui.common.HasClickListeners;
import com.vaadin.ui.common.HtmlImport;
import com.vaadin.ui.event.ComponentEventListener;
import com.vaadin.ui.polymertemplate.Id;
import com.vaadin.ui.polymertemplate.PolymerTemplate;

@Tag("confirm-dialog")
@HtmlImport("context://src/elements/confirm-dialog.html")
public class ConfirmationDialog extends PolymerTemplate<ConfirmationDialog.Model> {

	public interface Model extends TemplateModel {

		void setMessage(String message);

		void setCaption(String caption);

		void setOkText(String okText);

		void setCancelText(String cancelText);

		void setOpened(Boolean opened);
	}

	@Id("ok")
	private Button okButton;

	@Id("confirm-dialog-cancel")
	private Button cancelButton;

	private Registration okRegistration;
	private Registration cancelRegistration;

	private void clearRegistration() {
		if (okRegistration != null) {
			okRegistration.remove();
			okRegistration = null;
		}

		if (cancelRegistration != null) {
			cancelRegistration.remove();
			cancelRegistration = null;
		}
	}

	public void show(Message message, ComponentEventListener<HasClickListeners.ClickEvent<Button>> okListener) {
		show(message.getCaption(), message.getMessage(), message.getOkText(), message.getCancelText(), okListener,
				null);
	}

	public void show(String caption, String message, String okText, String cancelText,
			ComponentEventListener<HasClickListeners.ClickEvent<Button>> okListener,
			ComponentEventListener<HasClickListeners.ClickEvent<Button>> cancelListener) {
		clearRegistration();

		getModel().setCaption(caption);
		getModel().setMessage(message);
		getModel().setOkText(okText);
		getModel().setCancelText(cancelText);
		getModel().setOpened(true);

		okRegistration = okButton.addClickListener(okListener);

		if (cancelListener != null) {
			cancelRegistration = cancelButton.addClickListener(cancelListener);
		}
	}
}