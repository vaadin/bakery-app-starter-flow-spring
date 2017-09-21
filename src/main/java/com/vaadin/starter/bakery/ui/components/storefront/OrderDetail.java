/**
 * 
 */
package com.vaadin.starter.bakery.ui.components.storefront;

import com.vaadin.annotations.Convert;
import com.vaadin.annotations.HtmlImport;
import com.vaadin.annotations.Id;
import com.vaadin.annotations.Include;
import com.vaadin.annotations.Tag;
import com.vaadin.flow.event.ComponentEventListener;
import com.vaadin.flow.template.PolymerTemplate;
import com.vaadin.flow.template.model.TemplateModel;
import com.vaadin.shared.Registration;
import com.vaadin.starter.bakery.backend.data.entity.Order;
import com.vaadin.starter.bakery.ui.converters.LocalDateConverter;
import com.vaadin.starter.bakery.ui.converters.LocalTimeConverter;
import com.vaadin.starter.bakery.ui.converters.LongToStringConverter;
import com.vaadin.starter.bakery.ui.utils.FormattingUtils;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComponentEvent;

@Tag("order-detail")
@HtmlImport("frontend://src/storefront/order-detail.html")
public class OrderDetail extends PolymerTemplate<OrderDetail.Model> {

	private Order order;

	@Id("back")
	private Button back;

	@Id("save")
	private Button save;

	public OrderDetail() {
		back.addClickListener(e -> fireEvent(new BackEvent()));
		save.addClickListener(e -> fireEvent(new SaveEvent()));
	}

	public void display(Order order,boolean review) {
		this.order = order;
		getModel().setReview(true);
		getModel().setItem(order);
		getModel().setTotalPrice(FormattingUtils.formatAsCurrency(order.getTotalPrice()));
	}

	public interface Model extends TemplateModel {
		@Include({ "id", "dueDate", "dueTime", "state", "pickupLocation.name", "customer.fullName",
				"customer.phoneNumber", "customer.details", "items.product.name", "items.comment", "items.quantity",
				"items.product.price" })
		@Convert(value = LongToStringConverter.class, path = "id")
		@Convert(value = LocalDateConverter.class, path = "dueDate")
		@Convert(value = LocalTimeConverter.class, path = "dueTime")
		@Convert(value = OrderStateConverter.class, path = "state")
		void setItem(Order order);

		void setReview(boolean review);

		void setTotalPrice(String totalPrice);
	}

	public Registration addSaveListener(ComponentEventListener<SaveEvent> listener) {
		return addListener(SaveEvent.class, listener);
	}

	public Registration addBackListener(ComponentEventListener<BackEvent> listener) {
		return addListener(BackEvent.class, listener);
	}

	public class SaveEvent extends ComponentEvent<OrderDetail> {

		private SaveEvent() {
			super(OrderDetail.this, false);
		}
	}

	public class BackEvent extends ComponentEvent<OrderDetail> {

		private BackEvent() {
			super(OrderDetail.this, false);
		}
	}

}
