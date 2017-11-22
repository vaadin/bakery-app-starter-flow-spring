/**
 *
 */
package com.vaadin.starter.bakery.ui.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.vaadin.starter.bakery.test.FormattingTest;

public class FormattingUtilsTest extends FormattingTest {

	@Test
	public void formatAsCurrencyShoudBeLocaleIndependent() {
		String result = FormattingUtils.formatAsCurrency(987654345);
		assertEquals("$9,876,543.45", result);
	}

	@Test
	public void getUiPriceFormatterShoudReturnALocaleIndependent() {
		String result = FormattingUtils.getUiPriceFormatter().format(9876543);
		assertEquals("9876543.00", result);
	}
}
