package com.vaadin.starter.bakery.ui.components.storefront;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.vaadin.data.BeanValidationBinder;
import com.vaadin.data.ValidationException;
import com.vaadin.flow.model.TemplateModel;
import com.vaadin.shared.Registration;
import com.vaadin.starter.bakery.backend.data.OrderState;
import com.vaadin.starter.bakery.backend.data.entity.Order;
import com.vaadin.starter.bakery.backend.data.entity.PickupLocation;
import com.vaadin.starter.bakery.backend.data.entity.Product;
import com.vaadin.starter.bakery.backend.data.entity.User;
import com.vaadin.starter.bakery.ui.HasToast;
import com.vaadin.starter.bakery.ui.converters.LocalTimeConverter;
import com.vaadin.starter.bakery.ui.utils.FormattingUtils;
import com.vaadin.ui.Tag;
import com.vaadin.ui.button.Button;
import com.vaadin.ui.combobox.ComboBox;
import com.vaadin.ui.common.HtmlImport;
import com.vaadin.ui.datepicker.DatePicker;
import com.vaadin.ui.event.ComponentEvent;
import com.vaadin.ui.event.ComponentEventListener;
import com.vaadin.ui.html.H2;
import com.vaadin.ui.polymertemplate.Id;
import com.vaadin.ui.polymertemplate.PolymerTemplate;
import com.vaadin.ui.textfield.TextField;

import static com.vaadin.starter.bakery.ui.utils.TemplateUtil.addToSlot;

@Tag("order-edit")
@HtmlImport("context://src/storefront/order-edit.html")
public class OrderEdit extends PolymerTemplate<OrderEdit.Model> implements HasToast {

	public interface Model extends TemplateModel {

		void setOpened(boolean opened);

		void setTotalPrice(String totalPrice);

		void setStatus(String status);

		void setStatusValue(String statusValue);

		void setTime(String order);

		void setPickupLocation(String pickupLocation);
	}

	@Id("order-edit-title")
	private H2 title;

	@Id("order-edit-status")
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

	@Id("order-edit-cancel")
	private Button cancel;

	@Id("review")
	private Button review;

	private OrderItemsEdit items = new OrderItemsEdit();

	private Order order;

	private boolean initialHasChanges;

	private User currentUser;

	private BeanValidationBinder<Order> binder = new BeanValidationBinder<>(Order.class);

	private final LocalTimeConverter localTimeConverter = new LocalTimeConverter();

	private final OrderStateConverter orderStateConverter = new OrderStateConverter();

	private boolean isDirty = false;

	public OrderEdit() {
		addToSlot(this, items, "order-items-edit");

		cancel.addClickListener(e -> fireEvent(new CancelEvent(hasChanges())));
		review.addClickListener(e -> {
			try {
				order.setDueTime(localTimeConverter.toModel(time.getValue()));
				PickupLocation location = new PickupLocation();
				location.setName(pickupLocation.getValue());
				order.setPickupLocation(location);
				order.changeState(currentUser, OrderState.forDisplayName(status.getValue()));
				binder.writeBean(this.order);
				fireEvent(new ReviewEvent());
			} catch (ValidationException ex) {
				toast("Please fill out all required fields before proceeding.");
			}
		});

		status.setItems(Arrays.stream(OrderState.values()).map(OrderState::getDisplayName));
		status.addValueChangeListener(e -> {
			getModel().setStatus(e.getValue());
			setIsDirty(true);
		});

		date.setValue(LocalDate.now());
		binder.forField(date).bind("dueDate");

		final LocalTimeConverter localTimeConverter = new LocalTimeConverter();
		final Stream<String> defaultTimes = IntStream.rangeClosed(8, 16)
				.mapToObj(i -> localTimeConverter.toPresentation(LocalTime.of(i, 0)));
		time.setItems(defaultTimes);
		time.addValueChangeListener(e -> setIsDirty(true));

		pickupLocation.setItems("Bakery", "Store");
		pickupLocation.addValueChangeListener(e -> setIsDirty(true));

		customerName.setRequired(true);
		binder.forField(customerName).bind("customer.fullName");
		customerNumber.setRequired(true);
		binder.forField(customerNumber).bind("customer.phoneNumber");
		binder.forField(customerDetails).bind("customer.details");

		items.setRequiredIndicatorVisible(true);
		binder.forField(items).bind("items");
		items.addPriceChangeListener(e -> setTotalPrice(e.getTotalPrice()));

		items.addListener(OrderItemsEdit.ValueChangeEvent.class, e -> review.setDisabled(!hasChanges()));
		items.addListener(OrderItemsEdit.NewEditorEvent.class, e -> updateDesktopViewOnItemsEdit());
		binder.addValueChangeListener(e -> review.setDisabled(!hasChanges()));
	}

	public void setInitialHasChanges(boolean initialHasChanges) {
		this.initialHasChanges = initialHasChanges;
	}

	public void setIsDirty(boolean isDirty) {
		this.isDirty = isDirty;
		review.setDisabled(!hasChanges());
	}

	private boolean hasChanges() {
		return initialHasChanges || binder.hasChanges() || items.hasChanges() || isDirty;
	}

	private void updateDesktopViewOnItemsEdit() {
		getElement().callFunction("_updateDesktopViewOnItemsEdit");
	}

	public void init(User currentUser, Collection<Product> availableProducts) {
		this.currentUser = currentUser;
		items.setProducts(availableProducts);
	}

	public void close() {
		this.order = null;
		items.reset();
		getModel().setTime("");
		getModel().setStatusValue("");
		getModel().setPickupLocation("");
		this.setTotalPrice(0);
		getModel().setStatus(null);
	}

	public void setEditableItem(Order order) {
		this.order = order;
		this.initialHasChanges = false;
		getModel().setOpened(true);
		binder.readBean(order);
		boolean newOrder = order.getId() == null;
		title.setText(String.format("%s Order", newOrder ? "New" : "Edit"));

		getModel().setTime(localTimeConverter.toPresentation(order.getDueTime()));

		if (order.getState() != null) {
			String status = orderStateConverter.toPresentation(order.getState());
			getModel().setStatusValue(status);
			getModel().setStatus(status);
		}

		if (order.getPickupLocation() != null) {
			getModel().setPickupLocation(order.getPickupLocation().getName());
		}
		review.setDisabled(true);
		updateDesktopViewOnItemsEdit();
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
