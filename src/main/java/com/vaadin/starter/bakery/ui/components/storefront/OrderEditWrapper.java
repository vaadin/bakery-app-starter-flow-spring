package com.vaadin.starter.bakery.ui.components.storefront;

import java.util.Collection;

import com.vaadin.flow.model.TemplateModel;
import com.vaadin.shared.Registration;
import com.vaadin.starter.bakery.backend.data.entity.Order;
import com.vaadin.starter.bakery.backend.data.entity.Product;
import com.vaadin.starter.bakery.backend.data.entity.User;
import com.vaadin.starter.bakery.backend.service.ProductService;
import com.vaadin.starter.bakery.backend.service.UserService;
import com.vaadin.ui.Tag;
import com.vaadin.ui.common.HtmlImport;
import com.vaadin.ui.event.ComponentEvent;
import com.vaadin.ui.event.ComponentEventListener;
import com.vaadin.ui.polymertemplate.PolymerTemplate;

import static com.vaadin.starter.bakery.ui.utils.TemplateUtil.addToSlot;

@Tag("order-edit-wrapper")
@HtmlImport("context://src/storefront/order-edit-wrapper.html")
public class OrderEditWrapper extends PolymerTemplate<OrderEditWrapper.Model> {

	public interface Model extends TemplateModel {
		void setOpened(boolean opened);
	}

	private OrderEdit orderEdit = new OrderEdit();

	private OrderDetail orderDetail = new OrderDetail();

	private Order order;

	public OrderEditWrapper(ProductService productService, UserService userService) {
		orderEdit.addCancelListener(e -> fireEvent(new CancelEvent(e.hasChanges())));
		orderEdit.addReviewListener(e -> this.details(true));
		orderDetail.addBackListener(e -> this.edit(true));
		orderDetail.addSaveListener(e -> fireEvent(new SaveEvent()));
		orderDetail.addEditListener(
				e -> openEdit(order, userService.getCurrentUser(), productService.getRepository().findAll()));
		orderDetail.addCancelListener(e -> fireEvent(new CancelEvent(false)));
	}

	public void openEdit(Order order, User currentUser, Collection<Product> availableProducts) {
		// This is a workaround for a Safari 11 issue.
		// If the orderEdit is injected into the page in the OrderEditWrapper constructor,
		// Safari fails to set the styles correctly.
		if (orderEdit.getElement().getParent() == null) {
			addToSlot(this, orderEdit, "detail-dialog");
		}

		this.order = order;
		orderEdit.init(currentUser, availableProducts);
		orderEdit.setEditableItem(order);
		getModel().setOpened(true);
		edit(false);
	}

	public void openDetails(Order order) {
		this.order = order;
		details(false);
		getModel().setOpened(true);
	}

	public void close() {
		orderEdit.close();
		this.order = null;
		getModel().setOpened(false);
	}

	private void edit(boolean initialHasChanges) {
		orderEdit.setInitialHasChanges(initialHasChanges);
		orderEdit.getElement().setAttribute("hidden", false);
		orderDetail.getElement().setAttribute("hidden", true);
	}

	private void details(boolean isReview) {
		// This is a workaround for a Safari 11 issue.
		// If the orderDetail is injected into the page in the OrderEditWrapper constructor,
		// Safari fails to set the styles correctly.
		if (orderDetail.getElement().getParent() == null) {
			addToSlot(this, orderDetail, "detail-dialog");
		}
		orderDetail.display(order, isReview);
		orderEdit.getElement().setAttribute("hidden", true);
		orderDetail.getElement().setAttribute("hidden", false);
	}

	public Registration addSaveListener(ComponentEventListener<SaveEvent> listener) {
		return addListener(SaveEvent.class, listener);
	}

	public Registration addCancelListener(ComponentEventListener<CancelEvent> listener) {
		return addListener(CancelEvent.class, listener);
	}

	public Registration addCommentListener(ComponentEventListener<OrderDetail.CommentEvent> listener) {
		return orderDetail.addCommentListener(listener);
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
