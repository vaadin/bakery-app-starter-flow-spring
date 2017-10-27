package com.vaadin.starter.bakery.ui.presenter;

import com.vaadin.data.ValidationException;
import com.vaadin.starter.bakery.ui.HasNotifications;
import com.vaadin.starter.bakery.ui.messages.Message;

public interface EntityView<T> extends HasNotifications {

	void closeDialog(boolean updated);

	void openDialog(T entity, boolean edit);

	void showConfirmationRequest(Message message);

	default void showError(String message, boolean isPersistent) {
		showNotification(message, isPersistent);
	}

	boolean isDirty();

	void write(T entity) throws ValidationException;
}
