package com.vaadin.flow.demo.patientportal.ui.dataproviders;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.vaadin.flow.demo.patientportal.app.BeanLocator;
import com.vaadin.flow.demo.patientportal.backend.service.OrderService;
import com.vaadin.flow.demo.patientportal.ui.entities.Customer;
import com.vaadin.flow.demo.patientportal.ui.entities.Order;

public class OrdersDataProvider {

	@Autowired
	private OrderService orderService;

	private LocalDate filterDate = LocalDate.now().minusDays(1);

	private static OrdersDataProvider provider;

	public static OrdersDataProvider get() {
		if (provider == null) {
			provider = new OrdersDataProvider();
		}
		return provider;
	}

	public List<Order> getOrdersList(int start, int count) {
		List<Order> result = getOrdersList();
		if (start > result.size())
			return Collections.emptyList();

		if (start + count > result.size())
			return result.subList(start, result.size() - 1);

		return result.subList(start, start + count);
	}

	public List<Order> getOrdersList() {
		List<Order> list = new ArrayList<>();
		fetchFromBackEnd(null).forEach(entityOrder -> list.add(new Order(entityOrder.getState().getDisplayName(),
				entityOrder.getDueDate(), entityOrder.getPickupLocation().getName(),
				new Customer(entityOrder.getCustomer().getFullName()), Collections.emptyList())));

		return list;

	}

	protected Page<com.vaadin.flow.demo.patientportal.backend.data.entity.Order> fetchFromBackEnd(Pageable pageable) {
		return getOrderService().findAnyMatchingAfterDueDate(Optional.empty(), getOptionalFilterDate(), pageable);
	}

	private Optional<LocalDate> getOptionalFilterDate() {
		if (filterDate == null) {
			return Optional.empty();
		} else {
			return Optional.of(filterDate);
		}
	}

	public void setIncludePast(boolean includePast) {
		if (includePast) {
			filterDate = null;
		} else {
			filterDate = LocalDate.now().minusDays(1);
		}
	}

	protected OrderService getOrderService() {
		if (orderService == null) {
			orderService = BeanLocator.find(OrderService.class);
		}
		return orderService;
	}

}
