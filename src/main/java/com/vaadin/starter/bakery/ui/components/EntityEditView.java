package com.vaadin.starter.bakery.ui.components;

import com.vaadin.data.ValidationException;
import com.vaadin.shared.Registration;
import com.vaadin.starter.bakery.backend.data.entity.AbstractEntity;
import com.vaadin.starter.bakery.ui.event.CancelEvent;
import com.vaadin.starter.bakery.ui.event.DeleteEvent;
import com.vaadin.starter.bakery.ui.event.SaveEvent;
import com.vaadin.starter.bakery.ui.event.ValidationFailedEvent;
import com.vaadin.ui.event.ComponentEventListener;

public interface EntityEditView<T extends AbstractEntity> {

	Registration addSaveListener(ComponentEventListener<SaveEvent> listener);

	Registration addDeleteListener(ComponentEventListener<DeleteEvent> listener);

	Registration addCancelListener(ComponentEventListener<CancelEvent> listener);

	Registration addValidationFailedEvent(ComponentEventListener<ValidationFailedEvent> listener);

	void read(T entity);

	boolean isDirty();

	void write(T entity) throws ValidationException;
}