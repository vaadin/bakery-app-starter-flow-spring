package com.vaadin.starter.bakery.ui.components.storefront.converter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.vaadin.flow.model.ModelConverter;

/**
 * Date converter specific for the way date is displayed in storefront.
 */
public class StorefrontLocalDateConverter implements ModelConverter<LocalDate, StorefrontDate> {

	private static final DateTimeFormatter DAY_FORMATTER = DateTimeFormatter.ofPattern("MMM d");
	private static final DateTimeFormatter WEEKDAY_FORMATTER = DateTimeFormatter.ofPattern("EEEE");

	@Override
	public StorefrontDate toPresentation(LocalDate modelValue) {
		StorefrontDate result = null;
		if (modelValue != null) {
			result = new StorefrontDate();
			result.setDay(DAY_FORMATTER.format(modelValue));
			result.setWeekday(WEEKDAY_FORMATTER.format(modelValue));
		}
		return result;
	}

	@Override
	public LocalDate toModel(StorefrontDate presentationValue) {
		// Not implemented.
		throw new UnsupportedOperationException();
	}

}
