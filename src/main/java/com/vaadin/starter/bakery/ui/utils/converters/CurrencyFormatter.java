package com.vaadin.starter.bakery.ui.utils.converters;

import java.io.Serializable;

import com.vaadin.starter.bakery.ui.dataproviders.DataProviderUtil;
import com.vaadin.starter.bakery.ui.utils.FormattingUtils;

public class CurrencyFormatter implements Serializable  {
	public String encode(Integer modelValue) {
		return DataProviderUtil.convertIfNotNull(modelValue, FormattingUtils::formatAsCurrency);
	}
}
