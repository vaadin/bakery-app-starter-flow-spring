/**
 * 
 */
package com.vaadin.starter.bakery.ui.components.storefront;

import java.util.function.Consumer;
import java.util.function.IntConsumer;

import com.vaadin.annotations.HtmlImport;
import com.vaadin.annotations.Id;
import com.vaadin.annotations.Tag;
import com.vaadin.components.data.HasValue;
import com.vaadin.data.BeanValidationBinder;
import com.vaadin.data.Converter;
import com.vaadin.data.Result;
import com.vaadin.data.ValueContext;
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
	    productSource.setupBeanComboBox(products);
		products.addChangeListener(e -> {
			this.setPrice();
			parent.productChanged(this,productSource.getProductByName(products.getValue()));
			this.amount.setEnabled(true);
		});

		binder.forField(products).withConverter(productSource).bind("product");

		binder.forField(amount).bind("quantity");
		amount.addValueChangeListener(e -> { 
			this.setPrice();
			parent.priceChanged();
		});

		binder.forField(comment).bind("comment");
		
		delete.addClickListener(e->parent.deleteItem(this));
	}

	private void setPrice() {
		Integer selectedAmount = amount.getValue();
		Product product = productSource.getProductByName(products.getValue());
		String formattedPrice = null;
		if (selectedAmount != null && product != null) {
			int totalPrice = selectedAmount * product.getPrice();
			formattedPrice = FormattingUtils.formatAsCurrency(totalPrice);
		}
		this.price.setText(formattedPrice);
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		HasValue.super.setReadOnly(readOnly);
		binder.setReadOnly(readOnly);
		delete.setDisabled(readOnly);
		comment.setDisabled(readOnly);
	}

	@Override
	public OrderItemEdit setValue(OrderItem value) {
		this.orderItem = value;
		binder.setBean(value);
		return this;
	}

	@Override
	public OrderItem getValue() {
		return this.orderItem;
	}

}
