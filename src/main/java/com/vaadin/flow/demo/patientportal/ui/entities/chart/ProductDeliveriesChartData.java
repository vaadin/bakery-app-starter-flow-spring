package com.vaadin.flow.demo.patientportal.ui.entities.chart;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.vaadin.flow.demo.patientportal.backend.data.entity.Product;

public class ProductDeliveriesChartData {

	private String title;
	private String seriesName;
	private List<IntegerChartPoint> points;

	public ProductDeliveriesChartData() {

	}

	public ProductDeliveriesChartData(String title, String seriesName, Map<Product, Integer> productsDeliveries) {
		this.title = title;
		this.seriesName = seriesName;
		this.points = convertProductsToPoints(productsDeliveries);
	}

	private static List<IntegerChartPoint> convertProductsToPoints(Map<Product, Integer> productsDeliveries) {
		List<IntegerChartPoint> points = new ArrayList<>();
		for (Entry<Product, Integer> entry : productsDeliveries.entrySet()) {
			points.add(new IntegerChartPoint(entry.getKey().getName(), entry.getValue()));
		}
		return points;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String toString() {
		return "PieChartData [title=" + title + ", seriesName=" + seriesName + ", points=" + points + "]";
	}

	public String getSeriesName() {
		return seriesName;
	}

	public void setSeriesName(String seriesName) {
		this.seriesName = seriesName;
	}

	public List<IntegerChartPoint> getPoints() {
		return points;
	}

	public void setPoints(List<IntegerChartPoint> points) {
		this.points = points;
	}
}
