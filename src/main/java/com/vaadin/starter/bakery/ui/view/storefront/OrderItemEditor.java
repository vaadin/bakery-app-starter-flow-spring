package com.vaadin.starter.bakery.ui.view.storefront;

import com.vaadin.data.BeanValidationBinder;
import com.vaadin.data.BindingValidationStatus;
import com.vaadin.flow.model.TemplateModel;
import com.vaadin.shared.Registration;
import com.vaadin.starter.bakery.backend.data.entity.OrderItem;
import com.vaadin.starter.bakery.backend.data.entity.Product;
import com.vaadin.starter.bakery.ui.components.ComboBoxForBinder;
import com.vaadin.starter.bakery.ui.utils.FormattingUtils;
import com.vaadin.starter.bakery.ui.view.storefront.event.CommentChangeEvent;
import com.vaadin.starter.bakery.ui.view.storefront.event.DeleteEvent;
import com.vaadin.starter.bakery.ui.view.storefront.event.PriceChangeEvent;
import com.vaadin.starter.bakery.ui.view.storefront.event.ProductChangeEvent;
import com.vaadin.ui.Tag;
import com.vaadin.ui.button.Button;
import com.vaadin.ui.common.HasValue;
import com.vaadin.ui.common.HtmlImport;
import com.vaadin.ui.event.ComponentEventListener;
import com.vaadin.ui.html.Div;
import com.vaadin.ui.polymertemplate.Id;
import com.vaadin.ui.polymertemplate.PolymerTemplate;
import com.vaadin.ui.textfield.TextField;

import java.util.stream.Stream;

@Tag("order-item-editor")
@HtmlImport("src/storefront/order-item-editor.html")
public class OrderItemEditor extends PolymerTemplate<TemplateModel> implements HasValue<OrderItemEditor, OrderItem> {

	@Id("products")
	private ComboBoxForBinder<Product> products;

	@Id("delete")
	private Button delete;

	@Id("amount")
	private AmountField amount;

	@Id("product-price")
	private Div price;

	@Id("comment")
	private TextField comment;

	private OrderItem orderItem;

	private int totalPrice;

	private BeanValidationBinder<OrderItem> binder = new BeanValidationBinder<>(OrderItem.class);

	public OrderItemEditor(ProductDataProvider productSource) {
		this.amount.setDisabled(true);
		products.setDataProvider(productSource);
		products.addValueChangeListener(e -> {
			if (this.amount.getValue() == null) {
				this.amount.setDisabled(false);
				this.amount.setValue(1);
			}
			if (this.comment.isDisabled()) {
				this.comment.setDisabled(false);
			}
			fireEvent(new ProductChangeEvent(this, e.getValue()));
			this.setPrice();
		});

		amount.addValueChangeListener(e -> this.setPrice());
		comment.addValueChangeListener(e -> fireEvent(new CommentChangeEvent(this, e.getValue())));

		binder.forField(amount).bind("quantity");
		binder.forField(comment).bind("comment");
		binder.forField(products).bind("product");

		delete.addClickListener(e -> fireEvent(new DeleteEvent(this, totalPrice)));
		this.setPrice();
	}

	private void setPrice() {
		int oldValue = totalPrice;
		Integer selectedAmount = amount.getValue();
		Product product = products.getValue();
		totalPrice = 0;
		if (selectedAmount != null && product != null) {
			totalPrice = selectedAmount * product.getPrice();
		}
		this.price.setText(FormattingUtils.formatAsCurrency(totalPrice));
		if (oldValue != totalPrice) {
			fireEvent(new PriceChangeEvent(this, oldValue, totalPrice));
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

	public Stream<HasValue> validate() {
		return binder.validate().getFieldValidationErrors().stream().map(BindingValidationStatus::getField);
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
}
