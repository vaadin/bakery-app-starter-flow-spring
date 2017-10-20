package com.vaadin.starter.bakery.ui.utils;

import com.vaadin.starter.bakery.ui.entities.StorefrontItemHeader;
import elemental.json.JsonObject;
import elemental.json.impl.JreJsonFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;

public class StorefrontItemHeaderGenerator {

	private static final JreJsonFactory JSON_FACTORY;
	private static final DateTimeFormatter MODEL_DATE_TIME_FORMATTER;
	private static final DateTimeFormatter HEADER_DATE_TIME_FORMATTER;
	private static final List<BiFunction<LocalDate, Boolean, Optional<StorefrontItemHeader>>> HEADER_FUNCTIONS;

	static {
		JSON_FACTORY = new JreJsonFactory();
		MODEL_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		HEADER_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("EEE, MMM d");
		HEADER_FUNCTIONS = Arrays.asList(
				(date, showPrevious) -> headerIfRecent(date),
				(date, showPrevious) -> headerIfYesterday(date),
				(date, showPrevious) -> headerIfToday(date),
				(date, showPrevious) -> headerIfThisWeekBeforeYesterday(date),
				(date, showPrevious) -> headerIfThisWeekStartingTomorrow(date, showPrevious),
				(date, showPrevious) -> headerIfUpcoming(date));
	}

	public static JsonObject computeEntriesWithHeader(List<com.vaadin.starter.bakery.backend.data.entity.Order> orders, boolean showPrevious) {
		Map<String, StorefrontItemHeader> result = new HashMap<>(HEADER_FUNCTIONS.size());
		boolean[] usedGroups = new boolean[HEADER_FUNCTIONS.size()];
		ordersLoop: for (com.vaadin.starter.bakery.backend.data.entity.Order order : orders) {
			for (int i = 0; i < HEADER_FUNCTIONS.size(); i++) {
				Optional<StorefrontItemHeader> header
						= HEADER_FUNCTIONS.get(i).apply(order.getDueDate(), showPrevious);
				if (!usedGroups[i] && header.isPresent()) {
					usedGroups[i] = true;
					result.put(order.getId().toString(), header.get());
					if (i == usedGroups.length - 1) {
						break ordersLoop;
					}
					break;
				}
			}
		}

		return toJsonObject(result);
	}

	private static JsonObject toJsonObject(Map<String, StorefrontItemHeader> map) {
		JsonObject result = JSON_FACTORY.createObject();
		map.forEach((k, v) -> {
			JsonObject value = JSON_FACTORY.createObject();
			value.put("main", v.getMain());
			value.put("secondary", v.getSecondary());
			result.put(k, value);
		});
		return result;
	}

	private static Optional<StorefrontItemHeader> headerIfRecent(LocalDate date) {
		LocalDate today = LocalDate.now();
		LocalDate thisWeekStart = today.minusDays(today.getDayOfWeek().getValue() - 1);
		return Optional.ofNullable(date.isBefore(thisWeekStart) ?
				new StorefrontItemHeader("Recent", "Before this week") : null);
	}

	private static Optional<StorefrontItemHeader> headerIfYesterday(LocalDate date) {
		LocalDate yesterday = LocalDate.now().minusDays(1);
		return Optional.ofNullable(date.equals(yesterday) ?
				new StorefrontItemHeader("Yesterday", secondaryHeaderFor(yesterday)) : null);
	}

	private static Optional<StorefrontItemHeader> headerIfToday(LocalDate date) {
		LocalDate today = LocalDate.now();
		return Optional.ofNullable(date.equals(today) ?
				new StorefrontItemHeader("Today", secondaryHeaderFor(today)) : null);
	}

	private static Optional<StorefrontItemHeader> headerIfThisWeekBeforeYesterday(LocalDate date) {
		LocalDate today = LocalDate.now();
		LocalDate yesterday = today.minusDays(1);
		LocalDate thisWeekStart = today.minusDays(today.getDayOfWeek().getValue() - 1);
		return Optional.ofNullable((date.equals(thisWeekStart) || date.isAfter(thisWeekStart))
				&& date.isBefore(yesterday) ?
				new StorefrontItemHeader("This week before yesterday",
						secondaryHeaderFor(thisWeekStart, yesterday)) : null);
	}

	private static Optional<StorefrontItemHeader> headerIfThisWeekStartingTomorrow(
			LocalDate date, boolean showPrevious) {

		LocalDate today = LocalDate.now();
		LocalDate tomorrow = today.plusDays(1);
		LocalDate nextWeekStart = today.minusDays(today.getDayOfWeek().getValue() - 1).plusWeeks(1);
		return Optional.ofNullable((date.equals(tomorrow) || date.isAfter(tomorrow))
				&& date.isBefore(nextWeekStart) ?
				new StorefrontItemHeader(showPrevious ? "This week starting tomorrow" : "This week",
						secondaryHeaderFor(tomorrow, nextWeekStart)) : null);
	}

	private static Optional<StorefrontItemHeader> headerIfUpcoming(LocalDate date) {
		LocalDate today = LocalDate.now();
		LocalDate nextWeekStart = today.minusDays(today.getDayOfWeek().getValue() - 1).plusWeeks(1);
		return Optional.ofNullable(date.isEqual(nextWeekStart) || date.isAfter(nextWeekStart) ?
				new StorefrontItemHeader("Upcoming", "After this week") : null);
	}

	private static LocalDate toDate(String date) {
		return LocalDate.parse(date, MODEL_DATE_TIME_FORMATTER);
	}

	private static String secondaryHeaderFor(LocalDate date) {
		return HEADER_DATE_TIME_FORMATTER.format(date);
	}

	private static String secondaryHeaderFor(LocalDate start, LocalDate end) {
		return secondaryHeaderFor(start) + " - " + secondaryHeaderFor(end);
	}
}
