package com.vaadin.starter.bakery.ui.dataproviders;

import java.time.LocalDate;
import java.time.MonthDay;
import java.time.Year;
import java.util.List;
import java.util.Optional;

import com.vaadin.starter.bakery.backend.data.entity.Order;
import com.vaadin.starter.bakery.ui.utils.OrderFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.vaadin.starter.bakery.backend.data.DashboardData;
import com.vaadin.starter.bakery.backend.service.OrderService;

@Service
public class OrdersDataProvider {

	private final OrderService orderService;

	@Autowired
	public OrdersDataProvider(OrderService orderService) {
		this.orderService = orderService;
	}

	public long countAnyMatchingAfterDueDate(OrderFilter filter) {
		return getOrderService()
				.countAnyMatchingAfterDueDate(Optional.of(filter.getFilter()), getFilterDate(filter.isShowPrevious()));
	}

	public List<Order> getOriginalOrdersList() {
		return fetchFromBackEnd(null, false, null).getContent();
	}

	public DashboardData getDashboardData() {
		return orderService.getDashboardData(MonthDay.now().getMonthValue(), Year.now().getValue());
	}

	public Page<Order> fetchFromBackEnd(OrderFilter filter, Pageable pageable) {
		return fetchFromBackEnd(filter.getFilter(), filter.isShowPrevious(), pageable);
	}

	public Page<Order> fetchFromBackEnd(String filter, boolean showPrevious, Pageable pageable) {
		return getOrderService()
				.findAnyMatchingAfterDueDate(Optional.ofNullable(filter), getFilterDate(showPrevious), pageable);
	}

	private Optional<LocalDate> getFilterDate(boolean showPrevious) {
		if (showPrevious) {
			return Optional.empty();
		}

		return Optional.of(LocalDate.now().minusDays(1));
	}

	public Long findFirstOrderAfterDueDate(String filter, LocalDate date, Sort sort) {
		Page<Order> page = getOrderService().findAnyMatchingAfterDueDate(Optional.ofNullable(filter), Optional.ofNullable(date), new PageRequest(0, 1, sort));
		if (page.getContent().isEmpty()) {
			return null;
		}
		return page.getContent().get(0).getId();
	}

	private OrderService getOrderService() {
		return orderService;
	}

}
