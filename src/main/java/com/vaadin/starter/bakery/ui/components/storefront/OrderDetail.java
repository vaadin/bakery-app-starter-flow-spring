/**
 *
 */
package com.vaadin.starter.bakery.ui.components.storefront;

import com.vaadin.flow.dom.Element;
import com.vaadin.flow.model.Convert;
import com.vaadin.flow.model.Include;
import com.vaadin.flow.model.TemplateModel;
import com.vaadin.shared.Registration;
import com.vaadin.starter.bakery.backend.data.entity.HistoryItem;
import com.vaadin.starter.bakery.backend.data.entity.Order;
import com.vaadin.starter.bakery.ui.converters.LocalDateConverter;
import com.vaadin.starter.bakery.ui.converters.LocalDateTimeConverter;
import com.vaadin.starter.bakery.ui.converters.LocalTimeConverter;
import com.vaadin.starter.bakery.ui.converters.LongToStringConverter;
import com.vaadin.starter.bakery.ui.utils.FormattingUtils;
import com.vaadin.ui.Tag;
import com.vaadin.ui.button.Button;
import com.vaadin.ui.common.HasClickListeners;
import com.vaadin.ui.common.HtmlImport;
import com.vaadin.ui.event.ComponentEvent;
import com.vaadin.ui.event.ComponentEventListener;
import com.vaadin.ui.polymertemplate.Id;
import com.vaadin.ui.polymertemplate.PolymerTemplate;
import com.vaadin.ui.textfield.TextField;

import java.util.List;

@Tag("order-detail")
@HtmlImport("context://src/storefront/order-detail.html")
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
			if (commentField.getValue() != null && !commentField.getValue().isEmpty()) {
				fireEvent(new CommentEvent(order.getId(), commentField.getValue()));
			}
		});
	}

	public void display(Order order, boolean review) {
		this.order = order;
		getModel().setItem(order);
		getModel().setTotalPrice(FormattingUtils.formatAsCurrency(order.getTotalPrice()));
		if (!review) {
			getModel().setHistory(order.getHistory());
			commentField.clear();
		}
		setHidden(cancel.getElement(), review);
		setHidden(back.getElement(), !review);
		setHidden(edit.getElement(), review);
		setHidden(save.getElement(), !review);
		setHidden(history, review);
		setHidden(comment, review);
	}

	private void setHidden(Element e, boolean hide) {
		e.setAttribute("hidden", hide);
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

		@Include({ "message", "createdBy.firstName", "timestamp", "newState" })
		@Convert(value = LocalDateTimeConverter.class, path = "timestamp")
		@Convert(value = OrderStateConverter.class, path = "newState")
		void setHistory(List<HistoryItem> history);

		void setReview(boolean review);

		void setTotalPrice(String totalPrice);
	}

	public Registration addSaveListener(ComponentEventListener<HasClickListeners.ClickEvent<Button>> listener) {
		return save.addClickListener(listener);
	}

	public Registration addCancelListener(ComponentEventListener<HasClickListeners.ClickEvent<Button>> listener) {
		return cancel.addClickListener(listener);
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
