/**
 *
 */
package com.vaadin.starter.bakery.ui.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.Test;

import com.vaadin.starter.bakery.test.FormattingTest;

public class FormattingUtilsTest extends FormattingTest {

	@Test
	public void formatAsCurrencyShoudBeLocaleIndependent() {
		String result = FormattingUtils.formatAsCurrency(987654345);
		assertEquals("$9,876,543.45", result);
	}

	@Test
	public void getUiPriceFormatterShoudBeLocaleIndependent() {
		String result = FormattingUtils.getUiPriceFormatter().format(9876543);
		assertEquals("9876543.00", result);
	}

	@Test
	public void shortDayFormatterShouldBeLocaleIndependent() {
		String result = FormattingUtils.SHORT_DAY_FORMATTER.format(LocalDate.of(2017, 11, 13));
		assertEquals("Mon 13", result);
	}

	@Test
	public void weekdayFullDayFormatterShouldBeLocaleIndependent() {
		String result = FormattingUtils.WEEKDAY_FULLNAME_FORMATTER.format(LocalDate.of(2017, 10, 13));
		assertEquals("Friday", result);
	}

	@Test
	public void monthAndDayFormatterShouldBeLocaleIndependent() {
		String result = FormattingUtils.MONTH_AND_DAY_FORMATTER.format(LocalDate.of(2015, 6, 26));
		assertEquals("Jun 26", result);
	}

	@Test
	public void weekNumberShouldBeLocaleIndependent() {
		assertEquals(getWeek(2017, 9, 3), getWeek(2017, 9, 9));
		assertNotEquals(getWeek(2017, 9, 2), getWeek(2017, 9, 4));
		assertNotEquals(getWeek(2017, 9, 8), getWeek(2017, 9, 10));
	}

	@Test
	public void fullDateformatterShoudBeLocaleIndependent() {
		String result = FormattingUtils.FULL_DATE_FORMATTER.format(LocalDateTime.of(2016, 11, 27, 22, 15, 33));
		assertEquals("27.11.2016", result);
	}

	private int getWeek(int year, int month, int day) {
		return LocalDate.of(year, month, day).get(FormattingUtils.WEEK_OF_YEAR_FIELD);
	}
}
