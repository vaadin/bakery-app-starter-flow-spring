package com.vaadin.starter.bakery.ui.utils.converters;

import static com.vaadin.starter.bakery.ui.dataproviders.DataProviderUtil.convertIfNotNull;
import static com.vaadin.starter.bakery.ui.utils.FormattingUtils.HOUR_FORMATTER;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;

import com.vaadin.data.Converter;
import com.vaadin.data.Result;
import com.vaadin.data.ValueContext;
import com.vaadin.flow.model.ModelConverter;

public class LocalTimeConverter implements ModelConverter<LocalTime, String>, Converter<String, LocalTime> {

	@Override
	public String toPresentation(LocalTime modelValue) {
		return convertIfNotNull(modelValue, HOUR_FORMATTER::format);
	}

	@Override
	public LocalTime toModel(String presentationValue) {
		return convertIfNotNull(presentationValue, p -> LocalTime.parse(p, HOUR_FORMATTER));
	}

	@Override
	public Result<LocalTime> convertToModel(String value, ValueContext context) {
		try {
			return Result.ok(toModel(value));
		} catch (DateTimeParseException e) {
			return Result.error("Invalid time");
		}
	}

	@Override
	public String convertToPresentation(LocalTime value, ValueContext context) {
		return toPresentation(value);
	}

}
