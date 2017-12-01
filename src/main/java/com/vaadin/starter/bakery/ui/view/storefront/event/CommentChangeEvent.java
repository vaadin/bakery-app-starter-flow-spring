package com.vaadin.starter.bakery.ui.view.storefront.event;

import com.vaadin.starter.bakery.ui.view.storefront.OrderItemEditor;
import com.vaadin.ui.event.ComponentEvent;

public class CommentChangeEvent extends ComponentEvent<OrderItemEditor> {

	private final String comment;

	public CommentChangeEvent(OrderItemEditor component, String comment) {
		super(component, false);
		this.comment = comment;
	}

	public String getComment() {
		return comment;
	}

}