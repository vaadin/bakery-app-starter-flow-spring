package com.vaadin.starter.bakery.ui.view.storefront.event;

import com.vaadin.starter.bakery.ui.view.storefront.OrderDetailsFull;
import com.vaadin.ui.event.ComponentEvent;

public class CommentEvent extends ComponentEvent<OrderDetailsFull> {

	private Long orderId;
	private String message;

	public CommentEvent(OrderDetailsFull component, Long orderId, String message) {
		super(component, false);
		this.orderId = orderId;
		this.message = message;
	}

	public Long getOrderId() {
		return orderId;
	}

	public String getMessage() {
		return message;
	}
}