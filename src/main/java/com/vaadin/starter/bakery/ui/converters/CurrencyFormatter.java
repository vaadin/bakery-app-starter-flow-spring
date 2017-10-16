package com.vaadin.starter.bakery.ui.converters;

import java.math.BigDecimal;
import java.text.NumberFormat;

import com.vaadin.flow.model.ModelConverter;
import com.vaadin.starter.bakery.ui.dataproviders.DataProviderUtil;

public class CurrencyFormatter implements ModelConverter<Integer, String> {

	@Override
	public String toPresentation(Integer modelValue) {
		return DataProviderUtil.convertIfNotNull(modelValue,
				v -> NumberFormat.getCurrencyInstance().format(BigDecimal.valueOf(v, 2)));
	}

	@Override
	public Integer toModel(String presentationValue) {
		// Not implemented
		throw new UnsupportedOperationException();
	}

}
