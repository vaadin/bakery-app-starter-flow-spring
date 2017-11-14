package com.vaadin.starter.bakery.ui.view.storefront;

import static com.vaadin.starter.bakery.ui.utils.TemplateUtil.addToSlot;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.vaadin.data.BeanValidationBinder;
import com.vaadin.data.ValidationException;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.data.provider.Query;
import com.vaadin.flow.model.TemplateModel;
import com.vaadin.shared.Registration;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.starter.bakery.backend.data.OrderState;
import com.vaadin.starter.bakery.backend.data.entity.Order;
import com.vaadin.starter.bakery.backend.data.entity.Product;
import com.vaadin.starter.bakery.backend.data.entity.User;
import com.vaadin.starter.bakery.ui.dataproviders.DataProviderUtil;
import com.vaadin.starter.bakery.ui.event.CancelEvent;
import com.vaadin.starter.bakery.ui.utils.FormattingUtils;
import com.vaadin.starter.bakery.ui.utils.converters.LocalTimeConverter;
import com.vaadin.starter.bakery.ui.utils.converters.OrderStateConverter;
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

@Tag("order-edit")
@HtmlImport("src/storefront/order-edit.html")
@SpringComponent
@Scope("prototype")
public class OrderEdit extends PolymerTemplate<OrderEdit.Model> {

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
	private ComboBox<OrderState> status;

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

	private User currentUser;

	private BeanValidationBinder<Order> binder = new BeanValidationBinder<>(Order.class);

	private final LocalTimeConverter localTimeConverter = new LocalTimeConverter();

	private final OrderStateConverter orderStateConverter = new OrderStateConverter();

	private final PickupLocationDataProvider locationProvider;

	private boolean hasChanges = false;

	@Autowired
	public OrderEdit(PickupLocationDataProvider locationProvider) {
		this.locationProvider = locationProvider;
		addToSlot(this, items, "order-items-edit");

		cancel.addClickListener(e -> fireEvent(new CancelEvent(this, false)));
		review.addClickListener(e -> fireEvent(new ReviewEvent()));

		status.setDataProvider(DataProvider.ofItems(OrderState.values()));
		status.addValueChangeListener(e -> {
			getModel().setStatus(DataProviderUtil.toString(e.getValue()));
		});
		binder.forField(status).bind("state");

		date.setValue(LocalDate.now());
		binder.forField(date).bind("dueDate");

		ListDataProvider<String> timeDataProvider = DataProvider.fromStream(
				IntStream.rangeClosed(8, 16).mapToObj(i -> localTimeConverter.toPresentation(LocalTime.of(i, 0))));
		time.setDataProvider(timeDataProvider);
		time.addValueChangeListener(e -> setHasChanges(true));

		pickupLocation.setDataProvider(locationProvider);
		pickupLocation.addValueChangeListener(e -> setHasChanges(true));
		pickupLocation.setRequired(true);

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

	private void setHasChanges(boolean hasChanges) {
		this.hasChanges = hasChanges;
		review.setDisabled(!hasChanges());
	}

	public boolean hasChanges() {
		return binder.hasChanges() || items.hasChanges() || hasChanges;
	}

	private void updateDesktopViewOnItemsEdit() {
		getElement().callFunction("_updateDesktopViewOnItemsEdit");
	}

	public void init(User currentUser, Collection<Product> availableProducts) {
		this.currentUser = currentUser;
		items.setProducts(availableProducts);
	}

	public void close() {
		items.reset();
		getModel().setTime("");
		getModel().setStatusValue("");
		getModel().setPickupLocation("");
		setTotalPrice(0);
	}

	public void write(Order order) throws ValidationException {
		order.setDueTime(localTimeConverter.toModel(time.getValue()));
		Query<String, String> locationQuery = new Query<>(0, 1, Collections.emptyList(), null,
				pickupLocation.getValue());
		locationProvider.findLocations(locationQuery).stream().findFirst().ifPresent(p -> order.setPickupLocation(p));
		order.changeState(currentUser, status.getValue());

		binder.writeBean(order);
	}

	public void read(Order order) {
		binder.readBean(order);
		title.setText(String.format("%s Order", order.isNew() ? "New" : "Edit"));
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

	private void setTotalPrice(int totalPrice) {
		getModel().setTotalPrice(FormattingUtils.formatAsCurrency(totalPrice));
	}

	public class ReviewEvent extends ComponentEvent<OrderEdit> {

		ReviewEvent() {
			super(OrderEdit.this, false);
		}
	}

}
