package com.vaadin.starter.bakery.ui.view.storefront.event;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.starter.bakery.ui.view.storefront.OrderItemEditor;

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