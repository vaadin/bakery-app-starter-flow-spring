package com.vaadin.starter.bakery.ui.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;

public class FormattingUtils {

	public static final String DECIMAL_ZERO = "0.00";

	public static String formatAsCurrency(int valueInCents) {
		return NumberFormat.getCurrencyInstance(BakeryConst.APP_LOCALE).format(BigDecimal.valueOf(valueInCents, 2));
	}

	public static DecimalFormat getUiPriceFormatter() {
		DecimalFormat formatter = new DecimalFormat("#" + DECIMAL_ZERO,
				DecimalFormatSymbols.getInstance(BakeryConst.APP_LOCALE));
		formatter.setGroupingUsed(false);
		return formatter;
	}
}
