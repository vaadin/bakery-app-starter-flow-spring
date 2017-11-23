package com.vaadin.starter.bakery.ui.utils;

import java.io.Serializable;
import java.util.List;

import com.vaadin.starter.bakery.backend.data.entity.Order;

public class PageInfo implements Serializable {
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