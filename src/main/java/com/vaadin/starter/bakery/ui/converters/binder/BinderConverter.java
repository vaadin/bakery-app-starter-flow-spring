package com.vaadin.starter.bakery.ui.converters.binder;

import com.vaadin.data.Converter;
import com.vaadin.data.Result;
import com.vaadin.data.ValueContext;

public interface BinderConverter<PRESENTATION, MODEL> extends Converter<PRESENTATION, MODEL> {

	@Override
	default Result<MODEL> convertToModel(PRESENTATION arg0, ValueContext arg1) {
		return arg0 != null && !"".equals(arg0)? convertToModelIfNotNull(arg0, arg1) : convertNullToModel(arg0, arg1);
	}

	@Override
	default PRESENTATION convertToPresentation(MODEL arg0, ValueContext arg1) {
		return arg0 != null? convertToPresentationIfNotNull(arg0, arg1) : convertNullToPresentation(arg0, arg1);
	}

	Result<MODEL> convertToModelIfNotNull(PRESENTATION arg0, ValueContext arg1);

	PRESENTATION convertToPresentationIfNotNull(MODEL arg0, ValueContext arg1);

	default Result<MODEL> convertNullToModel(PRESENTATION arg0, ValueContext arg1) {
		return Result.ok(null);
	}

	default PRESENTATION convertNullToPresentation(MODEL arg0, ValueContext arg1) {
		return null;
	}
}
