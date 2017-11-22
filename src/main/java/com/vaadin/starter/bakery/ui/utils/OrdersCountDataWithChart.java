package com.vaadin.starter.bakery.ui.utils;

import com.vaadin.starter.bakery.ui.utils.DashboardUtils.OrdersCountData;

public class OrdersCountDataWithChart extends OrdersCountData {

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