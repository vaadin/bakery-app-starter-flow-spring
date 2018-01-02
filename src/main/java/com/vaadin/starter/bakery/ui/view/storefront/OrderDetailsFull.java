/**
 *
 */
package com.vaadin.starter.bakery.ui.view.storefront;

import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.HasClickListeners;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.templatemodel.Convert;
import com.vaadin.flow.templatemodel.Include;
import com.vaadin.flow.templatemodel.TemplateModel;
import com.vaadin.starter.bakery.backend.data.entity.Order;
import com.vaadin.starter.bakery.ui.event.CancelEvent;
import com.vaadin.starter.bakery.ui.event.SaveEvent;
import com.vaadin.starter.bakery.ui.utils.converters.CurrencyFormatter;
import com.vaadin.starter.bakery.ui.utils.converters.LocalDateTimeConverter;
import com.vaadin.starter.bakery.ui.utils.converters.LocalTimeConverter;
import com.vaadin.starter.bakery.ui.utils.converters.LongToStringConverter;
import com.vaadin.starter.bakery.ui.utils.converters.OrderStateConverter;
import com.vaadin.starter.bakery.ui.view.storefront.converter.StorefrontLocalDateConverter;
import com.vaadin.starter.bakery.ui.view.storefront.event.CommentEvent;
import com.vaadin.starter.bakery.ui.view.storefront.event.EditEvent;

/**
 * The component displaying a full (read-only) summary of an order, and a comment
 * field to add comments.
 */
@Tag("order-details-full")
@HtmlImport("src/storefront/order-details-full.html")
public class OrderDetailsFull extends PolymerTemplate<OrderDetailsFull.Model> {

	private Order order;

	@Id("back")
	private Button back;

	@Id("cancel")
	private Button cancel;

	@Id("save")
	private Button save;

	@Id("edit")
	private Button edit;

	@Id("history")
	private Element history;

	@Id("comment")
	private Element comment;

	@Id("send-comment")
	private Button sendComment;

	@Id("comment-field")
	private TextField commentField;

	public OrderDetailsFull() {
		sendComment.addClickListener(e -> {
			String message = commentField.getValue();
			message = message == null ? "" : message.trim();
			if (!message.isEmpty()) {
				commentField.clear();
				fireEvent(new CommentEvent(this, order.getId(), message));
			}
		});
		save.addClickListener(e -> fireEvent(new SaveEvent(this, false)));
		cancel.addClickListener(e -> fireEvent(new CancelEvent(this, false)));
		edit.addClickListener(e -> fireEvent(new EditEvent(this)));
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

	public Registration addEditListener(ComponentEventListener<EditEvent> listener) {
		return addListener(EditEvent.class, listener);
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
}
