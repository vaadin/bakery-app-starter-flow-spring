package com.vaadin.starter.bakery.ui.view.storefront;

import static com.vaadin.starter.bakery.ui.utils.TemplateUtil.addToSlot;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.vaadin.data.BeanValidationBinder;
import com.vaadin.data.ValidationException;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.flow.model.TemplateModel;
import com.vaadin.shared.Registration;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.starter.bakery.backend.data.OrderState;
import com.vaadin.starter.bakery.backend.data.entity.Order;
import com.vaadin.starter.bakery.backend.data.entity.PickupLocation;
import com.vaadin.starter.bakery.backend.data.entity.Product;
import com.vaadin.starter.bakery.backend.data.entity.User;
import com.vaadin.starter.bakery.ui.dataproviders.DataProviderUtil;
import com.vaadin.starter.bakery.ui.event.CancelEvent;
import com.vaadin.starter.bakery.ui.utils.FormattingUtils;
import com.vaadin.starter.bakery.ui.utils.converters.LocalTimeConverter;
import com.vaadin.starter.bakery.ui.view.workaround.ComboboxBinderWrapper;
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

	}

	@Id("order-edit-title")
	private H2 title;

	@Id("order-edit-status")
	private ComboBox<OrderState> status;

	@Id("due-date")
	private DatePicker date;

	@Id("due-time")
	private ComboBox<LocalTime> time;

	@Id("pickup-location")
	private ComboBox<PickupLocation> pickupLocation;

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

	private OrderItemsEdit items;

	private User currentUser;

	private BeanValidationBinder<Order> binder = new BeanValidationBinder<>(Order.class);

	private final LocalTimeConverter localTimeConverter = new LocalTimeConverter();

	private boolean hasChanges = false;

	@Autowired
	public OrderEdit(PickupLocationDataProvider locationProvider, ProductDataProvider productDataProvider) {
		items = new OrderItemsEdit(productDataProvider);
		addToSlot(this, items, "order-items-edit");

		cancel.addClickListener(e -> fireEvent(new CancelEvent(this, false)));
		review.addClickListener(e -> fireEvent(new ReviewEvent()));

		status.setDataProvider(DataProvider.ofItems(OrderState.values()));
		status.addValueChangeListener(e -> DataProviderUtil.convertIfNotNull(e.getValue(), OrderState::name));
		binder.forField(new ComboboxBinderWrapper<>(status)).bind("state");
		date.setValue(LocalDate.now());

		SortedSet<LocalTime> timeValues = IntStream.rangeClosed(8, 16).mapToObj(i -> LocalTime.of(i, 0))
				.collect(Collectors.toCollection(TreeSet::new));
		time.setItems(timeValues);
		time.setItemLabelGenerator(localTimeConverter::toPresentation);
		time.addCustomValueSetListener(e -> {
			timeValues.add(localTimeConverter.toModel(e.getDetail()));
			time.setItems(timeValues);
		});
		binder.forField(new ComboboxBinderWrapper<>(time)).bind("dueTime");

		pickupLocation.setDataProvider(locationProvider);
		pickupLocation.setRequired(true);
		binder.forField(new ComboboxBinderWrapper<>(pickupLocation)).bind("pickupLocation");

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
		binder.addValueChangeListener(e -> {
			if (e.getOldValue() != null) {
				review.setDisabled(!hasChanges());
			}
		});
	}

	public boolean hasChanges() {
		return binder.hasChanges() || items.hasChanges() || hasChanges;
	}

	private void updateDesktopViewOnItemsEdit() {
		getElement().callFunction("_updateDesktopViewOnItemsEdit");
	}

	public void init(User currentUser, Collection<Product> availableProducts) {
		this.currentUser = currentUser;
	}

	public void close() {
		items.reset();
		setTotalPrice(0);
		hasChanges = false;
	}

	public void write(Order order) throws ValidationException {
		order.setDueTime(time.getValue());
		order.setPickupLocation(pickupLocation.getValue());
		order.changeState(currentUser, status.getValue());

		binder.writeBean(order);
	}

	public void read(Order order) {
		binder.readBean(order);
		title.setText(String.format("%s Order", order.isNew() ? "New" : "Edit"));

		if (order.getState() != null) {
			getModel().setStatus(order.getState().name());
		}

		hasChanges = false;
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
