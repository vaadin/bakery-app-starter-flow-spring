package com.vaadin.starter.bakery.ui.view.storefront;

import com.vaadin.flow.model.TemplateModel;
import com.vaadin.shared.Registration;
import com.vaadin.starter.bakery.backend.data.entity.Order;
import com.vaadin.starter.bakery.ui.entities.StorefrontItemHeader;
import com.vaadin.starter.bakery.ui.view.storefront.event.ExpandedEvent;
import com.vaadin.ui.Tag;
import com.vaadin.ui.button.Button;
import com.vaadin.ui.common.HasClickListeners;
import com.vaadin.ui.common.HtmlImport;
import com.vaadin.ui.event.ComponentEvent;
import com.vaadin.ui.event.ComponentEventListener;
import com.vaadin.ui.event.DomEvent;
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

	@Id("order-detail")
	private OrderDetail orderDetail;

	public StorefrontItemDetailWrapper() {
		orderDetail.addCancelListener(
				e -> this.fireEvent(new CollapsedEvent(this, false)));
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

	public Registration addExpandedListener(ComponentEventListener<ExpandedEvent> listener) {
		return addListener(ExpandedEvent.class, listener);
	}

	@DomEvent("collapsed")
	public static class CollapsedEvent extends ComponentEvent<StorefrontItemDetailWrapper> {
		public CollapsedEvent(StorefrontItemDetailWrapper source, boolean fromClient) {
			super(source, fromClient);
		}
	}

	public Registration addCollapsedListener(ComponentEventListener<CollapsedEvent> listener) {
		return addListener(CollapsedEvent.class, listener);
	}

	public Registration addEditListener(ComponentEventListener<HasClickListeners.ClickEvent<Button>> listener) {
		return orderDetail.addEditListener(listener);
	}

	public Registration addCommentListener(ComponentEventListener<OrderDetail.CommentEvent> listener) {
		return orderDetail.addCommentListener(listener);
	}
}
