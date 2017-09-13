package com.vaadin.starter.bakery.ui.components.storefront;

import java.util.Collection;

import com.vaadin.annotations.HtmlImport;
import com.vaadin.annotations.Id;
import com.vaadin.annotations.Tag;
import com.vaadin.flow.template.PolymerTemplate;
import com.vaadin.flow.template.model.TemplateModel;
import com.vaadin.starter.bakery.backend.data.entity.Order;
import com.vaadin.starter.bakery.backend.data.entity.Product;
import com.vaadin.starter.bakery.backend.data.entity.User;

@Tag("order-edit-wrapper")
@HtmlImport("frontend://src/storefront/order-edit-wrapper.html")
public class OrderEditWrapper extends PolymerTemplate<OrderEditWrapper.Model> {

	@Id("orderEdit")
	private OrderEdit orderEdit;

	@Id("orderDetail")
	private OrderDetail orderDetail;
	
	public interface Model extends TemplateModel {
		void setReview(boolean review);
	}
	
	public void openEdit(Order order, User currentUser, Collection<Product> availableProducts, Runnable onSave,
			Runnable onCancel) {
		orderEdit.init(currentUser, availableProducts, this::review, onCancel);
		orderDetail.display(order, this::edit, onSave);
		orderEdit.setEditableItem(order);
		edit();
	}

	public void close() {
		orderEdit.close();
	}
	
	private void edit() {
		getModel().setReview(false);
	}
	
	private void review() {
		orderDetail.onReview();
		getModel().setReview(true);
	}

}
