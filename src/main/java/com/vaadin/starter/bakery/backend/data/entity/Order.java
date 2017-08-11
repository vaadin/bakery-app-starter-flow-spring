package com.vaadin.starter.bakery.backend.data.entity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.vaadin.starter.bakery.backend.data.OrderState;

@Entity(name = "OrderInfo") // "Order" is a reserved word
public class Order extends AbstractEntity {

	@NotNull
	private LocalDate dueDate;
	@NotNull
	private LocalTime dueTime;
	@NotNull
	@OneToOne(cascade = CascadeType.ALL)
	private PickupLocation pickupLocation;
	@NotNull
	@OneToOne(cascade = CascadeType.ALL)
	private Customer customer;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	@OrderColumn(name = "ORDERITEM_INDEX")
	@JoinColumn
	private List<OrderItem> items;
	@NotNull
	private OrderState state;

	private boolean paid;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@OrderColumn(name = "id")
	private List<HistoryItem> history;

	public Order(User createdBy) {
		setState(OrderState.NEW);
		setCustomer(new Customer());
		setPickupLocation(new PickupLocation());
		addHistoryItem(createdBy, "Order placed");
		this.items = new ArrayList<>();
	}

	Order() {
		// Empty constructor is needed by Spring Data / JPA
	}

	public void addHistoryItem(User createdBy, String comment) {
		HistoryItem item = new HistoryItem(createdBy, comment);
		item.setNewState(state);
		if (history == null) {
			history = Collections.singletonList(item);
		} else {
			history.add(item);
		}
	}

	public void clearItems() {
		this.items.clear();
	}

	public void addOrderItem(Product product, int quantity, String comment) {
		OrderItem item = new OrderItem();
		item.setProduct(product);
		item.setQuantity(quantity);
		item.setComment(comment);
		this.items.add(item);
	}

	public LocalDate getDueDate() {
		return dueDate;
	}

	public void setDueDate(LocalDate dueDate) {
		this.dueDate = dueDate;
	}

	public LocalTime getDueTime() {
		return dueTime;
	}

	public void setDueTime(LocalTime dueTime) {
		this.dueTime = dueTime;
	}

	public PickupLocation getPickupLocation() {
		return pickupLocation;
	}

	public void setPickupLocation(PickupLocation pickupLocation) {
		this.pickupLocation = pickupLocation;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public List<OrderItem> getItems() {
		return items;
	}

	public void setItems(List<OrderItem> items) {
		this.items = items;
	}

	public boolean isPaid() {
		return paid;
	}

	public void setPaid(boolean paid) {
		this.paid = paid;
	}

	public List<HistoryItem> getHistory() {
		return history;
	}

	public void setHistory(List<HistoryItem> history) {
		this.history = history;
	}

	public OrderState getState() {
		return state;
	}

	public void setState(OrderState state) {
		this.state = state;
	}

}
