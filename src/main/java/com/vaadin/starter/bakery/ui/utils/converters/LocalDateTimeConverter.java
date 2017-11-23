package com.vaadin.starter.bakery.ui.utils.converters;

import static com.vaadin.starter.bakery.ui.dataproviders.DataProviderUtil.convertIfNotNull;
import static com.vaadin.starter.bakery.ui.utils.FormattingUtils.FULL_DATE_FORMATTER;

import java.time.LocalDateTime;

import com.vaadin.flow.model.ModelConverter;

public class LocalDateTimeConverter implements ModelConverter<LocalDateTime, String> {


	private static final LocalTimeConverter TIME_FORMATTER = new LocalTimeConverter();

	@Override
	public String toPresentation(LocalDateTime modelValue) {
		return convertIfNotNull(modelValue,
				v -> FULL_DATE_FORMATTER.format(v) + " " + TIME_FORMATTER.toPresentation(v.toLocalTime()));
	}

	@Override
	public LocalDateTime toModel(String presentationValue) {
		throw new UnsupportedOperationException();
	}
}
