package com.vaadin.starter.bakery.ui.entities.chart;

import java.util.List;

public class ColumnChartData {

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

	public ColumnChartData() {

	}

	public ColumnChartData(String title, String seriesName, List<Integer> values, List<String> categories) {
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
