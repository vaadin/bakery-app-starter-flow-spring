package com.vaadin.flow.demo.patientportal.ui.entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public class Order implements Serializable {

	private static final long serialVersionUID = -9042339584497118164L;

	@Override
	public String toString() {
		return "Order [status=" + status + ", date=" + date + ", place=" + place + ", customer=" + customer + ", goods="
				+ goods + "]";
	}

	String status;
	String date;
	String place;
	Customer customer;
	List<Good> goods;

	public Order() {

	}

	public Order(String status, LocalDate date, String place, Customer customer, List<Good> goods) {
		setStatus(status);
		this.date = date.toString();
		this.place = place;
		this.customer = customer;
		this.goods = goods;

	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		if (status != null)
			this.status = status.toLowerCase();
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

}
