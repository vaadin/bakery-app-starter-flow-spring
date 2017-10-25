package com.vaadin.starter.bakery.ui.view;

import com.vaadin.data.ValidationException;
import com.vaadin.ui.event.ComponentEventNotifier;

public interface EntityEditor<E> extends ComponentEventNotifier {

	boolean isDirty();

	void read(E entity);

	void write(E entity) throws ValidationException;
}
