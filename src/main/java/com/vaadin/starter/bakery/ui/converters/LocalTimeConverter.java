package com.vaadin.starter.bakery.ui.converters;

import static com.vaadin.starter.bakery.ui.dataproviders.DataProviderUtil.convertIfNotNull;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;

import com.vaadin.data.Result;
import com.vaadin.data.ValueContext;
import com.vaadin.flow.template.model.ModelConverter;
import com.vaadin.starter.bakery.ui.converters.binder.BinderConverter;

public class LocalTimeConverter implements ModelConverter<LocalTime, String>, BinderConverter<String, LocalTime> {

	@Override
	public String toPresentation(LocalTime modelValue) {
		return convertIfNotNull(modelValue, LocalTime::toString);
	}

	@Override
	public LocalTime toModel(String presentationValue) {
		return convertIfNotNull(presentationValue, LocalTime::parse);
	}

	@Override
	public Result<LocalTime> convertToModelIfNotNull(String arg0, ValueContext arg1) {
		try {
			return Result.ok(LocalTime.parse(arg0));
		} catch (DateTimeParseException e) {
			return Result.error("Invalid time");
		}
	}

	@Override
	public String convertToPresentationIfNotNull(LocalTime arg0, ValueContext arg1) {
		return arg0.toString();
	}

}
