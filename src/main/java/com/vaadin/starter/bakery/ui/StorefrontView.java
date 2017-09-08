/*
 * Copyright 2000-2017 Vaadin Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.vaadin.starter.bakery.ui;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.access.annotation.Secured;

import com.vaadin.annotations.ClientDelegate;
import com.vaadin.annotations.HtmlImport;
import com.vaadin.annotations.Tag;
import com.vaadin.flow.router.View;
import com.vaadin.flow.template.PolymerTemplate;
import com.vaadin.flow.template.model.TemplateModel;
import com.vaadin.hummingbird.ext.spring.annotations.ParentView;
import com.vaadin.hummingbird.ext.spring.annotations.Route;
import com.vaadin.starter.bakery.app.HasLogger;
import com.vaadin.starter.bakery.backend.data.Role;
import com.vaadin.starter.bakery.backend.data.entity.User;
import com.vaadin.starter.bakery.backend.service.OrderService;
import com.vaadin.starter.bakery.backend.service.ProductService;
import com.vaadin.starter.bakery.backend.service.UserService;
import com.vaadin.starter.bakery.ui.components.storefront.OrderEdit;
import com.vaadin.starter.bakery.ui.dataproviders.OrdersDataProvider;
import com.vaadin.starter.bakery.ui.entities.Order;
import com.vaadin.starter.bakery.ui.entities.Product;
import com.vaadin.starter.bakery.ui.utils.BakeryConst;
import com.vaadin.ui.AttachEvent;

import elemental.json.JsonObject;

/**
 * @author Vaadin Ltd
 *
 */
@Tag("bakery-storefront")
@HtmlImport("frontend://src/storefront/bakery-storefront.html")
@Route(BakeryConst.PAGE_STOREFRONT)
@Route(value = "")
@ParentView(BakeryApp.class)
@Secured(Role.BARISTA)
public class StorefrontView extends PolymerTemplate<StorefrontView.Model> implements View, HasLogger, HasToast {

	public interface Model extends TemplateModel {

		void setOrders(List<Order> orders);

		List<Order> getOrders();

		void setProducts(List<Product> products);

		void setSelectedOrder(Order order);

		void getSelectedOrder(Order order);
	}

	private ProductService productService;
	private OrdersDataProvider ordersProvider;
	private OrderService orderService;
	private OrderEdit orderEdit;
	private UserService userService;

	@Autowired
	public StorefrontView(OrdersDataProvider ordersProvider, ProductService productService, OrderService orderService,
			UserService userService) {
		this.productService = productService;
		this.ordersProvider = ordersProvider;
		this.orderService = orderService;
		this.userService = userService;
	}

	@Override
	protected void onAttach(AttachEvent event) {
		super.onAttach(event);
		getModel().setOrders(new ArrayList<>());

		getModel().setSelectedOrder(null);

	}

	@ClientDelegate
	private void onSave(JsonObject order) {
		try {
			ordersProvider.save(order);
		} catch (Exception e) {
			getLogger().debug("There was a problem while saving the order", e);
			toast("Order was not saved", true);
		} finally {
			getElement().callFunction("_onFiltersChanged");
		}
	}

	@ClientDelegate
	private void onFiltersChanged(String filter, boolean showPrevious) {
		// the hardcoded limit of 200 is here until lazy loading is implemented (see
		// BFF-120)
		PageRequest pr = new PageRequest(0, 200, Direction.ASC, "dueDate", "dueTime", "id");
		getModel().setOrders(ordersProvider.getOrdersList(filter, showPrevious, pr).getOrders());
	}

	@ClientDelegate
	private void addComment(String orderId, String message) {
		try {
			ordersProvider.addOrderComment(orderId, message);
		} catch (Exception e) {
			toast(e.getMessage(), true);
		} finally {
			updateOrderInModel(orderId);
		}
	}

	@ClientDelegate
	private void edit(String id) {
		com.vaadin.starter.bakery.backend.data.entity.Order order;
		User currentUser = userService.getCurrentUser();
		if(id == null) {
			order = new com.vaadin.starter.bakery.backend.data.entity.Order(currentUser);
			order.setDueTime(LocalTime.of(16, 0));
			order.setDueDate(LocalDate.now());
		}else {
			order = orderService.findOrder(Long.valueOf(id));			
		}

		Runnable saveOrder = () -> {
			orderService.saveOrder(order);
			closeEditor();
		};
		Runnable cancelEdit = () -> {
			this.closeEditor();
			if(order.getId() != null) {
				updateOrderInModel(order.getId().toString());
			}
		};

		orderEdit = new OrderEdit(currentUser, productService.getRepository().findAll(), saveOrder,
				cancelEdit);
		orderEdit.getElement().setAttribute("slot", "order-editor");
		orderEdit.setEditableItem(order);

		getElement().appendChild(orderEdit.getElement());

	}

	private void closeEditor() {
		getElement().removeChild(orderEdit.getElement());
		orderEdit = null;
	}

	private void updateOrderInModel(String orderId) {
		int idx = findOrderIndexInModel(orderId);
		if (idx == -1) {
			return;
		}

		try {
			getModel().getOrders().set(idx, ordersProvider.getOrder(orderId));
		} catch (Exception e) {
			// exclude the order from the model if ordersProvider.getOrder() throws
			getModel().setOrders(getModel().getOrders().stream().filter(order -> !order.getId().equals(orderId))
					.collect(Collectors.toList()));
		}
	}

	private int findOrderIndexInModel(String orderId) {
		List<Order> orders = getModel().getOrders();
		for (int i = 0; i < orders.size(); i += 1) {
			Order modelOrder = orders.get(i);
			if (modelOrder.getId().equals(orderId)) {
				return i;
			}
		}
		return -1;
	}
}
