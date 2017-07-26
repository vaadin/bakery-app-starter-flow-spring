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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;

import com.vaadin.annotations.ClientDelegate;
import com.vaadin.annotations.HtmlImport;
import com.vaadin.annotations.Tag;
import com.vaadin.flow.router.View;
import com.vaadin.flow.template.PolymerTemplate;
import com.vaadin.flow.template.model.TemplateModel;
import com.vaadin.hummingbird.ext.spring.annotations.ParentView;
import com.vaadin.hummingbird.ext.spring.annotations.Route;
import com.vaadin.starter.bakery.ui.dataproviders.OrdersDataProvider;
import com.vaadin.starter.bakery.ui.dataproviders.ProductsDataProvider;
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
public class StorefrontView extends PolymerTemplate<StorefrontView.Model> implements View {

	public interface Model extends TemplateModel {
		void setOrders(List<Order> orders);

		List<Order> getOrders();

		void setProducts(List<Product> products);
	}

	private ProductsDataProvider productProvider;
	private OrdersDataProvider ordersProvider;

	@Autowired
	public StorefrontView(OrdersDataProvider ordersProvider, ProductsDataProvider productProvider) {
		this.productProvider = productProvider;
		this.ordersProvider = ordersProvider;
	}

	@Override
	protected void onAttach(AttachEvent event) {
		super.onAttach(event);
		getModel().setOrders(new ArrayList<>());

		getModel().setProducts(productProvider.findAll());
	}

	@ClientDelegate
	private void onSave(JsonObject order) {
		try {
			ordersProvider.save(order);
		} catch (Exception e) {
			getElement().callFunction("showErrorMessage", "Order was not saved");
		} finally {
			getElement().callFunction("_onFiltersChanged");
		}
	}

	@ClientDelegate
	private void onFiltersChanged(String filter, boolean showPrevious) {
		// the hardcoded limit of 200 is here until lazy loading is implemented (see BFF-120)
		PageRequest pr = new PageRequest(0, 200, Direction.ASC, "dueDate", "dueTime");
		getModel().setOrders(ordersProvider.getOrdersList(filter, showPrevious, pr).getOrders());
	}

	@ClientDelegate
	private void addComment(String orderId, String message) {
		try {
			ordersProvider.addOrderComment(orderId, message);
		} catch (Exception e) {
			getElement().callFunction("showErrorMessage", e.getMessage());
		} finally {
			updateOrderInModel(orderId);
		}
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
			getModel().setOrders(
					getModel().getOrders().stream()
							.filter(order -> !order.getId().equals(orderId))
							.collect(Collectors.toList()));
		}
	}

	private int findOrderIndexInModel(String orderId) {
		int idx = -1;
		List<Order> orders = getModel().getOrders();
		for (int i = 0; i < orders.size(); i += 1) {
			Order modelOrder = orders.get(i);
			if (modelOrder.getId().equals(orderId)) {
				idx = i;
				break;
			}
		}
		return idx;
	}
}
