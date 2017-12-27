package com.vaadin.starter.bakery.ui.view.storefront;

import java.util.List;

import com.vaadin.shared.Registration;
import com.vaadin.starter.bakery.backend.data.entity.OrderItem;
import com.vaadin.starter.bakery.backend.data.entity.Product;
import com.vaadin.starter.bakery.ui.view.storefront.event.NewEditorEvent;
import com.vaadin.starter.bakery.ui.view.storefront.event.TotalPriceChangeEvent;
import com.vaadin.ui.common.HasValue;
import com.vaadin.ui.event.ComponentEventListener;
import com.vaadin.ui.html.Div;

public class OrderItemsEditor extends Div implements HasValue<OrderItemsEditor, List<OrderItem>> {

	private OrderItemEditor empty;

	private List<OrderItem> items;

	private ProductDataProvider productDataProvider;

	private int totalPrice = 0;

	private boolean hasChanges = false;

	public OrderItemsEditor(ProductDataProvider productDataProvider) {
		this.productDataProvider = productDataProvider;
	}

	public void reset() {
		getElement().removeAllChildren();
		this.totalPrice = 0;
		this.hasChanges = false;
	}

	@Override
	public void setValue(List<OrderItem> items) {
		reset();
		this.items = items;
		if (items != null) {
			items.forEach(this::createEditor);
		}
		createEmptyElement();
		setHasChanges(false);
	}

	private OrderItemEditor createEditor(OrderItem value) {
		OrderItemEditor editor = new OrderItemEditor(productDataProvider);
		getElement().appendChild(editor.getElement());
		editor.addPriceChangeListener(e -> this.updateTotalPriceOnItemPriceChange(e.getOldValue(), e.getNewValue()));
		editor.addProductChangeListener(e -> this.productChanged(e.getSource(), e.getProduct()));
		editor.addCommentChangeListener(e -> setHasChanges(true));
		editor.addDeleteListener(e -> {
			if (empty != editor) {
				OrderItem orderItem = editor.getValue();
				items.remove(orderItem);
				this.remove(editor);
				updateTotalPriceOnItemPriceChange(e.getTotalPrice(), 0);
				setHasChanges(true);
			}
		});
		editor.setValue(value);
		return editor;
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		HasValue.super.setReadOnly(readOnly);
		this.getChildren().forEach(e -> ((OrderItemEditor) e).setReadOnly(readOnly));
	}

	@Override
	public List<OrderItem> getValue() {
		return items;
	}

	private void productChanged(OrderItemEditor item, Product product) {
		setHasChanges(true);
		if (empty == item) {
			createEmptyElement();
			OrderItem orderItem = new OrderItem();
			orderItem.setProduct(product);
			items.add(orderItem);
			item.setValue(orderItem);
			fireEvent(new NewEditorEvent(this));
		}
	}

	private void updateTotalPriceOnItemPriceChange(int oldItemPrice, int newItemPrice) {
		final int delta = newItemPrice - oldItemPrice;
		totalPrice += delta;
		setHasChanges(true);
		fireEvent(new TotalPriceChangeEvent(this, totalPrice));
	}

	private void createEmptyElement() {
		empty = createEditor(null);
	}

	public Registration addPriceChangeListener(ComponentEventListener<TotalPriceChangeEvent> listener) {
		return addListener(TotalPriceChangeEvent.class, listener);
	}

	public boolean hasChanges() {
		return hasChanges;
	}

	private void setHasChanges(boolean hasChanges) {
		this.hasChanges = hasChanges;
		if (hasChanges) {
			fireEvent(new com.vaadin.starter.bakery.ui.view.storefront.event.ValueChangeEvent(this));
		}
	}

}
