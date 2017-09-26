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

@Tag("order-item-edit")
@HtmlImport("context://src/storefront/order-item-edit.html")
public class OrderItemEdit extends PolymerTemplate<OrderItemEdit.Model> implements HasValue<OrderItemEdit, OrderItem> {

	public interface Model extends TemplateModel {
		void setTotalPrice(Integer total);

		Integer getTotalPrice();
	}

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

	private int totalPrice;

	private BeanValidationBinder<OrderItem> binder = new BeanValidationBinder<>(OrderItem.class);

	public OrderItemEdit(ProductSource productSource) {
		this.productSource = productSource;
		this.amount.setDisabled(true);
		productSource.setupBeanComboBox(products);
		products.addChangeListener(e -> {
			if (this.amount.getValue() == null) {
				this.amount.setDisabled(false);
				this.amount.setValue(1);
			}
			if (this.comment.isDisabled()) {
				this.comment.setDisabled(false);
			}
			fireEvent(new ProductChangeEvent(productSource.getProductByName(products.getValue())));
			this.setPrice();
		});

		amount.addValueChangeListener(e -> this.setPrice());
		comment.addValueChangeListener(e -> fireEvent(new CommentChangeEvent(e.getValue())));

		binder.forField(amount).bind("quantity");
		binder.forField(comment).bind("comment");

		// Bind with getter/setters to avoid required validation.
		binder.forField(products).withConverter(productSource).bind(OrderItem::getProduct, OrderItem::setProduct);

		delete.addClickListener(e -> fireEvent(new DeleteEvent(totalPrice)));
		this.setPrice();
	}

	private void setPrice() {
		int oldValue = totalPrice;
		Integer selectedAmount = amount.getValue();
		Product product = productSource.getProductByName(products.getValue());
		totalPrice = 0;
		if (selectedAmount != null && product != null) {
			totalPrice = selectedAmount * product.getPrice();
		}
		this.price.setText(FormattingUtils.formatAsCurrency(totalPrice));
		if (oldValue != totalPrice) {
			fireEvent(new PriceChangeEvent(oldValue, totalPrice));
		}
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
		boolean noProductSelected = value == null || value.getProduct() == null;
		amount.setDisabled(noProductSelected);
		comment.setDisabled(noProductSelected);
		this.setPrice();
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

	public Registration addCommentChangeListener(ComponentEventListener<CommentChangeEvent> listener) {
		return addListener(CommentChangeEvent.class, listener);
	}

	public Registration addDeleteListener(ComponentEventListener<DeleteEvent> listener) {
		return addListener(DeleteEvent.class, listener);
	}

	public class PriceChangeEvent extends ComponentEvent<OrderItemEdit> {

		private final int oldValue;

		private final int newValue;

		public PriceChangeEvent(int oldValue, int newValue) {
			super(OrderItemEdit.this, false);
			this.oldValue = oldValue;
			this.newValue = newValue;
		}

		public int getOldValue() {
			return oldValue;
		}

		public int getNewValue() {
			return newValue;
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

	public class CommentChangeEvent extends ComponentEvent<OrderItemEdit> {

		private final String comment;

		CommentChangeEvent(String comment) {
			super(OrderItemEdit.this, false);
			this.comment = comment;
		}

		public String getComment() {
			return comment;
		}

	}

	public class DeleteEvent extends ComponentEvent<OrderItemEdit> {

		private final int totalPrice;

		DeleteEvent(int totalPrice) {
			super(OrderItemEdit.this, false);
			this.totalPrice = totalPrice;
		}

		public int getTotalPrice() {
			return totalPrice;
		}

	}
}
