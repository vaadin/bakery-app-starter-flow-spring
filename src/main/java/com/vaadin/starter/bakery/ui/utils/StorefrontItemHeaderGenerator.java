package com.vaadin.starter.bakery.ui.utils;

import com.vaadin.starter.bakery.ui.entities.StorefrontItemHeader;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class StorefrontItemHeaderGenerator {

	private static final DateTimeFormatter HEADER_DATE_TIME_FORMATTER;

	static {
		HEADER_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("EEE, MMM d");
	}

	public static StorefrontItemHeader getRecentHeader() {
		return new StorefrontItemHeader("Recent", "Before this week");
	}

	public static StorefrontItemHeader getYesterdayHeader() {
		LocalDate yesterday = LocalDate.now().minusDays(1);
		return new StorefrontItemHeader("Yesterday", secondaryHeaderFor(yesterday));
	}

	public static StorefrontItemHeader getTodayHeader() {
		LocalDate today = LocalDate.now();
		return new StorefrontItemHeader("Today", secondaryHeaderFor(today));
	}

	public static StorefrontItemHeader getThisWeekBeforeYesterdayHeader() {
		LocalDate today = LocalDate.now();
		LocalDate yesterday = today.minusDays(1);
		LocalDate thisWeekStart = today.minusDays(today.getDayOfWeek().getValue() - 1);
		return new StorefrontItemHeader("This week before yesterday", secondaryHeaderFor(thisWeekStart, yesterday));
	}

	public static StorefrontItemHeader getThisWeekStartingTomorrow(boolean showPrevious) {
		LocalDate today = LocalDate.now();
		LocalDate tomorrow = today.plusDays(1);
		LocalDate nextWeekStart = today.minusDays(today.getDayOfWeek().getValue()).plusWeeks(1);
		return new StorefrontItemHeader(showPrevious ? "This week starting tomorrow" : "This week",
				secondaryHeaderFor(tomorrow, nextWeekStart));
	}

	public static StorefrontItemHeader getUpcomingHeader() {
		return new StorefrontItemHeader("Upcoming", "After this week");
	}

	private static String secondaryHeaderFor(LocalDate date) {
		return HEADER_DATE_TIME_FORMATTER.format(date);
	}

	private static String secondaryHeaderFor(LocalDate start, LocalDate end) {
		return secondaryHeaderFor(start) + " - " + secondaryHeaderFor(end);
	}
}
