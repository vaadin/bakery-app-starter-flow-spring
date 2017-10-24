package com.vaadin.starter.bakery.backend.data.entity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderColumn;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.validator.constraints.NotEmpty;

import com.vaadin.starter.bakery.backend.data.OrderState;

@Entity(name = "OrderInfo") // "Order" is a reserved word
@NamedEntityGraph(name = "Order.summary", attributeNodes = {
		@NamedAttributeNode("customer"),
		@NamedAttributeNode("pickupLocation")
})
public class Order extends AbstractEntity {

	@NotNull
	private LocalDate dueDate;
	@NotNull
	private LocalTime dueTime;
	@NotNull
	@OneToOne(cascade = CascadeType.ALL)
	@Fetch(FetchMode.JOIN)
	private PickupLocation pickupLocation;
	@NotNull
	@OneToOne(cascade = CascadeType.ALL)
	@Fetch(FetchMode.JOIN)
	private Customer customer;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	@OrderColumn(name = "ORDERITEM_INDEX")
	@JoinColumn
	@BatchSize(size = 1000)
	@NotEmpty
	@Valid
	private List<OrderItem> items;
	@NotNull
	private OrderState state;

	private boolean paid;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	@OrderColumn(name = "id")
	@BatchSize(size = 1000)
	private List<HistoryItem> history;

	public Order(User createdBy) {
		this.state = OrderState.NEW;
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
			history = new LinkedList<>();
		}
		history.add(item);
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

	public void changeState(User user, OrderState state) {
		boolean createHistory = this.state != state && this.state != null && state != null;
		this.state = state;
		if (createHistory) {
			addHistoryItem(user, "Order " + state.getDisplayName());
		}
	}

	public int getTotalPrice() {
		return items == null ? 0 : items.stream().mapToInt(OrderItem::getTotalPrice).sum();
	}

	@Override
	public String toString() {
		return "Order{" + "dueDate=" + dueDate + ", dueTime=" + dueTime + ", pickupLocation=" + pickupLocation
				+ ", customer=" + customer + ", items=" + items + ", state=" + state + ", paid=" + paid + ", history="
				+ history + '}';
	}
}
