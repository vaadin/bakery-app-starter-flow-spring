package com.vaadin.starter.bakery.ui.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.SignStyle;
import java.time.format.TextStyle;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.HashMap;
import java.util.Map;

public class FormattingUtils {

	public static final String DECIMAL_ZERO = "0.00";

	/**
	 * 3 letter month name + day number E.g: Nov 20
	 */
	public static final DateTimeFormatter MONTH_AND_DAY_FORMATTER = DateTimeFormatter.ofPattern("MMM d",
			BakeryConst.APP_LOCALE);

	/**
	 * Full day name. E.g: Monday.
	 */
	public static final DateTimeFormatter WEEKDAY_FULLNAME_FORMATTER = DateTimeFormatter.ofPattern("EEEE",
			BakeryConst.APP_LOCALE);

	/**
	 * For getting the week of the year from the local date.
	 */
	public static final TemporalField WEEK_OF_YEAR_FIELD = WeekFields.of(BakeryConst.APP_LOCALE).weekOfWeekBasedYear();

	/**
	 * 3 letter day of the week + day number. E.g: Mon 20
	 */
	public static final DateTimeFormatter SHORT_DAY_FORMATTER = DateTimeFormatter.ofPattern("E d",
			BakeryConst.APP_LOCALE);

	/**
	 * Full date format. E.g: 03.03.2001
	 */
	public static final DateTimeFormatter FULL_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");
	/**
	 * Formats hours with am/pm. E.g: 2:00 p.m.
	 */
	public static final DateTimeFormatter HOUR_FORMATTER;

	static {
		Map<Long, String> ampm = new HashMap<>();
		ampm.put(0L, "a.m.");
		ampm.put(1L, "p.m.");
		HOUR_FORMATTER = new DateTimeFormatterBuilder().parseCaseInsensitive().parseLenient()
				.appendValue(java.time.temporal.ChronoField.CLOCK_HOUR_OF_AMPM, 1, 2, SignStyle.NOT_NEGATIVE)
				.appendLiteral(':').appendValue(java.time.temporal.ChronoField.MINUTE_OF_HOUR, 2).optionalStart()
				.appendLiteral(' ').optionalEnd().appendText(java.time.temporal.ChronoField.AMPM_OF_DAY, ampm)
				.toFormatter();
	}
	
	public static String getFullMonthName(LocalDate date) {
		return date.getMonth().getDisplayName(TextStyle.FULL, BakeryConst.APP_LOCALE);
	}

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
