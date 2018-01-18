package com.vaadin.starter.bakery.ui.utils.messages;

import com.vaadin.flow.component.UI;
import com.vaadin.starter.bakery.ui.components.ConfirmDialog;

public class Message {

	public static final String CONFIRM_CAPTION_DELETE = "Confirm Delete";
	public static final String CONFIRM_MESSAGE_DELETE = "Are you sure you want to delete the selected Item? This action cannot be undone.";
	public static final String BUTTON_CAPTION_DELETE = "Delete";
	public static final String BUTTON_CAPTION_CANCEL = "Cancel";

	public static final MessageSupplier UNSAVED_CHANGES = createMessage("Unsaved Changes", "Discard", "Continue Editing",
			"There are unsaved modifications to the %s. Discard changes?");

	public static final MessageSupplier CONFIRM_DELETE = createMessage(CONFIRM_CAPTION_DELETE, BUTTON_CAPTION_DELETE,
			BUTTON_CAPTION_CANCEL, CONFIRM_MESSAGE_DELETE);

	private final String caption;
	private final String okText;
	private final String cancelText;
	private final String message;

	public Message(String caption, String okText, String cancelText, String message) {
		this.caption = caption;
		this.okText = okText;
		this.cancelText = cancelText;
		this.message = message;
	}

	private static MessageSupplier createMessage(String caption, String okText, String cancelText, String message) {
		return (parameters) -> new Message(caption, okText, cancelText, String.format(message, parameters));
	}

	public String getCaption() {
		return caption;
	}

	public String getOkText() {
		return okText;
	}

	public String getCancelText() {
		return cancelText;
	}

	public String getMessage() {
		return message;
	}

	@FunctionalInterface
	public interface MessageSupplier {
		Message createMessage(Object... parameters);
	}

	/**
	 * Show a confirmation dialog with the message.
	 * When user clicks on OK button, onOk callback is executed.
	 */
	public static void confirm(Message message, Runnable onOk) {
		ConfirmDialog confirmation = new ConfirmDialog();

		confirmation.setMessage(message.getMessage());
		confirmation.setCaption(message.getCaption());
		confirmation.setCancelText(message.getCancelText());
		confirmation.setOkText(message.getOkText());

		confirmation.addOkClickListener(e -> {
			onOk.run();
			UI.getCurrent().remove(confirmation);
		});
		confirmation.addCancelClickListener(e -> UI.getCurrent().remove(confirmation));

		UI.getCurrent().add(confirmation);
		confirmation.setOpened(true);
	}
}
