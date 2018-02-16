package com.vaadin.starter.bakery.ui.components;

import com.vaadin.flow.component.Focusable;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.shared.Registration;

/**
 * Workaround for date-picker to avoid receiving a value-changed event when the
 * value has been changed from server side. This prevents binder marking the
 * bean dirty. https://github.com/vaadin/flow/issues/3350
 */
public class ComboBoxForBinder<T> extends ComboBox<T> implements Focusable<ComboBox<T>> {

	private T lastNotifiedValue;
	private boolean setFromServer;

	@Override
	public void setValue(T value) {
		super.setValue(value);
		setFromServer = true;
	}

	@Override
	public Registration addValueChangeListener(ValueChangeListener<ComboBox<T>, T> listener) {
		return super.addValueChangeListener((ValueChangeListener<ComboBox<T>, T>) e -> {
			if (!setFromServer && (getValue() != null && !getValue().equals(lastNotifiedValue)
					|| e.getValue() == null && lastNotifiedValue != null)) {
				listener.onComponentEvent(e);
			}
			lastNotifiedValue = e.getValue();
			setFromServer = false;
		});
	}
}
