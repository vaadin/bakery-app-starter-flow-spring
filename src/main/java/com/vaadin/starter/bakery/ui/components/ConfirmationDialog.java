package com.vaadin.starter.bakery.ui.components;

import com.vaadin.flow.model.TemplateModel;
import com.vaadin.shared.Registration;
import com.vaadin.starter.bakery.ui.event.DecisionEvent;
import com.vaadin.starter.bakery.ui.messages.Message;
import com.vaadin.starter.bakery.ui.presenter.Confirmer;
import com.vaadin.ui.Tag;
import com.vaadin.ui.button.Button;
import com.vaadin.ui.common.HtmlImport;
import com.vaadin.ui.event.ComponentEventListener;
import com.vaadin.ui.polymertemplate.Id;
import com.vaadin.ui.polymertemplate.PolymerTemplate;

@Tag("confirm-dialog")
@HtmlImport("context://src/elements/confirm-dialog.html")
public class ConfirmationDialog extends PolymerTemplate<ConfirmationDialog.Model> implements Confirmer {

	public interface Model extends TemplateModel {

		void setMessage(String message);

		void setCaption(String caption);

		void setOkText(String okText);

		void setCancelText(String cancelText);

		void setOpened(Boolean opened);
	}

	@Id("confirm-dialog-ok")
	private Button okButton;

	@Id("confirm-dialog-cancel")
	private Button cancelButton;

	public ConfirmationDialog() {
		okButton.addClickListener(e -> fireEvent(new DecisionEvent(this, true)));
		okButton.addClickListener(e -> getModel().setOpened(false));
		cancelButton.addClickListener(e -> fireEvent(new DecisionEvent(this, false)));
		cancelButton.addClickListener(e -> getModel().setOpened(false));
	}

	public void show(Message message) {
		show(message.getCaption(), message.getMessage(), message.getOkText(), message.getCancelText());
	}

	public void show(String caption, String message, String okText, String cancelText) {
		getModel().setCaption(caption);
		getModel().setMessage(message);
		getModel().setOkText(okText);
		getModel().setCancelText(cancelText);
		getModel().setOpened(true);
	}

	public Registration addDecisionListener(ComponentEventListener<DecisionEvent> listener) {
		return addListener(DecisionEvent.class, listener);
	}
}