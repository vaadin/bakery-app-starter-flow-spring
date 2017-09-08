package com.vaadin.starter.bakery.ui.converters;

import com.vaadin.data.Result;
import com.vaadin.data.ValueContext;
import com.vaadin.flow.template.model.ModelConverter;
import com.vaadin.starter.bakery.ui.converters.binder.BinderConverter;
import com.vaadin.starter.bakery.ui.dataproviders.DataProviderUtil;

public class EnumConverter<E extends Enum<E>> implements ModelConverter<E, LabelValueBean>,BinderConverter<String, E> {

	private final Class<E> enumClass;

	public EnumConverter(Class<E> enumClass) {
		this.enumClass = enumClass;
	}

	@Override
	public LabelValueBean toPresentation(E modelValue) {
		return DataProviderUtil.convertIfNotNull(modelValue, e -> new LabelValueBean(e.name(), e.toString()));
	}

	@Override
	public E toModel(LabelValueBean presentationValue) {
		return DataProviderUtil.convertIfNotNull(presentationValue, p -> Enum.valueOf(enumClass, p.getValue()));
	}

	@Override
	public Result<E> convertToModelIfNotNull(String arg0, ValueContext arg1) {
		return Result.ok(Enum.valueOf(enumClass, arg0));
	}

	@Override
	public String convertToPresentationIfNotNull(E arg0, ValueContext arg1) {
		return arg0.toString();
	}

}
