package com.vaadin.flow.demo.patientportal.ui.utils;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

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

	private static String getDeliveriesThisMonthTitle() {
		String thisMonth;
		switch (Calendar.getInstance().get(Calendar.MONTH)) {
		case Calendar.JANUARY:
			thisMonth = "January";
			break;
		case Calendar.FEBRUARY:
			thisMonth = "February";
			break;
		case Calendar.MARCH:
			thisMonth = "March";
			break;
		case Calendar.APRIL:
			thisMonth = "April";
			break;
		case Calendar.MAY:
			thisMonth = "May";
			break;
		case Calendar.JUNE:
			thisMonth = "June";
			break;
		case Calendar.JULY:
			thisMonth = "July";
			break;
		case Calendar.AUGUST:
			thisMonth = "August";
			break;

		case Calendar.SEPTEMBER:
			thisMonth = "September";
			break;
		case Calendar.OCTOBER:
			thisMonth = "October";
			break;
		case Calendar.NOVEMBER:
			thisMonth = "November";
			break;
		case Calendar.DECEMBER:
			thisMonth = "December";
			break;
		default:
			thisMonth = "this month";
			break;
		}

		return "Deliveries in " + thisMonth;
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

	public static class ChartData {

		private String title;
		private String seriesName;
		private List<String> categories;
		private List<Integer> values;

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public List<Integer> getValues() {
			return values;
		}

		public void setValues(List<Integer> values) {
			this.values = values;
		}

		public ChartData() {

		}

		public ChartData(String title, String seriesName, List<Integer> values, List<String> categories) {
			this.title = title;
			this.seriesName = seriesName;
			this.values = values;
			this.categories = categories;
		}

		@Override
		public String toString() {
			return "ChartData [title=" + title + ", values=" + values + "]";
		}

		public List<String> getCategories() {
			return categories;
		}

		public void setCategories(List<String> categories) {
			this.categories = categories;
		}

		public String getSeriesName() {
			return seriesName;
		}

		public void setSeriesName(String seriesName) {
			this.seriesName = seriesName;
		}

	}

	public static ChartData getDeliveriesThisYearChartData(List<Number> deliveriesThisYear) {
		return new ChartData(getDeliveriesThisYearTitle(), "per Month", convertNumbersToIntegers(deliveriesThisYear),
				getDeliveriesThisYearCategories());
	}

	public static ChartData getDeliveriesThisMonthChartData(List<Number> deliveriesThisMonth) {
		return new ChartData(getDeliveriesThisMonthTitle(), "per Day", convertNumbersToIntegers(deliveriesThisMonth),
				getDeliveriesThisMonthCategories(deliveriesThisMonth));
	}

}
