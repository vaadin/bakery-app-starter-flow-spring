package com.vaadin.starter.bakery.ui.components.storefront.converter;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.vaadin.flow.model.ModelConverter;
import com.vaadin.starter.bakery.ui.components.storefront.converter.StorefrontLocalDateConverter.StorefrontDate;

/**
 * Date converter specific for the way date is displayed in storefront.
 */
public class StorefrontLocalDateConverter implements ModelConverter<LocalDate, StorefrontDate> {

	private static final DateTimeFormatter DAY_FORMATTER = DateTimeFormatter.ofPattern("LLL d");
	private static final DateTimeFormatter WEEKDAY_FORMATTER = DateTimeFormatter.ofPattern("EEE");

	@Override
	public StorefrontDate toPresentation(LocalDate modelValue) {
		StorefrontDate result = null;
		if (modelValue != null) {
			result = new StorefrontDate();
			result.day = DAY_FORMATTER.format(modelValue);
			result.weekday = WEEKDAY_FORMATTER.format(modelValue);
			result.year = modelValue.getYear();
		}
		return result;
	}

	@Override
	public LocalDate toModel(StorefrontDate presentationValue) {
		// TODO See if necessary.
		throw new UnsupportedOperationException();
	}

	public static class StorefrontDate implements Serializable {
		private String day;
		private String weekday;
		private int year;

		public String getDay() {
			return day;
		}

		public void setDay(String day) {
			this.day = day;
		}

		public String getWeekday() {
			return weekday;
		}

		public void setWeekday(String weekday) {
			this.weekday = weekday;
		}

		public int getYear() {
			return year;
		}

		public void setYear(int year) {
			this.year = year;
		}

	}
}
