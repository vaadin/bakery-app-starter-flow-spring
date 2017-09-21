package com.vaadin.starter.bakery.ui.components.storefront;

import static com.vaadin.starter.bakery.ui.utils.TemplateUtil.addToSlot;

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
import com.vaadin.starter.bakery.ui.utils.TemplateUtil;
import com.vaadin.ui.ComponentEvent;

@Tag("order-edit-wrapper")
@HtmlImport("context://src/storefront/order-edit-wrapper.html")
public class OrderEditWrapper extends PolymerTemplate<OrderEditWrapper.Model> {

	private OrderEdit orderEdit = new OrderEdit();

	private OrderDetail orderDetail = new OrderDetail();

	private Order order;

	public interface Model extends TemplateModel {
		void setReview(boolean review);

		void setOpened(boolean opened);
	}

	public OrderEditWrapper() {
		addToSlot(this, orderEdit, "detail-dialog");
		addToSlot(this, orderDetail, "detail-dialog");

		orderEdit.addCancelListener(e -> fireEvent(new CancelEvent(e.hasChanges())));
		orderEdit.addReviewListener(e -> this.review());
		orderDetail.addBackListener(e -> this.edit());
		orderDetail.addSaveListener(e -> fireEvent(new SaveEvent()));
	}

	public void openEdit(Order order, User currentUser, Collection<Product> availableProducts) {
		this.order = order;
		orderEdit.init(currentUser, availableProducts);
		orderEdit.setEditableItem(order);
		getModel().setOpened(true);
		edit();
	}

	public void close() {
		orderEdit.close();
		this.order = null;
		getModel().setOpened(false);
	}

	private void edit() {
		getModel().setReview(false);
		orderEdit.getElement().setAttribute("hidden", false);
		orderDetail.getElement().setAttribute("hidden", true);
	}

	private void review() {
		final boolean review = true;
		orderDetail.display(order, review);
		getModel().setReview(review);
		orderEdit.getElement().setAttribute("hidden", true);
		orderDetail.getElement().setAttribute("hidden", false);
	}

	public Registration addSaveListener(ComponentEventListener<SaveEvent> listener) {
		return addListener(SaveEvent.class, listener);
	}

	public Registration addCancelListener(ComponentEventListener<CancelEvent> listener) {
		return addListener(CancelEvent.class, listener);
	}

	public class CancelEvent extends ComponentEvent<OrderEditWrapper> {
		private final boolean hasChanges;

		CancelEvent(boolean hasChanges) {
			super(OrderEditWrapper.this, false);
			this.hasChanges = hasChanges;
		}

		public boolean hasChanges() {
			return hasChanges;
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
