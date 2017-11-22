package com.vaadin.starter.bakery.ui.view.storefront.converter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.vaadin.flow.model.ModelConverter;
import com.vaadin.starter.bakery.ui.utils.BakeryConst;

/**
 * Date converter specific for the way date is displayed in storefront.
 */
public class StorefrontLocalDateConverter implements ModelConverter<LocalDate, StorefrontDate> {

	private static final DateTimeFormatter DAY_FORMATTER = DateTimeFormatter.ofPattern("MMM d", BakeryConst.APP_LOCALE);
	private static final DateTimeFormatter WEEKDAY_FORMATTER = DateTimeFormatter.ofPattern("EEEE",
			BakeryConst.APP_LOCALE);

	@Override
	public StorefrontDate toPresentation(LocalDate modelValue) {
		StorefrontDate result = null;
		if (modelValue != null) {
			result = new StorefrontDate();
			result.setDay(DAY_FORMATTER.format(modelValue));
			result.setWeekday(WEEKDAY_FORMATTER.format(modelValue));
			result.setDate(modelValue.toString());
		}
		return result;
	}

	@Override
	public LocalDate toModel(StorefrontDate presentationValue) {
		throw new UnsupportedOperationException();
	}

}
