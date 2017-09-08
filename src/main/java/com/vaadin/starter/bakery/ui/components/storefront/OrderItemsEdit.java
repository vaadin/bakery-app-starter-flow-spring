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
import com.vaadin.flow.template.PolymerTemplate;
import com.vaadin.flow.template.model.TemplateModel;
import com.vaadin.starter.bakery.backend.data.entity.OrderItem;
import com.vaadin.starter.bakery.backend.data.entity.Product;

/**
 * @author tulio
 *
 */
@Tag("order-items-edit")
@HtmlImport("frontend://src/storefront/order-items-edit.html")
public class OrderItemsEdit extends PolymerTemplate<TemplateModel> implements HasValue<OrderItemsEdit, List<OrderItem>>  {

	private OrderItemEdit empty;
	
	private List<OrderItem> items;
	
	private List<OrderItemEdit> editors = new ArrayList<>(5);
	
	private ProductSource productSource;

	private IntConsumer onTotalPriceChanged;
	
	void setProducts(Collection<Product> products) {
		this.productSource = new ProductSource(products);
		createEmptyElement();
	}

	@Override
	public OrderItemsEdit setValue(List<OrderItem> items) {
		this.items = items;
		getElement().removeChild(empty.getElement());
		if(items != null) {
			items.forEach(i -> {
				OrderItemEdit editor = new OrderItemEdit(this, productSource);
				editor.setValue(i);
				editors.add(editor);
				getElement().appendChild(editor.getElement());
			});
		}
		getElement().appendChild(empty.getElement());
		return this;
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
	
	void productChanged(OrderItemEdit item,Product product) {
		if(empty == item) {
			createEmptyElement();
			OrderItem  orderItem = new OrderItem();
			orderItem.setProduct(product);
			items.add(orderItem);
			item.setValue(orderItem);
			priceChanged();
		}
	}

	void priceChanged() {
		Integer totalPrice = items.stream().filter(o -> o != null && o.getProduct() != null)
				.mapToInt(o -> o.getProduct().getPrice() * o.getQuantity()).sum();
		onTotalPriceChanged.accept(totalPrice);
	}

	void deleteItem(OrderItemEdit item) {
		if(empty != item) {
			OrderItem  orderItem = item.getValue();
			items.remove(orderItem);
			editors.remove(item);
			getElement().removeChild(item.getElement());
			priceChanged();
		}
	}

	private void createEmptyElement() {
		empty = new OrderItemEdit(this,productSource);
		editors.add(empty);
		getElement().appendChild(empty.getElement());
	}

	public void setOnTotalPriceChanged(IntConsumer onTotalPriceChanged) {
		this.onTotalPriceChanged = onTotalPriceChanged;
	}

}
