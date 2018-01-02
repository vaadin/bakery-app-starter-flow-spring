package com.vaadin.starter.bakery.ui.view.storefront.converter;

import static com.vaadin.starter.bakery.ui.utils.FormattingUtils.MONTH_AND_DAY_FORMATTER;
import static com.vaadin.starter.bakery.ui.utils.FormattingUtils.WEEKDAY_FULLNAME_FORMATTER;

import java.time.LocalDate;

import com.vaadin.flow.templatemodel.ModelConverter;

/**
 * Date converter specific for the way date is displayed in storefront.
 */
public class StorefrontLocalDateConverter implements ModelConverter<LocalDate, StorefrontDate> {

	@Override
	public StorefrontDate toPresentation(LocalDate modelValue) {
		StorefrontDate result = null;
		if (modelValue != null) {
			result = new StorefrontDate();
			result.setDay(MONTH_AND_DAY_FORMATTER.format(modelValue));
			result.setWeekday(WEEKDAY_FULLNAME_FORMATTER.format(modelValue));
			result.setDate(modelValue.toString());
		}
		return result;
	}

	@Override
	public LocalDate toModel(StorefrontDate presentationValue) {
		throw new UnsupportedOperationException();
	}

}
