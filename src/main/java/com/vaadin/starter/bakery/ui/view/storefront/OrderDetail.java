/**
 *
 */
package com.vaadin.starter.bakery.ui.view.storefront;

import com.vaadin.flow.dom.Element;
import com.vaadin.flow.model.Convert;
import com.vaadin.flow.model.Include;
import com.vaadin.flow.model.TemplateModel;
import com.vaadin.shared.Registration;
import com.vaadin.starter.bakery.backend.data.entity.Order;
import com.vaadin.starter.bakery.ui.utils.converters.CurrencyFormatter;
import com.vaadin.starter.bakery.ui.utils.converters.LocalDateTimeConverter;
import com.vaadin.starter.bakery.ui.utils.converters.LocalTimeConverter;
import com.vaadin.starter.bakery.ui.utils.converters.LongToStringConverter;
import com.vaadin.starter.bakery.ui.event.CancelEvent;
import com.vaadin.starter.bakery.ui.event.SaveEvent;
import com.vaadin.starter.bakery.ui.utils.converters.OrderStateConverter;
import com.vaadin.starter.bakery.ui.view.storefront.converter.StorefrontLocalDateConverter;
import com.vaadin.ui.Tag;
import com.vaadin.ui.button.Button;
import com.vaadin.ui.common.HasClickListeners;
import com.vaadin.ui.common.HtmlImport;
import com.vaadin.ui.event.ComponentEvent;
import com.vaadin.ui.event.ComponentEventListener;
import com.vaadin.ui.polymertemplate.Id;
import com.vaadin.ui.polymertemplate.PolymerTemplate;
import com.vaadin.ui.textfield.TextField;

@Tag("order-detail")
@HtmlImport("src/storefront/order-detail.html")
public class OrderDetail extends PolymerTemplate<OrderDetail.Model> {

	private Order order;

	@Id("order-detail-back")
	private Button back;

	@Id("order-detail-cancel")
	private Button cancel;

	@Id("order-detail-save")
	private Button save;

	@Id("order-detail-edit")
	private Button edit;

	@Id("history")
	private Element history;

	@Id("order-detail-comment")
	private Element comment;

	@Id("send-comment")
	private Button sendComment;

	@Id("comment-field")
	private TextField commentField;

	public OrderDetail() {
		sendComment.addClickListener(e -> {
			fireEvent(new CommentEvent(order.getId(), commentField.getValue()));
		});
		save.addClickListener(e -> fireEvent(new SaveEvent(this, false)));
		cancel.addClickListener(e -> fireEvent(new CancelEvent(this, false)));
	}

	public void display(Order order, boolean review) {
		this.order = order;
		getModel().setItem(order);
		if (!review) {
			commentField.clear();
		}
		getModel().setReview(review);
	}

	public interface Model extends TemplateModel {
		@Include({ "id", "dueDate.day", "dueDate.weekday", "dueDate.date", "dueTime", "state", "pickupLocation.name",
				"customer.fullName", "customer.phoneNumber", "customer.details", "items.product.name", "items.comment",
				"items.quantity", "items.product.price", "history.message", "history.createdBy.firstName",
				"history.timestamp", "history.newState", "totalPrice" })
		@Convert(value = LongToStringConverter.class, path = "id")
		@Convert(value = StorefrontLocalDateConverter.class, path = "dueDate")
		@Convert(value = LocalTimeConverter.class, path = "dueTime")
		@Convert(value = OrderStateConverter.class, path = "state")
		@Convert(value = CurrencyFormatter.class, path = "items.product.price")
		@Convert(value = LocalDateTimeConverter.class, path = "history.timestamp")
		@Convert(value = OrderStateConverter.class, path = "history.newState")
		@Convert(value = CurrencyFormatter.class, path = "totalPrice")
		void setItem(Order order);

		void setReview(boolean review);
	}

	public Registration addSaveListenter(ComponentEventListener<SaveEvent> listener) {
		return addListener(SaveEvent.class, listener);
	}

	public Registration addEditListener(ComponentEventListener<HasClickListeners.ClickEvent<Button>> listener) {
		return edit.addClickListener(listener);
	}

	public Registration addBackListener(ComponentEventListener<HasClickListeners.ClickEvent<Button>> listener) {
		return back.addClickListener(listener);
	}

	public Registration addCommentListener(ComponentEventListener<CommentEvent> listener) {
		return addListener(CommentEvent.class, listener);
	}

	public Registration addCancelListener(ComponentEventListener<CancelEvent> listener) {
		return addListener(CancelEvent.class, listener);
	}

	public class CommentEvent extends ComponentEvent<OrderDetail> {

		private Long orderId;
		private String message;

		private CommentEvent(Long orderId, String message) {
			super(OrderDetail.this, false);
			this.orderId = orderId;
			this.message = message;
		}

		public Long getOrderId() {
			return orderId;
		}

		public String getMessage() {
			return message;
		}
	}
}
