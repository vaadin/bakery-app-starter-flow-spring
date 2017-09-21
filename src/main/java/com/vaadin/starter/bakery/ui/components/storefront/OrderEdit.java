package com.vaadin.starter.bakery.ui.components.storefront;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.vaadin.annotations.HtmlImport;
import com.vaadin.annotations.Id;
import com.vaadin.annotations.Tag;
import com.vaadin.data.BeanValidationBinder;
import com.vaadin.data.ValidationException;
import com.vaadin.flow.event.ComponentEventListener;
import com.vaadin.flow.html.H2;
import com.vaadin.flow.template.PolymerTemplate;
import com.vaadin.flow.template.model.TemplateModel;
import com.vaadin.shared.Registration;
import com.vaadin.starter.bakery.backend.data.OrderState;
import com.vaadin.starter.bakery.backend.data.entity.Order;
import com.vaadin.starter.bakery.backend.data.entity.Product;
import com.vaadin.starter.bakery.backend.data.entity.User;
import com.vaadin.starter.bakery.ui.HasToast;
import com.vaadin.starter.bakery.ui.converters.LocalTimeConverter;
import com.vaadin.starter.bakery.ui.utils.FormattingUtils;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.ComponentEvent;
import com.vaadin.ui.DatePicker;
import com.vaadin.ui.TextField;

@Tag("order-edit")
@HtmlImport("frontend://src/storefront/order-edit.html")
public class OrderEdit extends PolymerTemplate<OrderEdit.Model> implements HasToast {

	public interface Model extends TemplateModel {

		void setOpened(boolean opened);

		void setTotalPrice(String totalPrice);

		void setStatus(String status);
	}

	@Id("title")
	private H2 title;

	@Id("status")
	private ComboBox<String> status;

	@Id("due-date")
	private DatePicker date;

	@Id("due-time")
	private ComboBox<String> time;

	@Id("pickup-location")
	private ComboBox<String> pickupLocation;

	@Id("customer-name")
	private TextField customerName;

	@Id("customer-number")
	private TextField customerNumber;

	@Id("customer-details")
	private TextField customerDetails;

	@Id("items")
	private OrderItemsEdit items;

	@Id("cancel")
	private Button cancel;

	@Id("review")
	private Button review;

	private Order order;

	private User currentUser;

	private BeanValidationBinder<Order> binder = new BeanValidationBinder<>(Order.class);

	public OrderEdit() {
		cancel.addClickListener(e -> fireEvent(new CancelEvent(binder.hasChanges())));
		review.addClickListener(e -> {
			try {
				binder.writeBean(this.order);
				fireEvent(new ReviewEvent());
			} catch (ValidationException ex) {
				toast("Please fill out all required fields before proceeding.");
			}
		});

		status.setItems(Arrays.stream(OrderState.values()).map(OrderState::getDisplayName));
		status.addValueChangeListener(e -> getModel().setStatus(e.getValue()));

		binder.forField(status).withConverter(new OrderStateConverter()).bind(Order::getState,
				(o, s) -> o.changeState(currentUser, s));
		binder.forField(date).bind("dueDate");

		final LocalTimeConverter localTimeConverter = new LocalTimeConverter();
		final Stream<String> defaultTimes = IntStream.rangeClosed(8, 16)
				.mapToObj(i -> localTimeConverter.toPresentation(LocalTime.of(i, 0)));
		time.setItems(defaultTimes);
		binder.forField(time).withConverter(localTimeConverter).bind("dueTime");

		pickupLocation.setItems("Bakery", "Store");
		binder.forField(pickupLocation).bind("pickupLocation.name");

		customerName.setRequired(true);
		binder.forField(customerName).bind("customer.fullName");
		customerNumber.setRequired(true);
		binder.forField(customerNumber).bind("customer.phoneNumber");
		binder.forField(customerDetails).bind("customer.details");

		items.setRequiredIndicatorVisible(true);
		binder.forField(items).bind("items");
		items.addPriceChangeListener(e -> setTotalPrice(e.getTotalPrice()));

		binder.addValueChangeListener(e -> review.setDisabled(false));
	}

	public void init(User currentUser, Collection<Product> availableProducts) {
		this.currentUser = currentUser;
		items.setProducts(availableProducts);
	}

	public void close() {
		this.order = null;
		items.reset();
		this.setTotalPrice(0);
		getModel().setStatus(null);
	}

	public void setEditableItem(Order order) {
		this.order = order;
		getModel().setOpened(true);
		binder.readBean(order);
		boolean newOrder = order.getId() == null;
		title.setText(String.format("%s Order", newOrder ? "New" : "Edit"));
		review.setDisabled(true);
	}

	public Registration addReviewListener(ComponentEventListener<ReviewEvent> listener) {
		return addListener(ReviewEvent.class, listener);
	}

	public Registration addCancelListener(ComponentEventListener<CancelEvent> listener) {
		return addListener(CancelEvent.class, listener);
	}

	private void setTotalPrice(int totalPrice) {
		getModel().setTotalPrice(FormattingUtils.formatAsCurrency(totalPrice));
	}

	public class ReviewEvent extends ComponentEvent<OrderEdit> {

		ReviewEvent() {
			super(OrderEdit.this, false);
		}
	}

	public class CancelEvent extends ComponentEvent<OrderEdit> {

		private final boolean hasChanges;

		CancelEvent(boolean hasChanges) {
			super(OrderEdit.this, false);
			this.hasChanges = hasChanges;
		}

		public boolean hasChanges() {
			return hasChanges;
		}
	}

}
