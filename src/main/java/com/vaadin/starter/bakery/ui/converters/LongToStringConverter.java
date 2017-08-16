package com.vaadin.starter.bakery.ui.converters;

import com.vaadin.flow.template.model.ModelConverter;

public class LongToStringConverter implements ModelConverter<Long, String> {

	@Override
	public String toPresentation(Long modelValue) {
		if (modelValue == null) {
			return null;
		}
		return modelValue.toString();
	}

	@Override
	public Long toModel(String presentationValue) {
		if (presentationValue == null) {
			return null;
		}
		return Long.parseLong(presentationValue);
	}

}
