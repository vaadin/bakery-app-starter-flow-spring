package com.vaadin.starter.bakery.ui.utils;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class FormattingUtils {

	public static String formatAsCurrency(int i) {
		return NumberFormat.getCurrencyInstance(Locale.US).format(BigDecimal.valueOf(i,2));
	}

}
