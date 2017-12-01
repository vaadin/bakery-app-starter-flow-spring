package com.vaadin.starter.bakery.ui.view.storefront.event;

import com.vaadin.starter.bakery.ui.view.storefront.OrderItemsEditor;
import com.vaadin.ui.event.ComponentEvent;

public class TotalPriceChangeEvent extends ComponentEvent<OrderItemsEditor> {

	private final Integer totalPrice;

	public TotalPriceChangeEvent(OrderItemsEditor component, Integer totalPrice) {
		super(component, false);
		this.totalPrice = totalPrice;
	}

	public Integer getTotalPrice() {
		return totalPrice;
	}

}