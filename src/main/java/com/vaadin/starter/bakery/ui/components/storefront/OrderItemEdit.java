/**
 * 
 */
package com.vaadin.starter.bakery.ui.components.storefront;

import com.vaadin.annotations.HtmlImport;
import com.vaadin.annotations.Id;
import com.vaadin.annotations.Tag;
import com.vaadin.components.data.HasValue;
import com.vaadin.data.BeanValidationBinder;
import com.vaadin.flow.event.ComponentEventListener;
import com.vaadin.flow.html.Div;
import com.vaadin.flow.template.PolymerTemplate;
import com.vaadin.flow.template.model.TemplateModel;
import com.vaadin.shared.Registration;
import com.vaadin.starter.bakery.backend.data.entity.OrderItem;
import com.vaadin.starter.bakery.backend.data.entity.Product;
import com.vaadin.starter.bakery.ui.components.AmountField;
import com.vaadin.starter.bakery.ui.utils.FormattingUtils;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.ComponentEvent;
import com.vaadin.ui.TextField;

/**
 *
 */
@Tag("order-item-edit")
@HtmlImport("frontend://src/storefront/order-item-edit.html")
public class OrderItemEdit extends PolymerTemplate<TemplateModel> implements HasValue<OrderItemEdit, OrderItem> {

	private BeanValidationBinder<OrderItem> binder = new BeanValidationBinder<>(OrderItem.class);

	@Id("products")
	private ComboBox<String> products;

	@Id("delete")
	private Button delete;

	@Id("amount")
	private AmountField amount;

	@Id("product-price")
	private Div price;

	@Id("comment")
	private TextField comment;

	private OrderItem orderItem;

	private ProductSource productSource;

	public OrderItemEdit(OrderItemsEdit parent, ProductSource productSource) {

		this.productSource = productSource;
		this.amount.setDisabled(true);
		productSource.setupBeanComboBox(products);
		products.addChangeListener(e -> {
			if (this.amount.getValue() == null) {
				this.amount.setDisabled(false);
				this.amount.setValue(1);
			}
			fireEvent(new ProductChangeEvent(productSource.getProductByName(products.getValue())));
			this.setPrice();
		});
		amount.addValueChangeListener(e -> this.setPrice());

		// Bind with getter/setters to avoid required validation.
		binder.forField(products).withConverter(productSource).bind(OrderItem::getProduct, OrderItem::setProduct);

		binder.forField(amount).bind("quantity");

		binder.forField(comment).bind("comment");

		delete.addClickListener(e -> fireEvent(new DeleteEvent()));
		this.setPrice();
	}

	private void setPrice() {

		Integer selectedAmount = amount.getValue();
		Product product = productSource.getProductByName(products.getValue());
		int totalPrice = 0;
		if (selectedAmount != null && product != null) {
			totalPrice = selectedAmount * product.getPrice();
		}
		this.price.setText(FormattingUtils.formatAsCurrency(totalPrice));
		fireEvent(new PriceChangeEvent(totalPrice));
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		HasValue.super.setReadOnly(readOnly);
		binder.setReadOnly(readOnly);
		delete.setDisabled(readOnly);
		comment.setDisabled(readOnly);
	}

	@Override
	public void setValue(OrderItem value) {
		this.orderItem = value;
		binder.setBean(value);
		amount.setDisabled(value == null || value.getProduct() == null);
	}

	@Override
	public OrderItem getValue() {
		return this.orderItem;
	}

	public Registration addPriceChangeListener(ComponentEventListener<PriceChangeEvent> listener) {
		return addListener(PriceChangeEvent.class, listener);
	}

	public Registration addProductChangeListener(ComponentEventListener<ProductChangeEvent> listener) {
		return addListener(ProductChangeEvent.class, listener);
	}

	public Registration addDeleteListener(ComponentEventListener<DeleteEvent> listener) {
		return addListener(DeleteEvent.class, listener);
	}

	public class PriceChangeEvent extends ComponentEvent<OrderItemEdit> {

		private final Integer totalPrice;

		PriceChangeEvent(Integer totalPrice) {
			super(OrderItemEdit.this, false);
			this.totalPrice = totalPrice;
		}

		public Integer getTotalPrice() {
			return totalPrice;
		}

	}

	public class ProductChangeEvent extends ComponentEvent<OrderItemEdit> {

		private final Product product;

		ProductChangeEvent(Product product) {
			super(OrderItemEdit.this, false);
			this.product = product;
		}

		public Product getProduct() {
			return product;
		}

	}

	public class DeleteEvent extends ComponentEvent<OrderItemEdit> {
		DeleteEvent() {
			super(OrderItemEdit.this, false);
		}
	}
}
