package com.vaadin.starter.bakery.ui.view.storefront;

import com.vaadin.flow.model.TemplateModel;
import com.vaadin.shared.Registration;
import com.vaadin.starter.bakery.backend.data.entity.Order;
import com.vaadin.starter.bakery.ui.entities.StorefrontItemHeader;
import com.vaadin.starter.bakery.ui.event.CancelEvent;
import com.vaadin.starter.bakery.ui.view.storefront.event.CommentEvent;
import com.vaadin.ui.Tag;
import com.vaadin.ui.button.Button;
import com.vaadin.ui.common.HasClickListeners;
import com.vaadin.ui.common.HtmlImport;
import com.vaadin.ui.event.ComponentEventListener;
import com.vaadin.ui.polymertemplate.Id;
import com.vaadin.ui.polymertemplate.PolymerTemplate;

@Tag("storefront-item-detail-wrapper")
@HtmlImport("src/storefront/storefront-item-detail-wrapper.html")
public class StorefrontItemDetailWrapper extends PolymerTemplate<StorefrontItemDetailWrapper.Model> {

	public interface Model extends TemplateModel {
		void setSelected(boolean selected);
		boolean getSelected();

		void setHeader(StorefrontItemHeader header);
		void setDisplayHeader(boolean displayHeader);
	}

	private Order order;

	@Id("storefront-item")
	private StorefrontItem storefrontItem;

	private OrderDetail orderDetail = new OrderDetail();

	public StorefrontItemDetailWrapper() {
		orderDetail.getElement().setAttribute("slot", "order-detail");
		getModel().setDisplayHeader(false);
		setSelected(false);
	}

	public void setOrder(Order order) {
		this.order = order;
		storefrontItem.setOrder(order);
		orderDetail.display(order, false);
	}

	public Order getOrder() {
		return order;
	}

	public void setSelected(boolean selected) {
		if (selected) {
			getElement().appendChild(orderDetail.getElement());
		} else {
			orderDetail.getElement().removeFromParent();
		}
		getModel().setSelected(selected);
	}

	public boolean isSelected() {
		return getModel().getSelected();
	}

	public void setHeader(StorefrontItemHeader header) {
		if (header == null) {
			getModel().setDisplayHeader(false);
			return;
		}
		getModel().setHeader(header);
		getModel().setDisplayHeader(true);
	}

	public Registration addEditListener(ComponentEventListener<HasClickListeners.ClickEvent<Button>> listener) {
		return orderDetail.addEditListener(listener);
	}

	public Registration addCommentListener(ComponentEventListener<CommentEvent> listener) {
		return orderDetail.addCommentListener(listener);
	}
	
	public Registration addCancelListener(ComponentEventListener<CancelEvent> listener) {
		return orderDetail.addCancelListener(listener);
	}
}
