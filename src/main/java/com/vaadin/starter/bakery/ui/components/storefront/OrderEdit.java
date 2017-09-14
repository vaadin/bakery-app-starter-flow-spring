package com.vaadin.starter.bakery.ui.components.storefront;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.IntStream;

import com.vaadin.annotations.HtmlImport;
import com.vaadin.annotations.Id;
import com.vaadin.annotations.Tag;
import com.vaadin.data.BeanValidationBinder;
import com.vaadin.flow.html.Div;
import com.vaadin.flow.html.H2;
import com.vaadin.flow.html.Span;
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
import com.vaadin.ui.DatePicker;
import com.vaadin.ui.TextField;

@Tag("order-edit")
@HtmlImport("frontend://src/storefront/order-edit.html")
public class OrderEdit extends PolymerTemplate<OrderEdit.Model> implements HasToast {

	private BeanValidationBinder<Order> binder = new BeanValidationBinder<>(Order.class);

	private User currentUser;

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

	@Id("footer")
	private Div footer;

	private EditFooter editFooter;

	private Runnable onSave;
	private Runnable onCancel;

	public OrderEdit() {
		Runnable validateAndSave = () -> {
			if (binder.isValid()) {
				this.onSave.run();
			} else {
				toast("Please fill out all required fields before proceeding");
			}
		};
		editFooter = new EditFooter(() -> this.onCancel.run(), validateAndSave);
		footer.getElement().appendChild(editFooter.getElement());
		status.setItems(Arrays.stream(OrderState.values()).map(OrderState::getDisplayName));
		binder.forField(status).withConverter(new OrderStateConverter()).bind(Order::getState,
				(o, s) -> o.changeState(currentUser, s));
		binder.forField(date).bind("dueDate");

		time.setItems(IntStream.rangeClosed(8, 16).mapToObj(i -> LocalTime.of(i, 0).toString()));
		binder.forField(time).withConverter(new LocalTimeConverter()).bind("dueTime");

		pickupLocation.setItems("Bakery", "Store");
		binder.forField(pickupLocation).bind("pickupLocation.name");

		customerName.setRequired(true);
		binder.forField(customerName).bind("customer.fullName");
		customerNumber.setRequired(true);
		binder.forField(customerNumber).bind("customer.phoneNumber");
		binder.forField(customerDetails).bind("customer.details");
		binder.forField(items).bind("items");
		items.addPriceChangeListener(e -> editFooter.updatePrice(e.getTotalPrice()));

	}

	public void init(User currentUser, Collection<Product> availableProducts, Runnable onSave, Runnable onCancel) {
		this.onSave = onSave;
		this.onCancel = onCancel;
		this.currentUser = currentUser;

		items.setProducts(availableProducts);
	}

	public void close() {
		items.reset();
	}
	
	public interface Model extends TemplateModel {

		void setOpened(boolean opened);

	}

	public void setEditableItem(Order order) {
		getModel().setOpened(true);
		binder.setBean(order);
		boolean newOrder = order.getId() == null;
		title.setText(String.format("%s order", newOrder ? "New" : "Edit"));
	}
}

abstract class Footer extends PolymerTemplate<Footer.Model> {

	void updatePrice(int newPrice) {
		getModel().setPrice(FormattingUtils.formatAsCurrency(newPrice));
	}

	public interface Model extends TemplateModel {
		void setPrice(String price);
	}
}

@Tag("order-edit-edit-footer")
@HtmlImport("frontend://src/storefront/order-edit-edit-footer.html")
class EditFooter extends Footer {

	@Id("cancel")
	private Button cancel;

	@Id("review")
	private Button review;

	public EditFooter(Runnable onCancel, Runnable onReview) {
		cancel.addClickListener(e -> onCancel.run());
		review.addClickListener(e -> onReview.run());
	}
}
