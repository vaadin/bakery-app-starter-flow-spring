package com.vaadin.starter.bakery.ui.components;

import com.vaadin.annotations.HtmlImport;
import com.vaadin.annotations.Id;
import com.vaadin.annotations.Tag;
import com.vaadin.flow.event.ComponentEventListener;
import com.vaadin.flow.template.PolymerTemplate;
import com.vaadin.flow.template.model.TemplateModel;
import com.vaadin.shared.Registration;
import com.vaadin.ui.Button;
import com.vaadin.ui.HasClickListeners.ClickEvent;

@Tag("confirm-dialog")
@HtmlImport("frontend://src/elements/confirm-dialog.html")
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

	@Id("cancel")
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

	public void show(String caption, String message, String okText, String cancelText,
			ComponentEventListener<ClickEvent<Button>> okListener,
			ComponentEventListener<ClickEvent<Button>> cancelListener) {
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