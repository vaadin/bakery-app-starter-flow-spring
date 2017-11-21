package com.vaadin.starter.bakery.ui.utils;

import com.vaadin.starter.bakery.backend.data.entity.Order;
import com.vaadin.starter.bakery.backend.service.OrderService;
import com.vaadin.starter.bakery.ui.entities.StorefrontItemHeader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class StorefrontItemHeaderGenerator {

	private final DateTimeFormatter HEADER_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("EEE, MMM d");

	private final OrderService orderService;
	private final Map<Long, StorefrontItemHeader> ordersWithHeaders = new HashMap<>();

	private Sort sort;

	public StorefrontItemHeaderGenerator(OrderService orderService, String[] orderSortFields) {
		this.orderService = orderService;

		sort = new Sort(Sort.Direction.ASC, orderSortFields);
	}

	private StorefrontItemHeader getRecentHeader() {
		return new StorefrontItemHeader("Recent", "Before this week");
	}

	private StorefrontItemHeader getYesterdayHeader() {
		LocalDate yesterday = LocalDate.now().minusDays(1);
		return new StorefrontItemHeader("Yesterday", secondaryHeaderFor(yesterday));
	}

	private StorefrontItemHeader getTodayHeader() {
		LocalDate today = LocalDate.now();
		return new StorefrontItemHeader("Today", secondaryHeaderFor(today));
	}

	private StorefrontItemHeader getThisWeekBeforeYesterdayHeader() {
		LocalDate today = LocalDate.now();
		LocalDate yesterday = today.minusDays(1);
		LocalDate thisWeekStart = today.minusDays(today.getDayOfWeek().getValue() - 1);
		return new StorefrontItemHeader("This week before yesterday", secondaryHeaderFor(thisWeekStart, yesterday));
	}

	private StorefrontItemHeader getThisWeekStartingTomorrow(boolean showPrevious) {
		LocalDate today = LocalDate.now();
		LocalDate tomorrow = today.plusDays(1);
		LocalDate nextWeekStart = today.minusDays(today.getDayOfWeek().getValue()).plusWeeks(1);
		return new StorefrontItemHeader(showPrevious ? "This week starting tomorrow" : "This week",
				secondaryHeaderFor(tomorrow, nextWeekStart));
	}

	private StorefrontItemHeader getUpcomingHeader() {
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
			Long id = findFirstOrderAfterDueDate(filter, date, sort);
			if (id != null) {
				ordersWithHeaders.put(id, getYesterdayHeader());
			}

			date = date.minusDays(date.getDayOfWeek().getValue());
			id = findFirstOrderAfterDueDate(filter, date, sort);
			if (id != null) {
				ordersWithHeaders.put(id, getThisWeekBeforeYesterdayHeader());
			}

			id = findFirstOrderAfterDueDate(filter, null, sort);
			if (id != null) {
				ordersWithHeaders.put(id, getRecentHeader());
			}
		}

		LocalDate date = LocalDate.now().minusDays(1);
		Long id = findFirstOrderAfterDueDate(filter, date, sort);
		if (id != null) {
			ordersWithHeaders.put(id, getTodayHeader());
		}

		date = LocalDate.now();
		id = findFirstOrderAfterDueDate(filter, date, sort);
		if (id != null) {
			ordersWithHeaders.put(id, getThisWeekStartingTomorrow(showPrevious));
		}

		date = date.minusDays(date.getDayOfWeek().getValue()).plusWeeks(1);
		id = findFirstOrderAfterDueDate(filter, date, sort);
		if (id != null) {
			ordersWithHeaders.put(id, getUpcomingHeader());
		}
	}

	public Long findFirstOrderAfterDueDate(String filter, LocalDate date, Sort sort) {
		Page<Order> page = orderService
				.findAnyMatchingAfterDueDate(Optional.ofNullable(filter), Optional.ofNullable(date),
						new PageRequest(0, 1, sort));
		if (page.getContent().isEmpty()) {
			return null;
		}
		return page.getContent().get(0).getId();
	}


	public StorefrontItemHeader get(Long id) {
		return ordersWithHeaders.get(id);
	}
}
