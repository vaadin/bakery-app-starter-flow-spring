package com.vaadin.starter.bakery.ui.view.wrapper;

import com.vaadin.shared.Registration;
import com.vaadin.ui.combobox.ComboBox;

/**
 * Wrapper for combobox to allow initial values to be set via binder.
 */
public class ComboboxBinderWrapper<T> extends ComboBox<T> {

	private T lastSetValue;

	@Override
	public void setValue(T value) {
		super.setValue(value);
		this.lastSetValue = value;
		if (value != null) {
			getElement().setAttribute("value", getItemLabelGenerator().apply(value));
		}
	}

	@Override
	public Registration addValueChangeListener(ValueChangeListener<ComboBox<T>, T> listener) {
		return super.addValueChangeListener(e -> {
			if (e.getOldValue() != null && e.getValue() != null && !e.getValue().equals(lastSetValue)) {
				listener.onComponentEvent(e);
			}
		});
	}
}