package com.vaadin.starter.bakery.ui.utils.converters;

import static com.vaadin.starter.bakery.ui.dataproviders.DataProviderUtil.convertIfNotNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.vaadin.flow.model.ModelConverter;

public class LocalDateTimeConverter implements ModelConverter<LocalDateTime, String> {

	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");
	private static final LocalTimeConverter TIME_FORMATTER = new LocalTimeConverter();

	@Override
	public String toPresentation(LocalDateTime modelValue) {
		return convertIfNotNull(modelValue,
				v -> DATE_FORMATTER.format(v) + " " + TIME_FORMATTER.toPresentation(v.toLocalTime()));
	}

	@Override
	public LocalDateTime toModel(String presentationValue) {
		throw new UnsupportedOperationException();
	}
}
