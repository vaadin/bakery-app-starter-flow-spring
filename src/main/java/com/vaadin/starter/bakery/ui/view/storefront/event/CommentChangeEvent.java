package com.vaadin.starter.bakery.ui.view.storefront.event;

import com.vaadin.starter.bakery.ui.view.storefront.OrderItemEdit;
import com.vaadin.ui.event.ComponentEvent;

public class CommentChangeEvent extends ComponentEvent<OrderItemEdit> {

	private final String comment;

	public CommentChangeEvent(OrderItemEdit component, String comment) {
		super(component, false);
		this.comment = comment;
	}

	public String getComment() {
		return comment;
	}

}