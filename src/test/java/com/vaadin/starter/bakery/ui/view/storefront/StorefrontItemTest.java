package com.vaadin.starter.bakery.ui.view.storefront;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.time.LocalDate;

import org.junit.Test;

import com.vaadin.starter.bakery.test.FormattingTest;

public class StorefrontItemTest extends FormattingTest {

	@Test
	public void shortDayFormatterShouldBeLocaleIndependent() {

		String result = StorefrontItem.SHORT_DAY_FORMATTER.format(LocalDate.of(2017, 11, 13));
		assertEquals("Mon 13", result);
	}

	@Test
	public void fullDayFormatterShouldBeLocaleIndependent() {

		String result = StorefrontItem.FULL_DAY_FORMATTER.format(LocalDate.of(2017, 10, 13));
		assertEquals("Friday", result);
	}

	@Test
	public void monthAndDayFormatterShouldBeLocaleIndependent() {

		String result = StorefrontItem.MONTH_AND_DAY_FORMATTER.format(LocalDate.of(2015, 6, 26));
		assertEquals("Jun 26", result);
	}

	@Test
	public void weekNumberShouldBeLocaleIndependent() {
		assertEquals(getWeek(2017, 9, 3), getWeek(2017, 9, 9));
		assertNotEquals(getWeek(2017, 9, 2), getWeek(2017, 9, 4));
		assertNotEquals(getWeek(2017, 9, 8), getWeek(2017, 9, 10));
	}

	private int getWeek(int year, int month, int day) {
		return LocalDate.of(year, month, day).get(StorefrontItem.WEEK_OF_YEAR_FIELD);
	}
}
