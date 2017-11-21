package com.vaadin.starter.bakery.ui.utils.converters;

import static com.vaadin.starter.bakery.ui.dataproviders.DataProviderUtil.convertIfNotNull;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.format.SignStyle;
import java.util.HashMap;
import java.util.Map;

import com.vaadin.data.Converter;
import com.vaadin.data.Result;
import com.vaadin.data.ValueContext;
import com.vaadin.flow.model.ModelConverter;

public class LocalTimeConverter implements ModelConverter<LocalTime, String>, Converter<String, LocalTime> {

	public static final DateTimeFormatter formatter;

	static {
		Map<Long, String> ampm = new HashMap<>();
		ampm.put(0L, "a.m.");
		ampm.put(1L, "p.m.");
		formatter = new DateTimeFormatterBuilder().parseCaseInsensitive().parseLenient()
				.appendValue(java.time.temporal.ChronoField.CLOCK_HOUR_OF_AMPM, 1, 2, SignStyle.NOT_NEGATIVE)
				.appendLiteral(':').appendValue(java.time.temporal.ChronoField.MINUTE_OF_HOUR, 2).optionalStart()
				.appendLiteral(' ').optionalEnd().appendText(java.time.temporal.ChronoField.AMPM_OF_DAY, ampm)
				.toFormatter();
	}

	@Override
	public String toPresentation(LocalTime modelValue) {
		return convertIfNotNull(modelValue, formatter::format);
	}

	@Override
	public LocalTime toModel(String presentationValue) {
		return convertIfNotNull(presentationValue, p -> LocalTime.parse(p, formatter));
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
