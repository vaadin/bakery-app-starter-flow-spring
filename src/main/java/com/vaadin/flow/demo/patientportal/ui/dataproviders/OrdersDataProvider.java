package com.vaadin.flow.demo.patientportal.ui.dataproviders;

import java.time.LocalDate;
import java.time.MonthDay;
import java.time.Year;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.vaadin.flow.demo.patientportal.backend.data.DashboardData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.vaadin.flow.demo.patientportal.app.BeanLocator;
import com.vaadin.flow.demo.patientportal.backend.service.OrderService;
import com.vaadin.flow.demo.patientportal.ui.entities.Customer;
import com.vaadin.flow.demo.patientportal.ui.entities.Good;
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
		fetchFromBackEnd(null).forEach(entityOrder -> list.add(getConvertedOrder(entityOrder)));

		return list;

	}

	public DashboardData getDashboardData() {
		return orderService.getDashboardData(MonthDay.now().getDayOfMonth(), Year.now().getValue());
	}

	private Order getConvertedOrder(com.vaadin.flow.demo.patientportal.backend.data.entity.Order entityOrder) {
		if (entityOrder == null)
			return null;
		Order result = new Order();
		result.setCustomer(new Customer(entityOrder.getCustomer().getFullName()));
		result.setDate(entityOrder.getDueDate().toString());
		List<Good> goods = new ArrayList<>();

		entityOrder.getItems().stream()
				.forEach(item -> goods.add(new Good(item.getQuantity(), item.getProduct().getName())));
		result.setGoods(goods);
		result.setPlace(entityOrder.getPickupLocation().getName());
		result.setStatus(entityOrder.getState().getDisplayName());

		return result;

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
