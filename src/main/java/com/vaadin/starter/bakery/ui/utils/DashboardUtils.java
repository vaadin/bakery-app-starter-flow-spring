package com.vaadin.starter.bakery.ui.utils;

import static com.vaadin.starter.bakery.ui.utils.FormattingUtils.getFullMonthName;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import com.vaadin.addon.charts.model.AxisTitle;
import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.addon.charts.model.Configuration;
import com.vaadin.addon.charts.model.Legend;
import com.vaadin.addon.charts.model.ListSeries;
import com.vaadin.addon.charts.model.XAxis;
import com.vaadin.addon.charts.model.YAxis;
import com.vaadin.starter.bakery.backend.data.DeliveryStats;
import com.vaadin.starter.bakery.backend.data.OrderState;
import com.vaadin.starter.bakery.backend.data.entity.Order;
import com.vaadin.starter.bakery.backend.data.entity.Product;
import com.vaadin.starter.bakery.ui.entities.chart.ProductDeliveriesChartData;

public class DashboardUtils {

	public static final String[] MONTH_LABELS = new String[] {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul",
			"Aug", "Sep", "Oct", "Nov", "Dec"};

	public static void writeDeliveriesThisYearChartConfig(Configuration conf, List<Number> deliveriesThisYear) {
		String deliveriesThisYearTitle = "Deliveries in " + LocalDate.now().getYear();
		ListSeries series = new ListSeries("per Month", deliveriesThisYear);

		writeDashboardColumnChartConfig(conf, series, deliveriesThisYearTitle, MONTH_LABELS);
	}

	public static void writeDeliveriesThisMonthChartConfig(Configuration conf, List<Number> deliveriesThisMonth) {
		// A range going from 1 to the number of items in deliveriesThisMonth
		String[] deliveriesThisMonthCategories = IntStream.rangeClosed(1, deliveriesThisMonth.size())
				.mapToObj(String::valueOf).toArray(String[]::new);

		String deliveriesThisMonthTitle = "Deliveries in " + getFullMonthName(LocalDate.now());
		ListSeries series = new ListSeries("per Day", deliveriesThisMonth);

		writeDashboardColumnChartConfig(conf, series, deliveriesThisMonthTitle, deliveriesThisMonthCategories);
	}

	private static void writeDashboardColumnChartConfig(Configuration conf, ListSeries series, String chartTitle,
			String[] categories) {
		conf.getChart().setType(ChartType.COLUMN);
		conf.getChart().setBorderRadius(4);

		conf.setTitle(chartTitle);
		conf.addSeries(series);

		XAxis xAxis = new XAxis();
		xAxis.setTickInterval(1);
		xAxis.setMinorTickLength(0);
		xAxis.setTickLength(0);
		xAxis.setCategories(categories);
		conf.addxAxis(xAxis);

		YAxis yAxis = new YAxis();
		AxisTitle title = new AxisTitle();
		title.setText(null);
		yAxis.setTitle(title);
		conf.addyAxis(yAxis);

		Legend legend = new Legend();
		legend.setEnabled(false);
		conf.setLegend(legend);
	}

	public static ProductDeliveriesChartData getDeliveriesPerProductPieChartData(
			Map<Product, Integer> productDeliveries) {
		return new ProductDeliveriesChartData("Products delivered in " + getFullMonthName(LocalDate.now()), "count",
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

	public static OrdersCountData getNewOrdersCountData(DeliveryStats deliveryStats, Order lastOrder) {
		return new OrdersCountData("New", createSubtitle(lastOrder), deliveryStats.getNewOrders());
	}

	private static final String NEW_ORDERS_COUNT_SUBTITLE_PATTERN = "Last %d%s ago";

	private static String createSubtitle(Order lastOrder) {
		LocalDateTime currTime = LocalDateTime.now();
		LocalDateTime timestamp = lastOrder.getHistory().get(0).getTimestamp();

		long value = timestamp.until(currTime, ChronoUnit.DAYS);
		if (value > 0) {
			return String.format(NEW_ORDERS_COUNT_SUBTITLE_PATTERN, value, "d");
		}

		value = timestamp.until(currTime, ChronoUnit.HOURS);
		if (value > 0) {
			return String.format(NEW_ORDERS_COUNT_SUBTITLE_PATTERN, value, "h");
		}

		value = timestamp.until(currTime, ChronoUnit.MINUTES);
		if (value > 0) {
			return String.format(NEW_ORDERS_COUNT_SUBTITLE_PATTERN, value, "m");
		}

		// option if data contain orders from the future
		return "Last just added";
	}
}
