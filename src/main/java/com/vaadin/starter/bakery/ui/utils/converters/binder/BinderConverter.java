package com.vaadin.starter.bakery.ui.utils.converters.binder;

import com.vaadin.data.Converter;
import com.vaadin.data.Result;
import com.vaadin.data.ValueContext;

public interface BinderConverter<PRESENTATION, MODEL> extends Converter<PRESENTATION, MODEL> {

	@Override
	default Result<MODEL> convertToModel(PRESENTATION presentationValue, ValueContext valueContext) {
		return presentationValue != null && !"".equals(presentationValue)
				? convertToModelIfNotNull(presentationValue, valueContext)
						: convertNullToModel(presentationValue, valueContext);
	}

	@Override
	default PRESENTATION convertToPresentation(MODEL modelValue, ValueContext valueContext) {
		return modelValue != null ? convertToPresentationIfNotNull(modelValue, valueContext)
				: convertNullToPresentation(modelValue, valueContext);
	}

	default Result<MODEL> convertNullToModel(PRESENTATION presentationValue, ValueContext valueContext) {
		return Result.ok(null);
	}

	default PRESENTATION convertNullToPresentation(MODEL modelValue, ValueContext valueContext) {
		return null;
	}

	Result<MODEL> convertToModelIfNotNull(PRESENTATION presentationValue, ValueContext valueContext);

	PRESENTATION convertToPresentationIfNotNull(MODEL modelValue, ValueContext valueContext);
}
