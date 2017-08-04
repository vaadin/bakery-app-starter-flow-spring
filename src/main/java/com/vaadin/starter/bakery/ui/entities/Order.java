package com.vaadin.starter.bakery.ui.entities;

import java.io.Serializable;
import java.util.List;

public class Order implements Serializable {

	private static final long serialVersionUID = -9042339584497118164L;

	@Override
	public String toString() {
		return "Order [status=" + status + ", date=" + date + ", place=" + place + ", customer=" + customer + ", goods="
				+ goods + "]";
	}

	private String id;
	private String status;
	private String date;
	private String time;
	private String place;
	private Customer customer;
	private List<Good> goods;
	private List<HistoryItem> history;
	private int totalPrice;

	public Order() {

	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public List<Good> getGoods() {
		return goods;
	}

	public void setGoods(List<Good> goods) {
		this.goods = goods;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public List<HistoryItem> getHistory() {
		return history;
	}

	public void setHistory(List<HistoryItem> history) {
		this.history = history;
	}

	public int getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(int totalPrice) {
		this.totalPrice = totalPrice;
	}

}
