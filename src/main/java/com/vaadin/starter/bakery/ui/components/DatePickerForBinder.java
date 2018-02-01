package com.vaadin.starter.bakery.ui.components;


import java.time.LocalDate;

import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.shared.Registration;

/**
 * Workaround for date-picker to avoid receiving a value-changed event when the
 * value has been changed from server side. This prevents binder marking the
 * bean dirty. https://github.com/vaadin/flow/issues/3350
 */
public class DatePickerForBinder extends DatePicker {

	private LocalDate lastSetValue;

	@Override
	public void setValue(LocalDate value) {
		super.setValue(value);
		this.lastSetValue = value;
	}

	@Override
	public Registration addValueChangeListener(ValueChangeListener<DatePicker, LocalDate> listener) {
		return super.addValueChangeListener(e -> {
			if (e.getValue() != null && !e.getValue().equals(lastSetValue)
					|| e.getValue() == null && lastSetValue != null) {
				listener.onComponentEvent(e);
			}
		});
	}
}
