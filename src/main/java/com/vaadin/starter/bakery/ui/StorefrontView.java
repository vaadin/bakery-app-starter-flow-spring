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

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.annotations.ClientDelegate;
import com.vaadin.annotations.HtmlImport;
import com.vaadin.annotations.Tag;
import com.vaadin.flow.router.View;
import com.vaadin.flow.template.PolymerTemplate;
import com.vaadin.flow.template.model.TemplateModel;
import com.vaadin.hummingbird.ext.spring.annotations.ParentView;
import com.vaadin.hummingbird.ext.spring.annotations.Route;
import com.vaadin.starter.bakery.backend.service.OrderService;
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

	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("EEE, MMM dd");

	public static class OrderGroupModel {
		private String mainTitle;
		private String secondaryTitle;
		private List<Order> orders;

		public OrderGroupModel() {
		}

		public OrderGroupModel(String mainTitle, String secondaryTitle, List<Order> orders) {
			this.mainTitle = mainTitle;
			this.secondaryTitle = secondaryTitle;
			this.orders = orders;
		}

		public String getMainTitle() {
			return mainTitle;
		}

		public void setMainTitle(String mainTitle) {
			this.mainTitle = mainTitle;
		}

		public String getSecondaryTitle() {
			return secondaryTitle;
		}

		public void setSecondaryTitle(String secondaryTitle) {
			this.secondaryTitle = secondaryTitle;
		}

		public List<Order> getOrders() {
			return orders;
		}

		public void setOrders(List<Order> orders) {
			this.orders = orders;
		}
	}

	public interface Model extends TemplateModel {
		void setOrderGroups(List<OrderGroupModel> orderGroups);

		void setProducts(List<Product> products);
	}

	private OrderService orderService;

	@Autowired
	public StorefrontView(OrderService orderService) {
		this.orderService = orderService;
	}

	@Override
	protected void onAttach(AttachEvent event) {
		super.onAttach(event);
		getModel().setOrderGroups(new ArrayList<>());

		getModel().setProducts(ProductsDataProvider.getUiProducts());
	}

	@ClientDelegate
	private void onSave(JsonObject order) {
		try {
			OrdersDataProvider.get().save(order);
		} catch (Exception e) {
			getElement().callFunction("showErrorMessage", "Order was not saved");
		} finally {
			getElement().callFunction("_onFiltersChanged");
		}
	}

	@ClientDelegate
	private void onFiltersChanged(String filter, boolean showPrevious) {
		LocalDate startOfToday = LocalDate.now();
		LocalDate startOfTomorrow = startOfToday.plusDays(1);
		LocalDate startOfNextWeek = startOfToday.with(TemporalAdjusters.next(DayOfWeek.MONDAY));

		List<Order> previous = new ArrayList<>();
		List<Order> today = new ArrayList<>();
		List<Order> thisWeek = new ArrayList<>();
		List<Order> upcoming = new ArrayList<>();

		orderService
				.findAnyMatchingAfterDueDate(Optional.of(filter),
						showPrevious ? Optional.empty() : Optional.of(startOfToday.minusDays(1)), null)
				.forEach(order -> {
					LocalDate date = order.getDueDate();
					List<Order> group = upcoming;
					if (date.isBefore(startOfToday)) {
						group = previous;
					} else if (date.isBefore(startOfTomorrow)) {
						group = today;
					} else if (date.isBefore(startOfNextWeek)) {
						group = thisWeek;
					}
					group.add(OrdersDataProvider.toUIEntity(order));
				});

		List<OrderGroupModel> groups = new ArrayList<>();
		if (!previous.isEmpty()) {
			groups.add(new OrderGroupModel("Previous", "Yesterday and earlier", previous));
		}
		if (!today.isEmpty()) {
			groups.add(new OrderGroupModel("Today", DATE_FORMATTER.format(startOfToday), today));
		}
		if (!thisWeek.isEmpty()) {
			groups.add(new OrderGroupModel("This week", DATE_FORMATTER.format(startOfTomorrow) + " – "
					+ DATE_FORMATTER.format(startOfNextWeek.minusDays(1)), thisWeek));
		}
		if (!upcoming.isEmpty()) {
			groups.add(new OrderGroupModel("Upcoming", "After this week", upcoming));
		}
		getModel().setOrderGroups(groups);
	}
}
