package com.vaadin.starter.bakery.ui.view.storefront;

import com.vaadin.flow.model.Convert;
import com.vaadin.flow.model.Include;
import com.vaadin.flow.model.TemplateModel;
import com.vaadin.shared.Registration;
import com.vaadin.starter.bakery.backend.data.entity.Order;
import com.vaadin.starter.bakery.ui.entities.StorefrontItemHeader;
import com.vaadin.starter.bakery.ui.utils.converters.CurrencyFormatter;
import com.vaadin.starter.bakery.ui.utils.converters.LocalDateTimeConverter;
import com.vaadin.starter.bakery.ui.utils.converters.LocalTimeConverter;
import com.vaadin.starter.bakery.ui.utils.converters.LongToStringConverter;
import com.vaadin.starter.bakery.ui.utils.converters.OrderStateConverter;
import com.vaadin.starter.bakery.ui.view.storefront.converter.StorefrontLocalDateConverter;
import com.vaadin.ui.Tag;
import com.vaadin.ui.common.HtmlImport;
import com.vaadin.ui.event.ComponentEvent;
import com.vaadin.ui.event.ComponentEventListener;
import com.vaadin.ui.event.DomEvent;
import com.vaadin.ui.event.EventData;
import com.vaadin.ui.polymertemplate.PolymerTemplate;

@Tag("storefront-item-detail-wrapper")
@HtmlImport("src/storefront/storefront-item-detail-wrapper.html")
public class StorefrontItemDetailWrapper extends PolymerTemplate<StorefrontItemDetailWrapper.Model> {

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
		void setOrder(Order order);
		void setSelected(boolean selected);

		void setHeader(StorefrontItemHeader header);
		void setDisplayHeader(boolean displayHeader);
	}

	public StorefrontItemDetailWrapper() {
		getModel().setDisplayHeader(false);
		getModel().setSelected(false);
	}

	public void setOrder(Order order) {
		getModel().setOrder(order);
	}

	public void setSelected(boolean selected) {
		getModel().setSelected(selected);
	}

	public void setDisplayHeader(boolean displayHeader) {
		getModel().setDisplayHeader(displayHeader);
	}

	public void setHeader(StorefrontItemHeader header) {
		getModel().setHeader(header);
	}

	@DomEvent("opened")
	public static class OpenedEvent extends ComponentEvent<StorefrontItemDetailWrapper> {
		public OpenedEvent(StorefrontItemDetailWrapper source, boolean fromClient) {
			super(source, fromClient);
		}
	}

	public Registration addOpenedListener(ComponentEventListener<OpenedEvent> listener) {
		return addListener(OpenedEvent.class, listener);
	}

	@DomEvent("closed")
	public static class ClosedEvent extends ComponentEvent<StorefrontItemDetailWrapper> {
		public ClosedEvent(StorefrontItemDetailWrapper source, boolean fromClient) {
			super(source, fromClient);
		}
	}

	public Registration addClosedListener(ComponentEventListener<ClosedEvent> listener) {
		return addListener(ClosedEvent.class, listener);
	}

	@DomEvent("edit")
	public static class EditEvent extends ComponentEvent<StorefrontItemDetailWrapper> {
		public EditEvent(StorefrontItemDetailWrapper source, boolean fromClient) {
			super(source, fromClient);
		}
	}

	public Registration addEditListener(ComponentEventListener<EditEvent> listener) {
		return addListener(EditEvent.class, listener);
	}

	@DomEvent("commend-added")
	public static class CommentAddedEvent extends ComponentEvent<StorefrontItemDetailWrapper> {
		private final String orderId;
		private final String message;

		public CommentAddedEvent(StorefrontItemDetailWrapper source, boolean fromClient,
				@EventData("event.detail.orderId") String orderId,
				@EventData("event.detail.message") String message) {
			super(source, fromClient);
			this.orderId = orderId;
			this.message = message;
		}

		public String getOrderId() {
			return orderId;
		}

		public String getMessage() {
			return message;
		}
	}

	public Registration addCommentAddedListener(ComponentEventListener<CommentAddedEvent> listener) {
		return addListener(CommentAddedEvent.class, listener);
	}
}
