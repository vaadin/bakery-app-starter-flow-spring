package com.vaadin.starter.bakery.ui.view.workaround;

import com.vaadin.shared.Registration;
import com.vaadin.ui.combobox.ComboBox;
import com.vaadin.ui.common.HasValue;

/**
 * Implements a workaround for https://github.com/vaadin/flow/issues/2930.
 *
 */
public class ComboboxBinderWrapper<T> implements HasValue<ComboBox<T>, T> {

	private final ComboBox<T> comboBox;

	private T lastSetValue;

	public ComboboxBinderWrapper(ComboBox<T> comboBox) {
		this.comboBox = comboBox;
	}

	@Override
	public void setValue(T value) {
		final String ATTRIBUTE = "value";
		comboBox.setValue(value);
		this.lastSetValue = value;
		if (value != null) {
			comboBox.getElement().setAttribute(ATTRIBUTE, comboBox.getItemLabelGenerator().apply(value));
		}
	}

	public Registration addValueChangeListener(ValueChangeListener<ComboBox<T>, T> listener) {
		return comboBox.addValueChangeListener(e -> {
			boolean isLastSetValue = e.getValue() != null && e.getValue().equals(lastSetValue);
			if (e.getOldValue() != null || !isLastSetValue) {
				listener.onComponentEvent(e);
			}
		});
	}

	@Override
	public T getValue() {
		return comboBox.getValue();
	}

	@Override
	public ComboBox<T> get() {
		return comboBox;
	}

	public T getLastSetValue() {
		return lastSetValue;
	}

}