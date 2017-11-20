package com.vaadin.starter.bakery.ui.utils;

import java.time.LocalDate;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.Locale;

public final class DateTimeUtils {

	private static final TemporalField weekOfYear = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();

	public static boolean isSameWeek(LocalDate date1, LocalDate date2) {
		return date1.get(weekOfYear) == date2.get(weekOfYear);
	}
}
