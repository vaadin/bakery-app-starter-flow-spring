package com.vaadin.starter.bakery.ui.presenter;

import com.vaadin.data.ValidationException;
import com.vaadin.starter.bakery.ui.HasNotifications;

public interface EntityView<T> extends HasNotifications {

	void closeDialog(boolean updated);

	void openDialog(T entity, boolean edit);

	default void showError(String message, boolean isPersistent) {
		showNotification(message, isPersistent);
	}

	default void update(T entity) {

	}

	Confirmer getConfirmer();

	boolean isDirty();

	void write(T entity) throws ValidationException;

}
