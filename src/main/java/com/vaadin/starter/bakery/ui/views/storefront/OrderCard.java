package com.vaadin.starter.bakery.ui.views.storefront;

import static com.vaadin.starter.bakery.ui.utils.FormattingUtils.HOUR_FORMATTER;
import static com.vaadin.starter.bakery.ui.utils.FormattingUtils.MONTH_AND_DAY_FORMATTER;
import static com.vaadin.starter.bakery.ui.utils.FormattingUtils.SHORT_DAY_FORMATTER;
import static com.vaadin.starter.bakery.ui.utils.FormattingUtils.WEEKDAY_FULLNAME_FORMATTER;
import static com.vaadin.starter.bakery.ui.utils.FormattingUtils.WEEK_OF_YEAR_FIELD;

import java.time.LocalDate;

import com.vaadin.flow.data.renderer.TemplateRenderer;
import com.vaadin.starter.bakery.backend.data.entity.Order;
import com.vaadin.starter.bakery.backend.data.entity.OrderSummary;

public class OrderCard {
	
	public static final String ORDER_CARD_TEMPLATE =
			  "<div class='order-card'>"
			+ "  <div class='oc-content'>\n"
			+ "     <div class='oc-group-heading' hidden$='[[!item.header]]'>\n"
			+ "       <span class='oc-main'>[[item.header.main]]</span>\n"
			+ "       <span class='oc-secondary'>[[item.header.secondary]]</span>\n"
			+ "     </div>\n"
			+ "     <div class='oc-wrapper'>\n"
			+ "       <div class='oc-info-wrapper'>\n"
			+ "         <order-status-badge class='oc-badge' status='[[item.state]]'></order-status-badge>\n"
			+ "         <div class='oc-time-place'>"
					+ "           <h3 class='oc-time'>[[item.timePlace.time]]</h3>"
					+ "           <h3 class='oc-short-day'>[[item.timePlace.shortDay]]</h3>"
					+ "           <h3 class='oc-month'>[[item.timePlace.month]]</h3>"
					+ "           <div class='oc-secondary-time'>[[item.timePlace.secondaryTime]]</div>"
					+ "           <div class='oc-full-day'>[[item.timePlace.fullDay]]</div>"
					+ "           <div class='oc-place'>[[item.timePlace.place]]</div>"
			+ "         </div>\n"
			+ "       </div>\n"
			+ "       <div class='oc-name-items'>\n"
			+ "         <h3 class='oc-name'>[[item.customer.fullName]]</h3>\n"
			+ "         <div class='oc-goods'>\n"
			+ "           <template is='dom-repeat' items='[[item.items]]'>\n"
			+ "             <div class='oc-goods-item'>\n"
			+ "               <span class='count'>[[item.quantity]]</span>\n"
			+ "               <div>[[item.product.name]]</div>\n"
			+ "             </div>\n"
			+ "           </template>\n"
			+ "         </div>\n"
			+ "       </div>\n"
			+ "     </div>\n"
			+ "  </div>"
			+ "</div>";

	public static TemplateRenderer<Order> getTemplate() {
		return TemplateRenderer.<Order> of(OrderCard.ORDER_CARD_TEMPLATE);
	}
	
	public static OrderCard create(OrderSummary order) {
		LocalDate now = LocalDate.now();
		LocalDate date = order.getDueDate();
		OrderCard result = new OrderCard();

		if (date.equals(now) || date.equals(now.minusDays(1))) {
			// Today or yesterday
			result.setTime(HOUR_FORMATTER.format(order.getDueTime()));
			result.setPlace(order.getPickupLocation().getName());
		} else if (now.getYear() == date.getYear() && now.get(WEEK_OF_YEAR_FIELD) == date.get(WEEK_OF_YEAR_FIELD)) {
			// This week
			result.setShortDay(SHORT_DAY_FORMATTER.format(order.getDueDate()));
			result.setSecondaryTime(HOUR_FORMATTER.format(order.getDueTime()));
			result.setPlace(order.getPickupLocation().getName());
		} else {
			// Other dates
			result.setMonth(MONTH_AND_DAY_FORMATTER.format(order.getDueDate()));
			result.setFullDay(WEEKDAY_FULLNAME_FORMATTER.format(order.getDueDate()));
		}
		return result;
	}
	
	private String place;
	private String time;
	private String shortDay;
	private String secondaryTime;
	private String month;
	private String fullDay;

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getShortDay() {
		return shortDay;
	}

	public void setShortDay(String shortDay) {
		this.shortDay = shortDay;
	}

	public String getSecondaryTime() {
		return secondaryTime;
	}

	public void setSecondaryTime(String secondarytime) {
		this.secondaryTime = secondarytime;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getFullDay() {
		return fullDay;
	}

	public void setFullDay(String fullDay) {
		this.fullDay = fullDay;
	}
}
