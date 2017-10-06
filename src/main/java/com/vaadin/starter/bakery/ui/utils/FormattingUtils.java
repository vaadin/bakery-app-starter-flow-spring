package com.vaadin.starter.bakery.ui.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

public class FormattingUtils {

	public static final String DECIMAL_ZERO = "0.00";

	public static String formatAsCurrency(int valueInCents) {
		return NumberFormat.getCurrencyInstance(Locale.US).format(BigDecimal.valueOf(valueInCents, 2));
	}

	public static DecimalFormat getUiPriceFormatter() {
		DecimalFormatSymbols sep = new DecimalFormatSymbols();
		sep.setDecimalSeparator('.');
		return new DecimalFormat("#" + DECIMAL_ZERO, sep);
	}

}
