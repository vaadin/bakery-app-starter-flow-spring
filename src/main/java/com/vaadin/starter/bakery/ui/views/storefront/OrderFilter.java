package com.vaadin.starter.bakery.ui.views.storefront;

import java.io.Serializable;

public class OrderFilter implements Serializable {

	private String filter;

	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

	public boolean isShowPrevious() {
		return showPrevious;
	}

	public void setShowPrevious(boolean showPrevious) {
		this.showPrevious = showPrevious;
	}

	private boolean showPrevious;

	public OrderFilter(String filter, boolean showPrevious) {
		this.filter = filter;
		this.showPrevious = showPrevious;
	}

	public static OrderFilter getEmptyFilter() {
		return new OrderFilter("", false);
	}
}
