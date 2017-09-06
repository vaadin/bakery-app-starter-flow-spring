package com.vaadin.starter.bakery.ui.converters;

import com.vaadin.flow.template.model.ModelConverter;
import com.vaadin.starter.bakery.ui.dataproviders.DataProviderUtil;

public class EnumConverter<E extends Enum<E>> implements ModelConverter<E, LabelValueBean> {

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

}
