package com.vaadin.starter.bakery.ui.view.storefront;

import static com.vaadin.starter.bakery.ui.utils.TemplateUtil.addToSlot;

import com.vaadin.flow.model.TemplateModel;
import com.vaadin.shared.Registration;
import com.vaadin.starter.bakery.backend.data.entity.Order;
import com.vaadin.starter.bakery.backend.data.entity.OrderSummary;
import com.vaadin.starter.bakery.ui.entities.StorefrontItemHeader;
import com.vaadin.starter.bakery.ui.event.CancelEvent;
import com.vaadin.starter.bakery.ui.view.storefront.event.CommentEvent;
import com.vaadin.starter.bakery.ui.view.storefront.event.EditEvent;
import com.vaadin.ui.Tag;
import com.vaadin.ui.common.HtmlImport;
import com.vaadin.ui.event.ComponentEventListener;
import com.vaadin.ui.polymertemplate.Id;
import com.vaadin.ui.polymertemplate.PolymerTemplate;

/**
 * The component for expandable order cards for the list on the Storefront view.
 * When collapsed the order card shows brief order details, and switches to full
 * order details when expanded.
 * <p>
 * In addition, it includes an optional header above the order card. It is used
 * to visually separate orders into groups. Technically all order cards are
 * equivalent, but those that do have the header visible create a visual group
 * separation.
 */
@Tag("storefront-order-card")
@HtmlImport("src/storefront/storefront-order-card.html")
public class StorefrontOrderCard extends PolymerTemplate<StorefrontOrderCard.Model> {

	public interface Model extends TemplateModel {
		void setSelected(boolean selected);

		void setHeader(StorefrontItemHeader header);
		void setDisplayHeader(boolean displayHeader);
	}

	private OrderSummary order;

	@Id("order-details-brief")
	private OrderDetailsBrief orderDetailsBrief;
	
	private OrderDetailsFull orderDetailsFull;

	public StorefrontOrderCard() {
		getModel().setDisplayHeader(false);
		getModel().setSelected(false);
	}

	public void setOrder(OrderSummary order) {
		this.order = order;
		orderDetailsBrief.setOrder(order);
	}

	public void updateOrder(Order fullOrder) {
		setOrder(fullOrder);
		orderDetailsFull.display(fullOrder, false);
	}

	public OrderSummary getOrder() {
		return order;
	}

	public void openCard(Order fullOrder) {
		orderDetailsFull = createOrderDetailsFull();
		addToSlot(this, orderDetailsFull, "order-details-full");
		updateOrder(fullOrder);
		getModel().setSelected(true);
	}

	public void closeCard() {
		if (orderDetailsFull != null) {
			orderDetailsFull.getElement().removeFromParent();
			orderDetailsFull = null;
		}
		getModel().setSelected(false);
	}

	public void setHeader(StorefrontItemHeader header) {
		if (header == null) {
			getModel().setDisplayHeader(false);
			return;
		}
		getModel().setHeader(header);
		getModel().setDisplayHeader(true);
	}
	
	private OrderDetailsFull createOrderDetailsFull() {
		OrderDetailsFull result = new OrderDetailsFull();
		result.addEditListener(this::fireEvent);
		result.addCommentListener(this::fireEvent);
		result.addCancelListener(this::fireEvent);
		return result;
	}

	public Registration addEditListener(ComponentEventListener<EditEvent> listener) {
		return addListener(EditEvent.class, listener);
	}

	public Registration addCommentListener(ComponentEventListener<CommentEvent> listener) {
		return addListener(CommentEvent.class,listener);
	}
	
	public Registration addCancelListener(ComponentEventListener<CancelEvent> listener) {
		return addListener(CancelEvent.class,listener);
	}
}
