/**
 * 
 */
package com.vaadin.starter.bakery.ui.view.storefront;

import java.util.function.BiConsumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.starter.bakery.backend.data.entity.Order;
import com.vaadin.starter.bakery.backend.data.entity.User;
import com.vaadin.starter.bakery.backend.service.OrderService;
import com.vaadin.starter.bakery.ui.crud.EntityPresenter;

import com.vaadin.flow.component.Focusable;
import com.vaadin.flow.component.HasValue;

@SpringComponent
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SingleOrderPresenter {

	private final OrderService orderService;
	private final EntityPresenter<Order> entityPresenter;
	private final User currentUser;
	private StorefrontView view;
	private BiConsumer<Order, Boolean> onClose;

	@Autowired
	public SingleOrderPresenter(OrderService orderService, EntityPresenter<Order> entityPresenter, User currentUser) {
		this.orderService = orderService;
		this.entityPresenter = entityPresenter;
		this.currentUser = currentUser;
	}

	void init(StorefrontView view) {
		this.view = view;
		entityPresenter.setView(view);
		view.setupSingleOrderListeners(this);
	}

	void openOrder(Long id, boolean edit, BiConsumer<Order, Boolean> onClose) {
		this.onClose = onClose;
		entityPresenter.loadEntity(id, e -> open(e, edit));
	}

	void createOrder(BiConsumer<Order, Boolean> onClose) {
		this.onClose = onClose;
		open(entityPresenter.createNew(), true);
	}

	void cancel() {
		entityPresenter.cancel(() -> close(false));
	}

	void edit() {
		setElementsVisibility(true);
		view.getOpenedOrderEditor().read(entityPresenter.getEntity());
	}

	void back() {
		setElementsVisibility(true);
	}

	void review() {
		HasValue<?, ?> firstErrorField = view.validate().findFirst().orElse(null);
		if (firstErrorField == null) {
			if (entityPresenter.writeEntity()) {
				setElementsVisibility(false);
				view.getOpenedOrderDetails().display(entityPresenter.getEntity(), true);
			}
		} else if (firstErrorField instanceof Focusable) {
			((Focusable<?>) firstErrorField).focus();
		}
	}

	void save() {
		entityPresenter.save(e -> close(true));
	}

	void addComment(String comment) {
		if (entityPresenter.executeUpdate(e -> orderService.addComment(currentUser, e, comment))) {
			// You can only add comments when in view mode, so reopening in that state.
			open(entityPresenter.getEntity(), false);
		}
	}

	private void open(Order order, boolean edit) {
		view.setEditing(true);
		setElementsVisibility(edit);
		if (edit) {
			view.getOpenedOrderEditor().read(order);
		} else {
			view.getOpenedOrderDetails().display(order, false);
		}
	}

	private void setElementsVisibility(boolean editing) {
		view.getOpenedOrderDetails().setVisible(!editing);
		view.getOpenedOrderEditor().setVisible(editing);
	}

	private void close(boolean updated) {
		view.getOpenedOrderEditor().close();
		view.setEditing(false);
		onClose.accept(entityPresenter.getEntity(), updated);
		entityPresenter.close();
	}

}
