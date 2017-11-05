package com.vaadin.starter.bakery.ui.components;

import com.vaadin.flow.model.TemplateModel;
import com.vaadin.starter.bakery.ui.utils.messages.Message;
import com.vaadin.ui.Tag;
import com.vaadin.ui.UI;
import com.vaadin.ui.button.Button;
import com.vaadin.ui.common.HtmlImport;
import com.vaadin.ui.polymertemplate.Id;
import com.vaadin.ui.polymertemplate.PolymerTemplate;

@Tag("confirm-dialog")
@HtmlImport("src/elements/confirm-dialog.html")
public class ConfirmationDialog extends PolymerTemplate<ConfirmationDialog.Model> {

	public interface Model extends TemplateModel {

		void setMessage(String message);

		void setCaption(String caption);

		void setOkText(String okText);

		void setCancelText(String cancelText);
	}

	@Id("ok")
	private Button okButton;

	@Id("cancel")
	private Button cancelButton;

	public ConfirmationDialog() {
		okButton.addClickListener(e -> close());
		cancelButton.addClickListener(e -> close());
	}

	public void close() {
		getUI().ifPresent(ui -> ui.remove(this));
	}

	public static ConfirmationDialog show(Message message, Runnable onOk) {
		return show(UI.getCurrent(), message.getCaption(), message.getMessage(), message.getOkText(),
				message.getCancelText(), onOk);
	}

	public static ConfirmationDialog show(UI ui, String caption, String message, String okText, String cancelText,
			Runnable onOk) {
		ConfirmationDialog dialog = new ConfirmationDialog();
		dialog.getModel().setCaption(caption);
		dialog.getModel().setMessage(message);
		dialog.getModel().setOkText(okText);
		dialog.getModel().setCancelText(cancelText);
		dialog.okButton.addClickListener(e -> onOk.run());
		ui.add(dialog);
		return dialog;
	}

}