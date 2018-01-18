package com.vaadin.starter.bakery.ui.utils.converters;

import com.vaadin.flow.templatemodel.ModelConverter;
import com.vaadin.starter.bakery.ui.dataproviders.DataProviderUtil;
import com.vaadin.starter.bakery.ui.utils.FormattingUtils;

public class CurrencyFormatter implements ModelConverter<Integer, String> {

	@Override
	public String toPresentation(Integer modelValue) {
		return DataProviderUtil.convertIfNotNull(modelValue, FormattingUtils::formatAsCurrency);
	}

	@Override
	public Class<Integer> getModelType() {
		return Integer.TYPE;
	}

	@Override
	public Integer toModel(String presentationValue) {
		throw new UnsupportedOperationException();
	}
}
