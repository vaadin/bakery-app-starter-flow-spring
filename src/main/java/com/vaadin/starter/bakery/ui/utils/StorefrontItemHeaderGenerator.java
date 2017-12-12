package com.vaadin.starter.bakery.ui.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import com.vaadin.starter.bakery.backend.data.entity.Order;
import com.vaadin.starter.bakery.ui.entities.StorefrontItemHeader;

public class StorefrontItemHeaderGenerator {

	private final DateTimeFormatter HEADER_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("EEE, MMM d");

	private final Map<Long, StorefrontItemHeader> ordersWithHeaders = new HashMap<>();
	private List<HeaderGenerator> headerChain = new ArrayList<>();

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

	public StorefrontItemHeader get(Long id) {
		return ordersWithHeaders.get(id);
	}

	public void setShowPrevious(boolean showPrevious) {
		this.headerChain = createHeaderChain(showPrevious);
		ordersWithHeaders.clear();
	}

	public void ordersRead(List<Order> orders) {
		Iterator<HeaderGenerator> headerIterator = headerChain.stream().filter(h -> h.getSelected() == null).iterator();
		if(!headerIterator.hasNext()) 
			return;
		HeaderGenerator current = headerIterator.next();
		for (Order order : orders) {
			// If last selected, discard orders that match it.
			if (current.getSelected() != null && current.matches(order.getDueDate()))
				continue;
			while (current != null && !current.matches(order.getDueDate())) {
				current = headerIterator.hasNext() ? headerIterator.next() : null;
			}
			if (current == null)
				break;
			current.setSelected(order.getId());
			ordersWithHeaders.put(order.getId(), current.getHeader());
		}
	}

	private List<HeaderGenerator> createHeaderChain(boolean showPrevious) {
		List<HeaderGenerator> headerChain = new ArrayList<>();
		LocalDate today = LocalDate.now();
		LocalDate startOfTheWeek = today.minusDays(today.getDayOfWeek().getValue() - 1);
		if (showPrevious) {
			LocalDate yesterday = today.minusDays(1);
			// Week starting on Monday
			headerChain.add(new HeaderGenerator(d -> d.isBefore(startOfTheWeek), this.getRecentHeader()));
			if (startOfTheWeek.isBefore(yesterday)) {
				headerChain.add(new HeaderGenerator(d -> d.isBefore(yesterday) && !d.isAfter(startOfTheWeek),
						this.getThisWeekBeforeYesterdayHeader()));
			}
			headerChain.add(new HeaderGenerator(yesterday::equals, this.getYesterdayHeader()));
		}
		LocalDate firstDayOfTheNextWeek = startOfTheWeek.plusDays(7);
		headerChain.add(new HeaderGenerator(today::equals, getTodayHeader()));
		headerChain.add(new HeaderGenerator(d -> d.isAfter(today) && d.isBefore(firstDayOfTheNextWeek),
				getThisWeekStartingTomorrow(showPrevious)));
		headerChain.add(new HeaderGenerator(d -> !d.isBefore(firstDayOfTheNextWeek), getUpcomingHeader()));
		return headerChain;
	}
}

class HeaderGenerator {
	private Predicate<LocalDate> matcher;

	private StorefrontItemHeader header;

	private Long selected;

	public HeaderGenerator(Predicate<LocalDate> matcher, StorefrontItemHeader header) {
		this.matcher = matcher;
		this.header = header;
	}

	public boolean matches(LocalDate date) {
		return matcher.test(date);
	}

	public Long getSelected() {
		return selected;
	}

	public void setSelected(Long selected) {
		this.selected = selected;
	}

	public StorefrontItemHeader getHeader() {
		return header;
	}

}