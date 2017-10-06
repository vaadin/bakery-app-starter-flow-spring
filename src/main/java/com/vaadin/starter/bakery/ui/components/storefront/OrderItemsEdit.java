package com.vaadin.starter.bakery.ui.components.storefront;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.vaadin.components.data.HasValue;
import com.vaadin.flow.event.ComponentEventListener;
import com.vaadin.flow.html.Div;
import com.vaadin.shared.Registration;
import com.vaadin.starter.bakery.backend.data.entity.OrderItem;
import com.vaadin.starter.bakery.backend.data.entity.Product;
import com.vaadin.ui.ComponentEvent;

public class OrderItemsEdit extends Div implements HasValue<OrderItemsEdit, List<OrderItem>> {

	private OrderItemEdit empty;

	private List<OrderItem> items;

	private List<OrderItemEdit> editors = new LinkedList<>();

	private List<Registration> registrations = new LinkedList<>();

	private ProductSource productSource;

	private int totalPrice = 0;

	private boolean hasChanges = false;

	public void reset() {
		registrations.forEach(Registration::remove);
		registrations.clear();
		editors.forEach(i -> getElement().removeChild(i.getElement()));
		editors.clear();
		if (items != null)
			items.clear();
		getElement().removeAllChildren();
		this.totalPrice = 0;
		this.hasChanges = false;
	}

	void setProducts(Collection<Product> products) {
		this.productSource = new ProductSource(products);
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

	private OrderItemEdit createEditor(OrderItem value) {
		OrderItemEdit editor = new OrderItemEdit(productSource);
		editors.add(editor);
		getElement().appendChild(editor.getElement());
		Registration priceChangeRegistration = addRegistration(editor
				.addPriceChangeListener(e -> this.updateTotalPriceOnItemPriceChange(e.getOldValue(), e.getNewValue())));
		Registration productChangeRegistration = addRegistration(
				editor.addProductChangeListener(e -> this.productChanged(e.getSource(), e.getProduct())));
		Registration commentChangeRegistration = addRegistration(
				editor.addCommentChangeListener(e -> setHasChanges(true)));
		editor.addDeleteListener(e -> {
			if (empty != editor) {
				OrderItem orderItem = editor.getValue();
				items.remove(orderItem);
				editors.remove(editor);
				removeRegistration(priceChangeRegistration);
				removeRegistration(productChangeRegistration);
				removeRegistration(commentChangeRegistration);
				getElement().removeChild(editor.getElement());
				updateTotalPriceOnItemPriceChange(e.getTotalPrice(), 0);
				setHasChanges(true);
			}
		});
		editor.setValue(value);
		return editor;
	}

	private void removeRegistration(Registration r) {
		r.remove();
		registrations.remove(r);
	}

	private Registration addRegistration(Registration r) {
		registrations.add(r);
		return r;
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		HasValue.super.setReadOnly(readOnly);
		this.editors.forEach(e -> e.setReadOnly(readOnly));
	}

	@Override
	public List<OrderItem> getValue() {
		return items;
	}

	private void productChanged(OrderItemEdit item, Product product) {
		setHasChanges(true);
		if (empty == item) {
			createEmptyElement();
			OrderItem orderItem = new OrderItem();
			orderItem.setProduct(product);
			items.add(orderItem);
			item.setValue(orderItem);
			fireEvent(new NewEditorEvent());
		}
	}

	private void updateTotalPriceOnItemPriceChange(int oldItemPrice, int newItemPrice) {
		final int delta = newItemPrice - oldItemPrice;
		totalPrice += delta;
		setHasChanges(true);
		fireEvent(new PriceChangeEvent(totalPrice));
	}

	private void createEmptyElement() {
		empty = createEditor(null);
		getElement().appendChild(empty.getElement());
	}

	public Registration addPriceChangeListener(ComponentEventListener<PriceChangeEvent> listener) {
		return addListener(PriceChangeEvent.class, listener);
	}

	public boolean hasChanges() {
		return hasChanges;
	}

	private void setHasChanges(boolean hasChanges) {
		this.hasChanges = hasChanges;
		if (hasChanges) {
			fireEvent(new ValueChangeEvent());
		}
	}

	public class PriceChangeEvent extends ComponentEvent<OrderItemsEdit> {

		private final Integer totalPrice;

		PriceChangeEvent(Integer totalPrice) {
			super(OrderItemsEdit.this, false);
			this.totalPrice = totalPrice;
		}

		public Integer getTotalPrice() {
			return totalPrice;
		}

	}

	public class ValueChangeEvent extends ComponentEvent<OrderItemsEdit> {

		ValueChangeEvent() {
			super(OrderItemsEdit.this, false);
		}
	}

	public class NewEditorEvent extends ComponentEvent<OrderItemsEdit> {

		NewEditorEvent() {
			super(OrderItemsEdit.this, false);
		}
	}

}
