package com.vaadin.starter.bakery.ui.presenter;

import com.vaadin.data.ValidationException;
import com.vaadin.starter.bakery.ui.HasToast;

public interface EntityView<T> extends HasToast {

	void closeDialog(boolean updated);

	void openDialog(T entity, boolean edit);

	default void showError(String message, boolean isPersistent) {
		toast(message, isPersistent);
	}

	default void update(T entity) {

	}

	Confirmer getConfirmer();

	boolean isDirty();

	void write(T entity) throws ValidationException;

}
