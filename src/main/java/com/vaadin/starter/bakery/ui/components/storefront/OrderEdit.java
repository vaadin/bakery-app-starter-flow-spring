package com.vaadin.starter.bakery.ui.components.storefront;

import com.vaadin.annotations.Convert;
import com.vaadin.annotations.Exclude;
import com.vaadin.annotations.HtmlImport;
import com.vaadin.annotations.Id;
import com.vaadin.annotations.Tag;
import com.vaadin.data.BeanValidationBinder;
import com.vaadin.flow.html.H2;
import com.vaadin.flow.router.View;
import com.vaadin.flow.template.PolymerTemplate;
import com.vaadin.flow.template.model.TemplateModel;
import com.vaadin.starter.bakery.backend.data.OrderState;
import com.vaadin.starter.bakery.backend.data.entity.Order;
import com.vaadin.starter.bakery.backend.data.entity.User;
import com.vaadin.starter.bakery.ui.converters.EnumConverter;
import com.vaadin.starter.bakery.ui.converters.LocalDateConverter;
import com.vaadin.starter.bakery.ui.converters.LocalTimeConverter;
import com.vaadin.starter.bakery.ui.converters.LongToStringConverter;
import com.vaadin.starter.bakery.ui.dataproviders.DataProviderUtil;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DatePicker;

@Tag("order-edit-wrapper")
@HtmlImport("frontend://src/storefront/order-edit-wrapper.html")
public class OrderEdit  extends PolymerTemplate<OrderEdit.Model> implements View {


	private BeanValidationBinder<Order> binder = new BeanValidationBinder<>(Order.class);

	private User currentUser;

	@Id("title")
	private H2 title;

	@Id("status")
	private ComboBox<OrderState> status;
	
	@Id("due-date")
	private DatePicker date;
	
	
	
	
	public OrderEdit() {
		status.setItems(OrderState.values());
		binder.forField(status).bind(o -> DataProviderUtil.convertIfNotNull(o.getState(), Object::toString),
				(o, s) -> DataProviderUtil.convertIfNotNull(s, OrderState::valueOf));
		binder.forField(date).bind(Order::getDueDate, Order::setDueDate);
	}

	public interface Model extends TemplateModel {
		
		@Convert(value=LocalDateConverter.class,path="dueDate")
		@Convert(value=LocalTimeConverter.class,path="dueTime")
		@Convert(value = LongToStringConverter.class, path = "id")
		@Convert(value = LongToStringConverter.class, path = "items.id")
		@Convert(value = LongToStringConverter.class, path = "items.product.id")
		@Convert(value = LongToStringConverter.class, path = "pickupLocation.id")
		@Convert(value = LongToStringConverter.class, path = "customer.id")
		@Convert(value = LongToStringConverter.class, path = "history.id")
		@Convert(value = LongToStringConverter.class, path = "history.createdBy.id")
		@Convert(value = OrderStateConverter.class, path = "state")
		@Convert(value = OrderStateConverter.class, path = "history.newState")
		@Exclude({"history.timestamp","history.createdBy.email","history.createdBy.password","history.createdBy.role","history.createdBy.photoUrl","history.createdBy.locked"})
		void setEditableItem(Order order);
		void setReview(boolean review);
		void setOpened(boolean opened);
		
		
	}

	public void setEditableItem(Order order) {
		getModel().setEditableItem(order);
		getModel().setOpened(true);
	}

	public static class OrderStateConverter extends EnumConverter<OrderState> {

		public OrderStateConverter() {
			super(OrderState.class);
		}

	}
}
