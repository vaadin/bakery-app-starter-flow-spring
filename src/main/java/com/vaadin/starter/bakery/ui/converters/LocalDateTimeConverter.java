package com.vaadin.starter.bakery.ui.converters;

import com.vaadin.flow.model.ModelConverter;

import java.time.LocalDateTime;

import static com.vaadin.starter.bakery.ui.dataproviders.DataProviderUtil.convertIfNotNull;

public class LocalDateTimeConverter implements ModelConverter<LocalDateTime, String> {

	@Override
	public String toPresentation(LocalDateTime modelValue) {
		return convertIfNotNull(modelValue, LocalDateTime::toString);
	}

	@Override
	public LocalDateTime toModel(String presentationValue) {
		return convertIfNotNull(presentationValue, LocalDateTime::parse);
	}

}
