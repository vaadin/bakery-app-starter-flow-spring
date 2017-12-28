package com.vaadin.starter.bakery.ui.components;

import com.vaadin.shared.Registration;
import com.vaadin.ui.combobox.ComboBox;

/**
 * Workaround for combo-box to avoid receiving a value-changed event when
 * setting the value from server side.
 */
public class ComboBoxForBinder<T> extends ComboBox<T> {

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
			if (e.getValue() != null && !e.getValue().equals(lastSetValue)) {
				listener.onComponentEvent(e);
			}
		});
	}
}