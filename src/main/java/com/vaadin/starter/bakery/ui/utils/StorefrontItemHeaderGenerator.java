package com.vaadin.starter.bakery.ui.utils;

import com.vaadin.starter.bakery.ui.dataproviders.OrdersDataProvider;
import com.vaadin.starter.bakery.ui.entities.StorefrontItemHeader;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class StorefrontItemHeaderGenerator {

	private final DateTimeFormatter HEADER_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("EEE, MMM d");

	private final OrdersDataProvider ordersProvider;
	private final Map<Long, StorefrontItemHeader> ordersWithHeaders = new HashMap<>();

	private Sort sort;

	public StorefrontItemHeaderGenerator(OrdersDataProvider ordersProvider, String[] orderSortFields) {
		this.ordersProvider = ordersProvider;

		sort = new Sort(Sort.Direction.ASC, orderSortFields);
	}

	public StorefrontItemHeader getRecentHeader() {
		return new StorefrontItemHeader("Recent", "Before this week");
	}

	public StorefrontItemHeader getYesterdayHeader() {
		LocalDate yesterday = LocalDate.now().minusDays(1);
		return new StorefrontItemHeader("Yesterday", secondaryHeaderFor(yesterday));
	}

	public StorefrontItemHeader getTodayHeader() {
		LocalDate today = LocalDate.now();
		return new StorefrontItemHeader("Today", secondaryHeaderFor(today));
	}

	public StorefrontItemHeader getThisWeekBeforeYesterdayHeader() {
		LocalDate today = LocalDate.now();
		LocalDate yesterday = today.minusDays(1);
		LocalDate thisWeekStart = today.minusDays(today.getDayOfWeek().getValue() - 1);
		return new StorefrontItemHeader("This week before yesterday", secondaryHeaderFor(thisWeekStart, yesterday));
	}

	public StorefrontItemHeader getThisWeekStartingTomorrow(boolean showPrevious) {
		LocalDate today = LocalDate.now();
		LocalDate tomorrow = today.plusDays(1);
		LocalDate nextWeekStart = today.minusDays(today.getDayOfWeek().getValue()).plusWeeks(1);
		return new StorefrontItemHeader(showPrevious ? "This week starting tomorrow" : "This week",
				secondaryHeaderFor(tomorrow, nextWeekStart));
	}

	public StorefrontItemHeader getUpcomingHeader() {
		return new StorefrontItemHeader("Upcoming", "After this week");
	}

	private String secondaryHeaderFor(LocalDate date) {
		return HEADER_DATE_TIME_FORMATTER.format(date);
	}

	private String secondaryHeaderFor(LocalDate start, LocalDate end) {
		return secondaryHeaderFor(start) + " - " + secondaryHeaderFor(end);
	}

	public void updateHeaders(String filter, boolean showPrevious) {
		ordersWithHeaders.clear();

		if (showPrevious) {
			LocalDate date = LocalDate.now().minusDays(2);
			Long id = ordersProvider.findFirstOrderAfterDueDate(filter, date, sort);
			if (id != null) {
				ordersWithHeaders.put(id, getYesterdayHeader());
			}

			date = date.minusDays(date.getDayOfWeek().getValue());
			id = ordersProvider.findFirstOrderAfterDueDate(filter, date, sort);
			if (id != null) {
				ordersWithHeaders.put(id, getThisWeekBeforeYesterdayHeader());
			}

			id = ordersProvider.findFirstOrderAfterDueDate(filter, null, sort);
			if (id != null) {
				ordersWithHeaders.put(id, getRecentHeader());
			}
		}

		LocalDate date = LocalDate.now().minusDays(1);
		Long id = ordersProvider.findFirstOrderAfterDueDate(filter, date, sort);
		if (id != null) {
			ordersWithHeaders.put(id, getTodayHeader());
		}

		date = LocalDate.now();
		id = ordersProvider.findFirstOrderAfterDueDate(filter, date, sort);
		if (id != null) {
			ordersWithHeaders.put(id, getThisWeekStartingTomorrow(showPrevious));
		}

		date = date.minusDays(date.getDayOfWeek().getValue()).plusWeeks(1);
		id = ordersProvider.findFirstOrderAfterDueDate(filter, date, sort);
		if (id != null) {
			ordersWithHeaders.put(id, getUpcomingHeader());
		}
	}

	public StorefrontItemHeader get(Long id) {
		return ordersWithHeaders.get(id);
	}
}
