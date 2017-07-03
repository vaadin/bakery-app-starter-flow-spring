package com.vaadin.flow.demo.patientportal.backend.service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.vaadin.flow.demo.patientportal.app.BeanLocator;
import com.vaadin.flow.demo.patientportal.repositories.CustomerRepository;
import com.vaadin.flow.demo.patientportal.repositories.OrderRepository;
import com.vaadin.flow.demo.patientportal.backend.data.DashboardData;
import com.vaadin.flow.demo.patientportal.backend.data.DeliveryStats;
import com.vaadin.flow.demo.patientportal.backend.data.OrderState;
import com.vaadin.flow.demo.patientportal.backend.data.entity.Customer;
import com.vaadin.flow.demo.patientportal.backend.data.entity.HistoryItem;
import com.vaadin.flow.demo.patientportal.backend.data.entity.Order;
import com.vaadin.flow.demo.patientportal.backend.data.entity.Product;

@Service
public class OrderService {

	private OrderRepository orderRepository;

	private CustomerRepository customerRepository;

	private UserService userService;

	private static Set<OrderState> notAvailableStates;

	static {
		notAvailableStates = new HashSet<>(Arrays.asList(OrderState.values()));
		notAvailableStates.remove(OrderState.DELIVERED);
		notAvailableStates.remove(OrderState.READY);
		notAvailableStates.remove(OrderState.CANCELLED);
	}

	public Order findOrder(Long id) {
		return getOrderRepository().findOne(id);
	}

	public Order changeState(Order order, OrderState state) {
		if (order.getState() == state) {
			throw new IllegalArgumentException("Order state is already " + state);
		}
		order.setState(state);
		addHistoryItem(order, state);

		return getOrderRepository().save(order);
	}

	private void addHistoryItem(Order order, OrderState newState) {
		String comment = "Order " + newState.getDisplayName();

		HistoryItem item = new HistoryItem(getUserService().getCurrentUser(), comment);
		item.setNewState(newState);
		if (order.getHistory() == null) {
			order.setHistory(new ArrayList<>());
		}
		order.getHistory().add(item);
	}

	@Transactional(rollbackOn = Exception.class)
	public Order saveOrder(Order order) {
		Customer customer = getCustomerRepository().save(order.getCustomer());
		order.setCustomer(customer);

		if (order.getHistory() == null) {
			String comment = "Order placed";
			order.setHistory(new ArrayList<>());
			HistoryItem item = new HistoryItem(getUserService().getCurrentUser(), comment);
			item.setNewState(OrderState.NEW);
			order.getHistory().add(item);
		}

		return getOrderRepository().save(order);
	}

	public Order addHistoryItem(Order order, String comment) {
		HistoryItem item = new HistoryItem(getUserService().getCurrentUser(), comment);

		if (order.getHistory() == null) {
			order.setHistory(new ArrayList<>());
		}

		order.getHistory().add(item);

		return getOrderRepository().save(order);
	}

	public Page<Order> findAfterDueDateWithState(LocalDate filterDate, List<OrderState> states, Pageable pageable) {
		return getOrderRepository().findByDueDateAfterAndStateIn(filterDate, states, pageable);
	}

	public Page<Order> findAfterDueDate(LocalDate filterDate, Pageable pageable) {
		return getOrderRepository().findByDueDateAfter(filterDate, pageable);
	}

	public Page<Order> findAnyMatchingAfterDueDate(Optional<String> optionalFilter,
			Optional<LocalDate> optionalFilterDate, Pageable pageable) {
		if (optionalFilter.isPresent()) {
			if (optionalFilterDate.isPresent()) {
				return getOrderRepository().findByCustomerFullNameContainingIgnoreCaseAndDueDateAfter(
						optionalFilter.get(), optionalFilterDate.get(), pageable);
			} else {
				return getOrderRepository().findByCustomerFullNameContainingIgnoreCase(optionalFilter.get(), pageable);
			}
		} else {
			if (optionalFilterDate.isPresent()) {
				return getOrderRepository().findByDueDateAfter(optionalFilterDate.get(), pageable);
			} else {
				return getOrderRepository().findAll(pageable);
			}
		}
	}

	public long countAfterDueDateWithState(LocalDate filterDate, List<OrderState> states) {
		return getOrderRepository().countByDueDateAfterAndStateIn(filterDate, states);
	}

	public long countAnyMatchingAfterDueDate(Optional<String> optionalFilter, Optional<LocalDate> optionalFilterDate) {
		if (optionalFilter.isPresent() && optionalFilterDate.isPresent()) {
			return getOrderRepository().countByCustomerFullNameContainingIgnoreCaseAndDueDateAfter(optionalFilter.get(),
					optionalFilterDate.get());
		} else if (optionalFilter.isPresent()) {
			return getOrderRepository().countByCustomerFullNameContainingIgnoreCase(optionalFilter.get());
		} else if (optionalFilterDate.isPresent()) {
			return getOrderRepository().countByDueDateAfter(optionalFilterDate.get());
		} else {
			return getOrderRepository().count();
		}
	}

	public long countAfterDueDate(Optional<LocalDate> filterDate) {
		if (filterDate.isPresent()) {
			return getOrderRepository().countByDueDateAfter(filterDate.get());
		} else {
			return getOrderRepository().count();
		}
	}

	private DeliveryStats getDeliveryStats() {
		DeliveryStats stats = new DeliveryStats();
		LocalDate today = LocalDate.now();
		stats.setDueToday((int) getOrderRepository().countByDueDate(today));
		stats.setDueTomorrow((int) getOrderRepository().countByDueDate(today.plusDays(1)));
		stats.setDeliveredToday((int) getOrderRepository().countByDueDateAndStateIn(today,
				Collections.singleton(OrderState.DELIVERED)));

		stats.setNotAvailableToday((int) getOrderRepository().countByDueDateAndStateIn(today, notAvailableStates));
		stats.setNewOrders((int) getOrderRepository().countByState(OrderState.NEW));

		return stats;
	}

	public DashboardData getDashboardData(int month, int year) {
		DashboardData data = new DashboardData();
		data.setDeliveryStats(getDeliveryStats());
		data.setDeliveriesThisMonth(getDeliveriesPerDay(month, year));
		data.setDeliveriesThisYear(getDeliveriesPerMonth(year));

		Number[][] salesPerMonth = new Number[3][12];
		data.setSalesPerMonth(salesPerMonth);
		List<Object[]> sales = getOrderRepository().sumPerMonthLastThreeYears(OrderState.DELIVERED, year);

		for (Object[] salesData : sales) {
			// year, month, deliveries
			int y = year - (int) salesData[0];
			int m = (int) salesData[1] - 1;
			if (y == 0 && m == month - 1) {
				// skip current month as it contains incomplete data
				continue;
			}
			long count = (long) salesData[2];
			salesPerMonth[y][m] = count;
		}

		LinkedHashMap<Product, Integer> productDeliveries = new LinkedHashMap<>();
		data.setProductDeliveries(productDeliveries);
		for (Object[] result : getOrderRepository().countPerProduct(OrderState.DELIVERED, year, month)) {
			int sum = ((Long) result[0]).intValue();
			Product p = (Product) result[1];
			productDeliveries.put(p, sum);
		}

		return data;
	}

	private List<Number> getDeliveriesPerDay(int month, int year) {
		int daysInMonth = YearMonth.of(year, month).lengthOfMonth();
		return flattenAndReplaceMissingWithNull(daysInMonth,
				getOrderRepository().countPerDay(OrderState.DELIVERED, year, month));
	}

	private List<Number> getDeliveriesPerMonth(int year) {
		return flattenAndReplaceMissingWithNull(12, getOrderRepository().countPerMonth(OrderState.DELIVERED, year));
	}

	private List<Number> flattenAndReplaceMissingWithNull(int length, List<Object[]> list) {
		List<Number> counts = new ArrayList<>();
		for (int i = 0; i < length; i++) {
			counts.add(null);
		}

		for (Object[] result : list) {
			counts.set((Integer) result[0] - 1, (Number) result[1]);
		}
		return counts;
	}

	protected OrderRepository getOrderRepository() {
		if (orderRepository == null) {
			orderRepository = BeanLocator.find(OrderRepository.class);
		}
		return orderRepository;
	}

	protected CustomerRepository getCustomerRepository() {
		if (customerRepository == null) {
			customerRepository = BeanLocator.find(CustomerRepository.class);
		}
		return customerRepository;
	}

	protected UserService getUserService() {
		if (userService == null) {
			userService = BeanLocator.find(UserService.class);
		}
		return userService;
	}
}
