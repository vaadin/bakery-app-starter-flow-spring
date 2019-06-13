/**
 *
 */
package com.vaadin.starter.bakery.ui.utils.converters;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalTime;

import org.junit.jupiter.api.Test;

import com.vaadin.starter.bakery.test.FormattingTest;

public class LocalTimeConverterTest extends FormattingTest {

	@Test
	public void formattingShoudBeLocaleIndependent() {
		LocalTimeConverter converter = new LocalTimeConverter();
		String result = converter.encode(LocalTime.of(13, 9, 33));
		assertEquals("1:09 PM", result);
	}
}
