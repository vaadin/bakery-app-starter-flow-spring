/*
 * Copyright 2000-2017 Vaadin Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.vaadin.starter.bakery.ui.utils;

import com.vaadin.starter.bakery.ui.entities.Order;
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

/**
 * @author Vaadin Ltd
 *
 */
public class StorefrontItemHeaderGenerator {

	private static final JreJsonFactory JSON_FACTORY;
	private static final DateTimeFormatter MODEL_DATE_TIME_FORMATTER;
	private static final DateTimeFormatter HEADER_DATE_TIME_FORMATTER;
	private static final List<BiFunction<String, Boolean, Optional<StorefrontItemHeader>>> HEADER_FUNCTIONS;

	static {
		JSON_FACTORY = new JreJsonFactory();
		MODEL_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		HEADER_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("EEE, MMM d");
		HEADER_FUNCTIONS = Arrays.asList(
				StorefrontItemHeaderGenerator::headerIfRecent,
				StorefrontItemHeaderGenerator::headerIfYesterday,
				StorefrontItemHeaderGenerator::headerIfToday,
				StorefrontItemHeaderGenerator::headerIfThisWeekBeforeYesterday,
				StorefrontItemHeaderGenerator::headerIfThisWeekStartingTomorrow,
				StorefrontItemHeaderGenerator::headerIfUpcoming);
	}

	public static JsonObject computeEntriesWithHeader(List<Order> orders, boolean showPrevious) {
		Map<String, StorefrontItemHeader> result = new HashMap<>(HEADER_FUNCTIONS.size());
		boolean[] usedGroups = new boolean[HEADER_FUNCTIONS.size()];
		int used = 0;
		ordersLoop: for (Order order : orders) {
			for (int i = 0; i < HEADER_FUNCTIONS.size(); i++) {
				Optional<StorefrontItemHeader> header = HEADER_FUNCTIONS.get(i).apply(order.getDate(), showPrevious);
				if (!usedGroups[i] && header.isPresent()) {
					usedGroups[i] = true;
					result.put(order.getId(), header.get());
					if (++used == HEADER_FUNCTIONS.size()) {
						break ordersLoop;
					}
					break;
				}
			}
		}

		return toJsonObject(result);
	}

	// TODO(sayo-vaadin): Remove when Flow supports Map property type.
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

	private static Optional<StorefrontItemHeader> headerIfRecent(String date, boolean showPrevious) {
		LocalDate today = LocalDate.now();
		LocalDate thisWeekStart = today.minusDays(today.getDayOfWeek().getValue() - 1);
		return Optional.ofNullable(toDate(date).isBefore(thisWeekStart) ?
				new StorefrontItemHeader("Recent", "Before this week") : null);
	}

	private static Optional<StorefrontItemHeader> headerIfYesterday(String date, boolean showPrevious) {
		LocalDate yesterday = LocalDate.now().minusDays(1);
		return Optional.ofNullable(toDate(date).equals(yesterday) ?
				new StorefrontItemHeader("Yesterday", secondaryHeaderFor(yesterday)) : null);
	}

	private static Optional<StorefrontItemHeader> headerIfToday(String date, boolean showPrevious) {
		LocalDate today = LocalDate.now();
		return Optional.ofNullable(toDate(date).equals(today) ?
				new StorefrontItemHeader("Today", secondaryHeaderFor(today)) : null);
	}

	private static Optional<StorefrontItemHeader> headerIfThisWeekBeforeYesterday(String date, boolean showPrevious) {
		LocalDate today = LocalDate.now();
		LocalDate yesterday = today.minusDays(1);
		LocalDate thisWeekStart = today.minusDays(today.getDayOfWeek().getValue() - 1);
		LocalDate currentDate = toDate(date);
		return Optional.ofNullable(currentDate.isAfter(thisWeekStart) && currentDate.isBefore(yesterday) ?
				new StorefrontItemHeader("This week before yesterday",
						secondaryHeaderFor(thisWeekStart, yesterday)) : null);
	}

	private static Optional<StorefrontItemHeader> headerIfThisWeekStartingTomorrow(String date, boolean showPrevious) {
		LocalDate today = LocalDate.now();
		LocalDate tomorrow = today.plusDays(1);
		LocalDate nextWeekStart = today.minusDays(today.getDayOfWeek().getValue() - 1).plusWeeks(1);
		LocalDate currentDate = toDate(date);
		return Optional.ofNullable(currentDate.isAfter(tomorrow) && currentDate.isBefore(nextWeekStart) ?
				new StorefrontItemHeader(showPrevious ? "This week starting tomorrow" : "This week",
						secondaryHeaderFor(tomorrow, nextWeekStart)) : null);
	}

	private static Optional<StorefrontItemHeader> headerIfUpcoming(String date, boolean showPrevious) {
		LocalDate today = LocalDate.now();
		LocalDate nextWeekStart = today.minusDays(today.getDayOfWeek().getValue() - 1).plusWeeks(1);;
		final LocalDate currentDate = toDate(date);
		return Optional.ofNullable(currentDate.isEqual(nextWeekStart) || currentDate.isAfter(nextWeekStart) ?
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
