package com.vaadin.starter.bakery.ui.components.storefront;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.vaadin.data.BeanValidationBinder;
import com.vaadin.data.Result;
import com.vaadin.data.ValidationException;
import com.vaadin.data.ValueContext;
import com.vaadin.flow.model.TemplateModel;
import com.vaadin.shared.Registration;
import com.vaadin.starter.bakery.backend.data.OrderState;
import com.vaadin.starter.bakery.backend.data.entity.Order;
import com.vaadin.starter.bakery.backend.data.entity.PickupLocation;
import com.vaadin.starter.bakery.backend.data.entity.Product;
import com.vaadin.starter.bakery.backend.data.entity.User;
import com.vaadin.starter.bakery.ui.HasToast;
import com.vaadin.starter.bakery.ui.converters.LocalTimeConverter;
import com.vaadin.starter.bakery.ui.converters.binder.BinderConverter;
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

	private User currentUser;

	private BeanValidationBinder<Order> binder = new BeanValidationBinder<>(Order.class);

	public OrderEdit() {
		addToSlot(this, items, "order-items-edit");

		cancel.addClickListener(e -> fireEvent(new CancelEvent(binder.hasChanges() || items.hasChanges())));
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

		binder.forField(status).withConverter(new OrderStateConverter())
				.bind(Order::getState, (o, s) -> o.changeState(currentUser, s));

		date.setValue(LocalDate.now());
		binder.forField(date).bind("dueDate");

		final LocalTimeConverter localTimeConverter = new LocalTimeConverter();
		final Stream<String> defaultTimes = IntStream.rangeClosed(8, 16)
				.mapToObj(i -> localTimeConverter.toPresentation(LocalTime.of(i, 0)));
		time.setItems(defaultTimes);
		binder.forField(time).withConverter(localTimeConverter).bind("dueTime");

		pickupLocation.setItems("Bakery", "Store");
		binder.forField(pickupLocation).withConverter(new BinderConverter<String, PickupLocation>() {
			@Override
			public String convertNullToPresentation(PickupLocation modelValue, ValueContext valueContext) {
				return "";
			}

			@Override
			public Result<PickupLocation> convertToModelIfNotNull(String presentationValue, ValueContext valueContext) {
				PickupLocation location = new PickupLocation();
				location.setName(presentationValue);
				return Result.ok(location);
			}

			@Override
			public String convertToPresentationIfNotNull(PickupLocation modelValue, ValueContext valueContext) {
				if (modelValue.getName() == null) {
					return "";
				}
				return modelValue.getName();
			}
		}).bind(Order::getPickupLocation, Order::setPickupLocation);

		customerName.setRequired(true);
		binder.forField(customerName).bind("customer.fullName");
		customerNumber.setRequired(true);
		binder.forField(customerNumber).bind("customer.phoneNumber");
		binder.forField(customerDetails).bind("customer.details");

		items.setRequiredIndicatorVisible(true);
		binder.forField(items).bind("items");
		items.addPriceChangeListener(e -> setTotalPrice(e.getTotalPrice()));

		items.addListener(OrderItemsEdit.ValueChangeEvent.class, e -> review.setDisabled(false));
		items.addListener(OrderItemsEdit.NewEditorEvent.class, e -> updateDesktopViewOnItemsEdit());
		binder.addValueChangeListener(e -> review.setDisabled(false));
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
