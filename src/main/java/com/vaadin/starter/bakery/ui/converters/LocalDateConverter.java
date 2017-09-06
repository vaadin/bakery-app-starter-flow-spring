package com.vaadin.starter.bakery.ui.converters;

import static com.vaadin.starter.bakery.ui.dataproviders.DataProviderUtil.convertIfNotNull;

import java.time.LocalDate;

import com.vaadin.flow.template.model.ModelConverter;

public class LocalDateConverter implements ModelConverter<LocalDate, String> {

	@Override
	public String toPresentation(LocalDate modelValue) {
		return convertIfNotNull(modelValue, LocalDate::toString);
	}

	@Override
	public LocalDate toModel(String presentationValue) {
		return convertIfNotNull(presentationValue, LocalDate::parse);
	}

}
