package com.vaadin.starter.bakery.ui.views.storefront;

import static com.vaadin.starter.bakery.ui.utils.FormattingUtils.HOUR_FORMATTER;
import static com.vaadin.starter.bakery.ui.utils.FormattingUtils.MONTH_AND_DAY_FORMATTER;
import static com.vaadin.starter.bakery.ui.utils.FormattingUtils.SHORT_DAY_FORMATTER;
import static com.vaadin.starter.bakery.ui.utils.FormattingUtils.WEEKDAY_FULLNAME_FORMATTER;
import static com.vaadin.starter.bakery.ui.utils.FormattingUtils.WEEK_OF_YEAR_FIELD;

import java.time.LocalDate;
import java.util.List;

import com.vaadin.flow.data.renderer.TemplateRenderer;
import com.vaadin.starter.bakery.backend.data.entity.Order;
import com.vaadin.starter.bakery.backend.data.entity.OrderItem;
import com.vaadin.starter.bakery.backend.data.entity.OrderSummary;
import com.vaadin.starter.bakery.ui.utils.converters.OrderStateConverter;

/**
 * Help class to get ready to use TemplateRenderer for displaying order card list on the Storefront and Dashboard grids.
 * Using TemplateRenderer instead of ComponentRenderer optimizes the CPU and memory consumption.
 * <p>
 * In addition, component includes an optional header above the order card. It is used
 * to visually separate orders into groups. Technically all order cards are
 * equivalent, but those that do have the header visible create a visual group
 * separation.
 * <p>
 * Component css styles are located at /src/main/webapp/frontend/styles/order-card-style.html
 */
public class OrderCard {

	public static TemplateRenderer<Order> getTemplate() {
		return TemplateRenderer.of(
				  "<order-card"
				+ "  header='[[item.header]]'"
				+ "  order-card='[[item.orderCard]]'"
				+ "  on-click='cardClick'>"
				+ "</order-card>");
	}
	
	public static OrderCard create(OrderSummary order) {
		return new OrderCard(order);
	}

	private static OrderStateConverter stateConverter = new OrderStateConverter();

	private boolean recent, inWeek;

	private final OrderSummary order;
	
	public OrderCard(OrderSummary order) {
		this.order = order;
		LocalDate now = LocalDate.now();
		LocalDate date = order.getDueDate();
		recent = date.equals(now) || date.equals(now.minusDays(1));
		inWeek = !recent && now.getYear() == date.getYear() && now.get(WEEK_OF_YEAR_FIELD) == date.get(WEEK_OF_YEAR_FIELD);
	}

	public String getPlace() {
		return recent || inWeek ? order.getPickupLocation().getName() : null;
	}

	public String getTime() {
		return recent ? HOUR_FORMATTER.format(order.getDueTime()) : null;
	}

	public String getShortDay() {
		return inWeek ? SHORT_DAY_FORMATTER.format(order.getDueDate()) : null;
	}

	public String getSecondaryTime() {
		return inWeek ? HOUR_FORMATTER.format(order.getDueTime()) : null;
	}

	public String getMonth() {
		return recent || inWeek ? null : MONTH_AND_DAY_FORMATTER.format(order.getDueDate());
	}

	public String getFullDay() {
		return recent || inWeek ? null : WEEKDAY_FULLNAME_FORMATTER.format(order.getDueDate());
	}

	public String getState() {
		return stateConverter.toPresentation(order.getState());
	}

	public String getFullName() {
		return order.getCustomer().getFullName();
	}

	public List<OrderItem> getItems() {
		return order.getItems();
	}
}
