package com.vaadin.flow.demo.patientportal.ui.entities.chart;

public class IntegerChartPoint {

	private String name;
	private Integer y;

	public IntegerChartPoint() {

	}

	public IntegerChartPoint(String name, Integer y) {
		this.name = name;
		this.y = y;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getY() {
		return y;
	}

	public void setY(Integer y) {
		this.y = y;
	}

	@Override
	public String toString() {
		return "ChartPoint [name=" + name + ", y=" + y + "]";
	}

}
