package com.vaadin.starter.bakery.ui.components.storefront;

import java.time.LocalTime;
import java.util.Collection;
import java.util.stream.IntStream;

import com.vaadin.annotations.HtmlImport;
import com.vaadin.annotations.Id;
import com.vaadin.annotations.Tag;
import com.vaadin.data.BeanValidationBinder;
import com.vaadin.flow.html.Div;
import com.vaadin.flow.html.H2;
import com.vaadin.flow.template.PolymerTemplate;
import com.vaadin.flow.template.model.TemplateModel;
import com.vaadin.starter.bakery.backend.data.OrderState;
import com.vaadin.starter.bakery.backend.data.entity.Order;
import com.vaadin.starter.bakery.backend.data.entity.Product;
import com.vaadin.starter.bakery.backend.data.entity.User;
import com.vaadin.starter.bakery.ui.HasToast;
import com.vaadin.starter.bakery.ui.converters.EnumConverter;
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
	@Id("items")
	private OrderItemsEdit items;

	@Id("footer")
	private Div footer;

	private EditFooter editFooter;
	private ReviewFooter reviewFooter;

	public OrderEdit(User currentUser, Collection<Product> availableProducts, Runnable onSave, Runnable onCancel) {
		items.setProducts(availableProducts);
		status.setItems(OrderState.values());
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

		editFooter = new EditFooter(onCancel, this::review);
		reviewFooter = new ReviewFooter(this::edit, onSave);
		items.setOnTotalPriceChanged((totalPrice) -> {
			editFooter.updatePrice(totalPrice);
			reviewFooter.updatePrice(totalPrice);
		});
		edit();
	}

	private void edit() {
		binder.setReadOnly(false);
		footer.getElement().removeAllChildren();
		footer.getElement().appendChild(editFooter.getElement());
	}

	private void review() {
		if (binder.isValid()) {
			binder.setReadOnly(true);
			footer.getElement().removeAllChildren();
			footer.getElement().appendChild(reviewFooter.getElement());
		} else {
			toast("Please fill out all required fields before proceeding");
		}
	}

	public interface Model extends TemplateModel {

		void setOpened(boolean opened);

	}

	public void setEditableItem(Order order) {
		// getModel().setEditableItem(order);
		getModel().setOpened(true);
		binder.setBean(order);
	}

	public static class OrderStateConverter extends EnumConverter<OrderState> {

		public OrderStateConverter() {
			super(OrderState.class);
		}

	}

	enum Status {
		EDIT, REVIEW;
	}
}

abstract class Footer extends PolymerTemplate<TemplateModel> {
	@Id("price")
	private Div price;

	void updatePrice(int newPrice) {
		price.getElement().setText(FormattingUtils.formatAsCurrency(newPrice));
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

@Tag("order-edit-review-footer")
@HtmlImport("frontend://src/storefront/order-edit-review-footer.html")
class ReviewFooter extends Footer {

	@Id("back")
	private Button back;

	@Id("save")
	private Button save;

	public ReviewFooter(Runnable onBack, Runnable onSave) {
		back.addClickListener(e -> onBack.run());
		save.addClickListener(e -> onSave.run());
	}
}
