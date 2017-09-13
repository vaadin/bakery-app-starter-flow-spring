/**
 * 
 */
package com.vaadin.starter.bakery.ui.components.storefront;

import com.vaadin.annotations.HtmlImport;
import com.vaadin.annotations.Id;
import com.vaadin.annotations.Tag;
import com.vaadin.components.data.HasValue;
import com.vaadin.data.BeanValidationBinder;
import com.vaadin.flow.html.Div;
import com.vaadin.flow.template.PolymerTemplate;
import com.vaadin.flow.template.model.TemplateModel;
import com.vaadin.starter.bakery.backend.data.entity.OrderItem;
import com.vaadin.starter.bakery.backend.data.entity.Product;
import com.vaadin.starter.bakery.ui.components.AmountField;
import com.vaadin.starter.bakery.ui.utils.FormattingUtils;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.TextField;

/**
 * @author tulio
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
			this.setPrice();
			parent.productChanged(this,productSource.getProductByName(products.getValue()));
			this.amount.setDisabled(false);
			this.amount.setValue(1);
		});

		binder.forField(products).withConverter(productSource).bind("product");

		binder.forField(amount).bind("quantity");
		amount.addValueChangeListener(e -> { 
			this.setPrice();
			parent.priceChanged();
		});

		binder.forField(comment).bind("comment");
		
		delete.addClickListener(e->parent.deleteItem(this));
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

}
