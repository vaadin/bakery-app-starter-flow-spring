package com.vaadin.starter.bakery.ui.utils.messages;

import com.vaadin.generated.starter.elements.GeneratedStarterConfirmDialog;
import com.vaadin.shared.Registration;
import com.vaadin.ui.UI;
import com.vaadin.ui.event.ComponentEventListener;

@SuppressWarnings({ "unchecked", "rawtypes", "serial" })
public class Message {

	private static ComponentEventListener onOk;

	/**
	 * A singleton of <starter-confirmation-dialog> for reusing, since
	 * only one instance is used simultaneously in the app.
	 *
	 * We don't need to create a new class, since overriding a couple of
	 * methods in the generated class is enough for having an unique instance.
	 */
	public static final GeneratedStarterConfirmDialog<?> confirmation = new GeneratedStarterConfirmDialog() {
		{
			super.addOkClickListener(e -> {
				if (onOk != null) {
					onOk.onComponentEvent(e);
					onOk = null;
					UI.getCurrent().remove(this);
				}
			});

			super.addCancelClickListener(e -> {
				UI.getCurrent().remove(this);
			 });
		}

		@Override
		public void setOpened(boolean opened) {
			if (opened) {
				UI.getCurrent().add(this);
			}
			super.setOpened(opened);
		};

		@Override
		public Registration addOkClickListener(ComponentEventListener listener) {
			onOk = listener;
			return null;
		};
	};

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
		confirmation.setMessage(message.getMessage());
		confirmation.setCaption(message.getCaption());
		confirmation.setCancelText(message.getCancelText());
		confirmation.setOkText(message.getOkText());
		confirmation.addOkClickListener(e -> onOk.run());
		confirmation.setOpened(true);
	}

	/**
	 * Used in tests. It emulates an OK event.
	 */
	public static void confirm() {
		onOk.onComponentEvent(null);
		onOk = null;
		confirmation.setOpened(false);
	}
}
