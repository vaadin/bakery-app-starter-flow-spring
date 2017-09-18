package com.vaadin.starter.bakery.ui.components.storefront;

import java.util.Collection;

import com.vaadin.annotations.HtmlImport;
import com.vaadin.annotations.Id;
import com.vaadin.annotations.Tag;
import com.vaadin.flow.event.ComponentEventListener;
import com.vaadin.flow.template.PolymerTemplate;
import com.vaadin.flow.template.model.TemplateModel;
import com.vaadin.shared.Registration;
import com.vaadin.starter.bakery.backend.data.entity.Order;
import com.vaadin.starter.bakery.backend.data.entity.Product;
import com.vaadin.starter.bakery.backend.data.entity.User;
import com.vaadin.starter.bakery.ui.components.storefront.OrderItemEdit.DeleteEvent;
import com.vaadin.starter.bakery.ui.components.storefront.OrderItemEdit.ProductChangeEvent;
import com.vaadin.ui.ComponentEvent;

@Tag("order-edit-wrapper")
@HtmlImport("frontend://src/storefront/order-edit-wrapper.html")
public class OrderEditWrapper extends PolymerTemplate<OrderEditWrapper.Model> {

	@Id("orderEdit")
	private OrderEdit orderEdit;

	@Id("orderDetail")
	private OrderDetail orderDetail;

	private Order order;
	
	public interface Model extends TemplateModel {
		void setReview(boolean review);
	}

	public OrderEditWrapper() {
		orderEdit.addCancelListener(e -> fireEvent(new CancelEvent()));
		orderEdit.addReviewListener(e -> this.review());
		orderDetail.addBackListener(e -> this.edit());
		orderDetail.addSaveListener(e -> fireEvent(new SaveEvent()));
	}

	public void openEdit(Order order, User currentUser, Collection<Product> availableProducts) {
		this.order = order;
		orderEdit.init(currentUser, availableProducts);
		orderEdit.setEditableItem(order);

		edit();
	}

	public void close() {
		orderEdit.close();
		this.order = null;
	}

	private void edit() {
		getModel().setReview(false);
	}

	private void review() {
		final boolean review = true;
		orderDetail.display(order,review);
		getModel().setReview(review);
	}

	public Registration addSaveListener(ComponentEventListener<SaveEvent> listener) {
		return addListener(SaveEvent.class, listener);
	}

	public Registration addCancelListener(ComponentEventListener<CancelEvent> listener) {
		return addListener(CancelEvent.class, listener);
	}

	public class CancelEvent extends ComponentEvent<OrderEditWrapper> {
		CancelEvent() {
			super(OrderEditWrapper.this, false);
		}
	}

	public class SaveEvent extends ComponentEvent<OrderEditWrapper> {
		
		SaveEvent() {
			super(OrderEditWrapper.this, false);
		}
		
		public Order getOrder() {
			return order;
		}
	}
}
