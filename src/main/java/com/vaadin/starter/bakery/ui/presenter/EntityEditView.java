package com.vaadin.starter.bakery.ui.presenter;

import com.vaadin.data.ValidationException;
import com.vaadin.starter.bakery.backend.data.entity.AbstractEntity;
import com.vaadin.ui.event.ComponentEventNotifier;

public interface EntityEditView<T extends AbstractEntity> extends ComponentEventNotifier {

	boolean isDirty();

	void write(T entity) throws ValidationException;

}