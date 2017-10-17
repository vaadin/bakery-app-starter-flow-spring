package com.vaadin.starter.bakery.ui.presenter;

import java.util.Optional;

import com.vaadin.data.ValidationException;
import com.vaadin.shared.Registration;
import com.vaadin.starter.bakery.ui.HasToast;
import com.vaadin.starter.bakery.ui.event.CancelEvent;
import com.vaadin.starter.bakery.ui.event.DeleteEvent;
import com.vaadin.starter.bakery.ui.event.SaveEvent;
import com.vaadin.ui.UI;
import com.vaadin.ui.event.ComponentEventListener;

public interface EntityView<T> extends HasToast {

	void closeDialog(boolean updated);

	void openDialog(T entity, boolean edit);

	default void showError(String message, boolean isPersistent) {
		toast(message, isPersistent);
	}

	default void navigateToEntity(Optional<UI> ui, String basePage, String id) {
		final String location = basePage + (id == null || id.isEmpty() ? "" : "/" + id);
		ui.ifPresent(u -> u.navigateTo(location));
	}

	default void update(T entity) {

	}

	Confirmer getConfirmer();

	boolean isDirty();

	void write(T entity) throws ValidationException;

	Registration addSaveListener(ComponentEventListener<SaveEvent> listener);

	Registration addCancelListener(ComponentEventListener<CancelEvent> listener);

	Registration addDeleteListener(ComponentEventListener<DeleteEvent> listener);

}
