/**
 *
 */
package com.vaadin.starter.bakery.ui.view.storefront.converter;

import java.time.LocalDate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.vaadin.starter.bakery.test.FormattingTest;
import com.vaadin.starter.bakery.ui.views.storefront.converters.StorefrontDate;
import com.vaadin.starter.bakery.ui.views.storefront.converters.StorefrontLocalDateConverter;

public class StorefrontLocalDateConverterTest extends FormattingTest {

	@Test
	public void formattingShoudBeLocaleIndependent() {
		StorefrontLocalDateConverter converter = new StorefrontLocalDateConverter();
		StorefrontDate result = converter.encode(LocalDate.of(2017, 8, 22));
		Assertions.assertEquals("Aug 22", result.getDay());
		Assertions.assertEquals("2017-08-22", result.getDate());
		Assertions.assertEquals("Tuesday", result.getWeekday());
	}
}
