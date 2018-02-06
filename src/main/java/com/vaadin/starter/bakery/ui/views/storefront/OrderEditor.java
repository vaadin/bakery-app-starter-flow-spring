package com.vaadin.starter.bakery.ui.views.storefront;

import static com.vaadin.starter.bakery.ui.dataproviders.DataProviderUtil.createItemLabelGenerator;
import static com.vaadin.starter.bakery.ui.utils.TemplateUtil.addToSlot;

import java.time.LocalTime;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.BindingValidationStatus;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.validator.BeanValidator;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.templatemodel.TemplateModel;
import com.vaadin.starter.bakery.backend.data.OrderState;
import com.vaadin.starter.bakery.backend.data.entity.Order;
import com.vaadin.starter.bakery.backend.data.entity.PickupLocation;
import com.vaadin.starter.bakery.backend.data.entity.User;
import com.vaadin.starter.bakery.ui.components.ComboBoxForBinder;
import com.vaadin.starter.bakery.ui.components.DatePickerForBinder;
import com.vaadin.starter.bakery.ui.dataproviders.DataProviderUtil;
import com.vaadin.starter.bakery.ui.events.CancelEvent;
import com.vaadin.starter.bakery.ui.utils.FormattingUtils;
import com.vaadin.starter.bakery.ui.utils.converters.LocalTimeConverter;
import com.vaadin.starter.bakery.ui.views.storefront.events.ReviewEvent;
import com.vaadin.starter.bakery.ui.views.storefront.events.ValueChangeEvent;

@Tag("order-editor")
@HtmlImport("src/views/storefront/order-editor.html")
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class OrderEditor extends PolymerTemplate<OrderEditor.Model> {

	public interface Model extends TemplateModel {
		void setOpened(boolean opened);

		void setTotalPrice(String totalPrice);

		void setStatus(String status);
	}

	@Id("title")
	private H2 title;

	@Id("status")
	private ComboBoxForBinder<OrderState> status;

	@Id("dueDate")
	private DatePickerForBinder dueDate;

	@Id("dueTime")
	private ComboBoxForBinder<LocalTime> dueTime;

	@Id("pickupLocation")
	private ComboBoxForBinder<PickupLocation> pickupLocation;

	@Id("customerName")
	private TextField customerName;

	@Id("customerNumber")
	private TextField customerNumber;

	@Id("customerDetails")
	private TextField customerDetails;

	@Id("cancel")
	private Button cancel;

	@Id("review")
	private Button review;

	private OrderItemsEditor items;

	private User currentUser;
	
	private BeanValidationBinder<Order> binder = new BeanValidationBinder<>(Order.class);

	private final LocalTimeConverter localTimeConverter = new LocalTimeConverter();

	@Autowired
	public OrderEditor(PickupLocationDataProvider locationProvider, ProductDataProvider productDataProvider) {
		items = new OrderItemsEditor(productDataProvider);
		addToSlot(this, items, "order-items-editor");

		cancel.addClickListener(e -> fireEvent(new CancelEvent(this, false)));
		review.addClickListener(e -> fireEvent(new ReviewEvent(this)));

		status.setItemLabelGenerator(createItemLabelGenerator(OrderState::getDisplayName));
		status.setDataProvider(DataProvider.ofItems(OrderState.values()));
		status.addValueChangeListener(
				e -> getModel().setStatus(DataProviderUtil.convertIfNotNull(e.getValue(), OrderState::name)));
		binder.forField(status)
				.withValidator(new BeanValidator(Order.class, "state"))
				.bind(Order::getState, (o, s) -> {
					o.changeState(currentUser, s);
				});

		dueDate.setRequired(true);
		binder.bind(dueDate, "dueDate");

		SortedSet<LocalTime> timeValues = IntStream.rangeClosed(8, 16).mapToObj(i -> LocalTime.of(i, 0))
				.collect(Collectors.toCollection(TreeSet::new));
		dueTime.setItems(timeValues);
		dueTime.setItemLabelGenerator(localTimeConverter::toPresentation);
		dueTime.addCustomValueSetListener(e -> {
			timeValues.add(localTimeConverter.toModel(e.getDetail()));
			dueTime.setItems(timeValues);
		});
		binder.bind(dueTime, "dueTime");

		pickupLocation.setItemLabelGenerator(createItemLabelGenerator(PickupLocation::getName));
		pickupLocation.setDataProvider(locationProvider);
		binder.bind(pickupLocation, "pickupLocation");
		pickupLocation.setRequired(false);

		customerName.setRequired(true);
		binder.bind(customerName, "customer.fullName");

		customerNumber.setRequired(true);
		binder.bind(customerNumber, "customer.phoneNumber");

		binder.bind(customerDetails, "customer.details");

		items.setRequiredIndicatorVisible(true);
		binder.bind(items, "items");

		items.addPriceChangeListener(e -> setTotalPrice(e.getTotalPrice()));

		items.addListener(ValueChangeEvent.class, e -> review.setEnabled(hasChanges()));
		binder.addValueChangeListener(e -> {
			if (e.getOldValue() != null) {
				review.setEnabled(hasChanges());
			}
		});
	}

	public boolean hasChanges() {
		return binder.hasChanges() || items.hasChanges();
	}

	public void clear() {
		binder.readBean(null);
	}

	public void close() {
		setTotalPrice(0);
	}

	public void write(Order order) throws ValidationException {
		binder.writeBean(order);
	}

	public void read(Order order) {
		binder.readBean(order);
		title.setText(String.format("%s Order", order.isNew() ? "New" : "Edit"));

		if (order.getState() != null) {
			getModel().setStatus(order.getState().name());
		}

		review.setEnabled(false);
	}

	public Stream<HasValue<?, ?>> validate() {
		Stream<HasValue<?, ?>> errorFields = binder.validate().getFieldValidationErrors().stream()
				.map(BindingValidationStatus::getField);

		return Stream.concat(errorFields, items.validate());
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

	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}
}
