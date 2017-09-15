/**
 * 
 */
package com.vaadin.starter.bakery.ui.components.storefront;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.IntConsumer;

import com.vaadin.annotations.HtmlImport;
import com.vaadin.annotations.Tag;
import com.vaadin.components.data.HasValue;
import com.vaadin.flow.event.ComponentEventListener;
import com.vaadin.flow.template.PolymerTemplate;
import com.vaadin.flow.template.model.TemplateModel;
import com.vaadin.shared.Registration;
import com.vaadin.starter.bakery.backend.data.entity.OrderItem;
import com.vaadin.starter.bakery.backend.data.entity.Product;
import com.vaadin.ui.ComponentEvent;

@Tag("order-items-edit")
@HtmlImport("frontend://src/storefront/order-items-edit.html")
public class OrderItemsEdit extends PolymerTemplate<OrderItemsEdit.Model>
		implements HasValue<OrderItemsEdit, List<OrderItem>> {

	private OrderItemEdit empty;

	private List<OrderItem> items;

	private List<OrderItemEdit> editors = new ArrayList<>(5);

	private List<Registration> registrations = new ArrayList<>(5);

	private ProductSource productSource;

	public interface Model extends TemplateModel {
		void setTotalPrice(Integer total);

		Integer getTotalPrice();
	}

	public void reset() {
		registrations.forEach(Registration::remove);
		registrations.clear();
		editors.forEach(i -> getElement().removeChild(i.getElement()));
		editors.clear();
		if (items != null)
			items.clear();
		getElement().removeAllChildren();
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

	}

	private OrderItemEdit createEditor(OrderItem value) {
		OrderItemEdit editor = new OrderItemEdit(this, productSource);
		editor.setValue(value);
		editors.add(editor);
		getElement().appendChild(editor.getElement());
		Registration priceChangeRegistration = addRegistration(editor.addPriceChangeListener(e -> this.priceChanged()));
		Registration productChangeRegistration = addRegistration(
				editor.addProductChangeListener(e -> this.productChanged(e.getSource(), e.getProduct())));
		editor.addDeleteListener(e -> {
			if (empty != editor) {
				OrderItem orderItem = editor.getValue();
				items.remove(orderItem);
				editors.remove(editor);
				removeRegistration(priceChangeRegistration);
				removeRegistration(productChangeRegistration);
				getElement().removeChild(editor.getElement());
				priceChanged();
			}
		});
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
		if (empty == item) {
			createEmptyElement();
			OrderItem orderItem = new OrderItem();
			orderItem.setProduct(product);
			items.add(orderItem);
			item.setValue(orderItem);
			priceChanged();
		}
	}

	private void priceChanged() {
		Integer totalPrice = items.stream().filter(o -> o != null && o.getProduct() != null)
				.mapToInt(o -> o.getProduct().getPrice() * o.getQuantity()).sum();
		fireEvent(new PriceChangeEvent(totalPrice));
	}

	private void createEmptyElement() {
		empty = createEditor(null);
		getElement().appendChild(empty.getElement());
	}

	public Registration addPriceChangeListener(ComponentEventListener<PriceChangeEvent> listener) {
		return addListener(PriceChangeEvent.class, listener);
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
}
