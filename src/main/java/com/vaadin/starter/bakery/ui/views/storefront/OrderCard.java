package com.vaadin.starter.bakery.ui.views.storefront;

import static com.vaadin.starter.bakery.ui.utils.FormattingUtils.HOUR_FORMATTER;
import static com.vaadin.starter.bakery.ui.utils.FormattingUtils.MONTH_AND_DAY_FORMATTER;
import static com.vaadin.starter.bakery.ui.utils.FormattingUtils.SHORT_DAY_FORMATTER;
import static com.vaadin.starter.bakery.ui.utils.FormattingUtils.WEEKDAY_FULLNAME_FORMATTER;
import static com.vaadin.starter.bakery.ui.utils.FormattingUtils.WEEK_OF_YEAR_FIELD;

import java.time.LocalDate;
import java.util.List;

import com.vaadin.flow.data.renderer.TemplateRenderer;
import com.vaadin.starter.bakery.backend.data.entity.Customer;
import com.vaadin.starter.bakery.backend.data.entity.Order;
import com.vaadin.starter.bakery.backend.data.entity.OrderItem;
import com.vaadin.starter.bakery.backend.data.entity.OrderSummary;
import com.vaadin.starter.bakery.ui.utils.converters.OrderStateConverter;

public class OrderCard {
	
	public static TemplateRenderer<Order> getTemplate() {
		return TemplateRenderer.<Order>of(
			  "<div class='order-card'>"
			+ "  <div class='oc-content'>\n"
			+ "     <div class='oc-group-heading' hidden$='[[!item.header]]'>\n"
			+ "       <span class='oc-main'>[[item.header.main]]</span>\n"
			+ "       <span class='oc-secondary'>[[item.header.secondary]]</span>\n"
			+ "     </div>\n"
			+ "     <div class='oc-wrapper'>\n"
			+ "       <div class='oc-info-wrapper'>\n"
			+ "         <order-status-badge class='oc-badge' status='[[item.order.state]]'></order-status-badge>\n"
			+ "         <div class='oc-time-place'>"
			+ "           <h3 class='oc-time'>[[item.order.time]]</h3>"
			+ "           <h3 class='oc-short-day'>[[item.order.shortDay]]</h3>"
			+ "           <h3 class='oc-month'>[[item.order.month]]</h3>"
			+ "           <div class='oc-secondary-time'>[[item.order.secondaryTime]]</div>"
			+ "           <div class='oc-full-day'>[[item.order.fullDay]]</div>"
			+ "           <div class='oc-place'>[[item.order.place]]</div>"
			+ "         </div>\n"
			+ "       </div>\n"
			+ "       <div class='oc-name-items'>\n"
			+ "         <h3 class='oc-name'>[[item.order.customer.fullName]]</h3>\n"
			+ "         <div class='oc-goods'>\n"
			+ "           <template is='dom-repeat' items='[[item.order.items]]'>\n"
			+ "             <div class='oc-goods-item'>\n"
			+ "               <span class='count'>[[item.quantity]]</span>\n"
			+ "               <div>[[item.product.name]]</div>\n"
			+ "             </div>\n"
			+ "           </template>\n"
			+ "         </div>\n"
			+ "       </div>\n"
			+ "     </div>\n"
			+ "  </div>"
			+ "</div>");
	}
	
	public static OrderCard create(OrderSummary order) {
		return new OrderCard(order);
	}

	private OrderStateConverter stateConverter = new OrderStateConverter();

	private boolean recent, inWeek;

	private final OrderSummary order;
	
	public OrderCard(OrderSummary order) {
		this.order = order;
		LocalDate now = LocalDate.now();
		LocalDate date = order.getDueDate();
		recent = date.equals(now) || date.equals(now.minusDays(1));
		inWeek = now.getYear() == date.getYear() && now.get(WEEK_OF_YEAR_FIELD) == date.get(WEEK_OF_YEAR_FIELD);
	}

	public String getPlace() {
		return order.getPickupLocation().getName();
	}

	public String getTime() {
		return recent ? HOUR_FORMATTER.format(order.getDueTime()) : "";
	}

	public String getShortDay() {
		return inWeek ? SHORT_DAY_FORMATTER.format(order.getDueDate()) : "";
	}

	public String getSecondaryTime() {
		return inWeek ? HOUR_FORMATTER.format(order.getDueTime()) : "";
	}

	public String getMonth() {
		return recent || inWeek ? "" : MONTH_AND_DAY_FORMATTER.format(order.getDueDate());
	}

	public String getFullDay() {
		return recent || inWeek ? "" : WEEKDAY_FULLNAME_FORMATTER.format(order.getDueDate());
	}

	public String getState() {
		return stateConverter.toPresentation(order.getState());
	}

	public Customer getCustomer() {
		return order.getCustomer();
	}

	public List<OrderItem> getItems() {
		return order.getItems();
	}
}
