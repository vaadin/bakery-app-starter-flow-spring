package com.vaadin.starter.bakery.ui.utils;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.MonthDay;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.vaadin.starter.bakery.backend.data.DeliveryStats;
import com.vaadin.starter.bakery.backend.data.OrderState;
import com.vaadin.starter.bakery.backend.data.entity.Order;
import com.vaadin.starter.bakery.backend.data.entity.Product;
import com.vaadin.starter.bakery.ui.entities.chart.ColumnChartData;
import com.vaadin.starter.bakery.ui.entities.chart.ProductDeliveriesChartData;

public class DashboardUtils {

	private static String getDeliveriesThisYearTitle() {
		return "Deliveries in " + Calendar.getInstance().get(Calendar.YEAR);
	}

	private static List<String> getDeliveriesThisYearCategories() {
		return Arrays.asList("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec");
	}

	private static List<String> getDeliveriesThisMonthCategories(List<?> list) {
		AtomicInteger dayNumber = new AtomicInteger(0);
		return list.stream().map(o -> "" + dayNumber.incrementAndGet()).collect(Collectors.toList());
	}

	private static String getCurrentMonthName() {
		return Calendar.getInstance().getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH);
	}

	private static String getDeliveriesThisMonthTitle() {
		return "Deliveries in " + getCurrentMonthName();
	}

	private static List<Integer> convertNumbersToIntegers(List<Number> numbersList) {
		if (numbersList == null)
			return Collections.emptyList();
		return numbersList.stream().map(n -> {
			if (n != null)
				return (Integer) n.intValue();
			return 0;
		}).collect(Collectors.toList());

	}

	public static class OrdersCountDataWithChart extends OrdersCountData {

		private Integer overall;

		public OrdersCountDataWithChart() {

		}

		public OrdersCountDataWithChart(String title, String subtitle, Integer count, Integer overall) {
			super(title, subtitle, count);
			this.overall = overall;
		}

		public Integer getOverall() {
			return overall;
		}

		public void setOverall(Integer overall) {
			this.overall = overall;
		}

	}

	public static class OrdersCountData {

		private String title;
		private String subtitle;
		private Integer count;

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getSubtitle() {
			return subtitle;
		}

		public void setSubtitle(String subtitle) {
			this.subtitle = subtitle;
		}

		public Integer getCount() {
			return count;
		}

		public void setCount(Integer count) {
			this.count = count;
		}

		public OrdersCountData() {

		}

		public OrdersCountData(String title, String subtitle, Integer count) {
			this.title = title;
			this.subtitle = subtitle;
			this.count = count;
		}

	}

	public static ColumnChartData getDeliveriesThisYearChartData(List<Number> deliveriesThisYear) {
		return new ColumnChartData(getDeliveriesThisYearTitle(), "per Month",
				convertNumbersToIntegers(deliveriesThisYear), getDeliveriesThisYearCategories(), MonthDay.now().getMonthValue() - 1);
	}

	public static ColumnChartData getDeliveriesThisMonthChartData(List<Number> deliveriesThisMonth) {
		return new ColumnChartData(getDeliveriesThisMonthTitle(), "per Day",
				convertNumbersToIntegers(deliveriesThisMonth), getDeliveriesThisMonthCategories(deliveriesThisMonth), MonthDay.now().getDayOfMonth() - 1);
	}

	public static ProductDeliveriesChartData getDeliveriesPerProductPieChartData(
			Map<Product, Integer> productDeliveries) {
		return new ProductDeliveriesChartData("Products delivered in " + getCurrentMonthName(), "count",
				productDeliveries);
	}

	private static final String NEXT_DELIVERY_PATTERN = "Next Delivery %s";

	public static OrdersCountDataWithChart getTodaysOrdersCountData(DeliveryStats deliveryStats,
			Iterator<Order> ordersIterator) {
		OrdersCountDataWithChart ordersCountData = new OrdersCountDataWithChart("Remaining Today", null,
				deliveryStats.getDueToday() - deliveryStats.getDeliveredToday(), deliveryStats.getDueToday());

		LocalDate date = LocalDate.now();
		LocalTime time = LocalTime.now();
		while (ordersIterator.hasNext()) {

			Order order = ordersIterator.next();
			if (isOrderNextToDeliver(order, date, time)) {
				if (order.getDueDate().isEqual(date))
					ordersCountData.setSubtitle(String.format(NEXT_DELIVERY_PATTERN, order.getDueTime()));
				else
					ordersCountData.setSubtitle(String.format(NEXT_DELIVERY_PATTERN,
							order.getDueDate().getMonthValue() + "/" + order.getDueDate().getDayOfMonth()));

				break;
			}

		}
		return ordersCountData;
	}

	private static boolean isOrderNextToDeliver(Order order, LocalDate nowDate, LocalTime nowTime) {
		// ready order starting from current time
		return order.getState() == OrderState.READY
				&& ((order.getDueDate().isEqual(nowDate) && order.getDueTime().isAfter(nowTime))
						|| order.getDueDate().isAfter(nowDate));
	}

	public static OrdersCountData getNotAvailableOrdersCountData(DeliveryStats deliveryStats) {
		// TODO: which subtitle should be here?
		OrdersCountData ordersCountData = new OrdersCountData("Not Available", "Delivery tomorrow",
				deliveryStats.getNotAvailableToday());

		return ordersCountData;
	}

	public static OrdersCountData getTomorrowOrdersCountData(DeliveryStats deliveryStats,
			Iterator<Order> ordersIterator) {
		OrdersCountData ordersCountData = new OrdersCountData("Tomorrow", null, deliveryStats.getDueTomorrow());

		LocalDate date = LocalDate.now().plusDays(1);
		LocalTime minTime = LocalTime.MAX;
		while (ordersIterator.hasNext()) {
			Order order = ordersIterator.next();
			if (order.getDueDate().isBefore(date)) {
				continue;
			}

			if (order.getDueDate().isEqual(date)) {
				if (order.getDueTime().isBefore(minTime)) {
					minTime = order.getDueTime();
				}
			}

			if (order.getDueDate().isAfter(date)) {
				break;
			}
		}

		if (!LocalTime.MAX.equals(minTime))
			ordersCountData.setSubtitle("First delivery " + minTime);

		return ordersCountData;
	}

	private static final String NEW_ORDERS_COUNT_SUBTITLE_PATTERN = "Last %d%s ago";

	public static OrdersCountData getNewOrdersCountData(DeliveryStats deliveryStats, Order lastOrder) {
		OrdersCountData ordersCountData = new OrdersCountData("New", null, deliveryStats.getNewOrders());
		LocalDateTime currTime = LocalDateTime.now();

		Order order = lastOrder;

		LocalDateTime timestamp = order.getHistory().get(0).getTimestamp();

		long value = timestamp.until(currTime, ChronoUnit.DAYS);
		if (value > 0) {
			ordersCountData.setSubtitle(String.format(NEW_ORDERS_COUNT_SUBTITLE_PATTERN, value, "d"));
			return ordersCountData;
		}

		value = timestamp.until(currTime, ChronoUnit.HOURS);
		if (value > 0) {
			ordersCountData.setSubtitle(String.format(NEW_ORDERS_COUNT_SUBTITLE_PATTERN, value, "h"));
			return ordersCountData;
		}
		value = timestamp.until(currTime, ChronoUnit.MINUTES);
		if (value > 0) {
			ordersCountData.setSubtitle(String.format(NEW_ORDERS_COUNT_SUBTITLE_PATTERN, value, "m"));
			return ordersCountData;
		}

		// option if data contain orders from the future
		ordersCountData.setSubtitle("Last just added");
		return ordersCountData;
	}

	public static class PageInfo implements Serializable {
		private int pageNumber;
		private List<Order> orders;

		public PageInfo(){
		}

		public PageInfo(List<Order> orders, Integer pageNumber) {
			this.orders = orders;
			this.pageNumber = pageNumber;
		}

		public int getPageNumber() {
			return pageNumber;
		}

		public void setPageNumber(int pageNumber) {
			this.pageNumber = pageNumber;
		}

		public List<Order> getOrders() {
			return orders;
		}

		public void setOrders(List<Order> orders) {
			this.orders = orders;
		}
	}
}
