package com.vaadin.starter.bakery.ui.converters;

import static com.vaadin.starter.bakery.ui.dataproviders.DataProviderUtil.convertIfNotNull;

import java.time.LocalTime;

import com.vaadin.flow.template.model.ModelConverter;

public class LocalTimeConverter implements ModelConverter<LocalTime, String> {

	@Override
	public String toPresentation(LocalTime modelValue) {
		return convertIfNotNull(modelValue, LocalTime::toString);
	}

	@Override
	public LocalTime toModel(String presentationValue) {
		return convertIfNotNull(presentationValue, LocalTime::parse);
	}

}
