package com.vaadin.starter.bakery.ui.views.storefront;

import static com.vaadin.starter.bakery.ui.utils.FormattingUtils.HOUR_FORMATTER;
import static com.vaadin.starter.bakery.ui.utils.FormattingUtils.MONTH_AND_DAY_FORMATTER;
import static com.vaadin.starter.bakery.ui.utils.FormattingUtils.SHORT_DAY_FORMATTER;
import static com.vaadin.starter.bakery.ui.utils.FormattingUtils.WEEKDAY_FULLNAME_FORMATTER;
import static com.vaadin.starter.bakery.ui.utils.FormattingUtils.WEEK_OF_YEAR_FIELD;

import java.time.LocalDate;

import com.vaadin.starter.bakery.backend.data.entity.OrderSummary;

/**
 * The component displaying order cards for the list on the Storefront and Dashboard views.
 * <p>
 * In addition, it includes an optional header above the order card. It is used
 * to visually separate orders into groups. Technically all order cards are
 * equivalent, but those that do have the header visible create a visual group
 * separation.
 */

public class OrderCard {

	public static OrderCardTimePlace createComponents(OrderSummary order) {
		LocalDate now = LocalDate.now();
		LocalDate date = order.getDueDate();
		OrderCardTimePlace result = new OrderCardTimePlace();

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

	public static final String ORDER_CARD_TEMPLATE =
			  "<div class='order-card'>"
			+ "  <div class='oc-content'>\n"
			+ "     <div class='oc-group-heading' hidden$='[[!item.header]]'>\n"
			+ "       <span class='oc-main'>[[item.header.main]]</span>\n"
			+ "       <span class='oc-secondary'>[[item.header.secondary]]</span>\n"
			+ "     </div>\n"
			+ "     <div class='oc-wrapper' on-click='cardClick'>\n"
			+ "       <div class='oc-info-wrapper'>\n"
			+ "         <order-status-badge class='oc-badge' status='[[item.state]]'></order-status-badge>\n"
			+ "         <div class='oc-time-place'>"
			+ "		      <h3 class='oc-time'>[[item.timePlace.time]]</h3>"
			+ "	          <h3 class='oc-short-day'>[[item.timePlace.shortDay]]</h3>"
			+ "	          <h3 class='oc-month'>[[item.timePlace.month]]</h3>"
			+ "	          <div class='oc-secondary-time'>[[item.timePlace.secondaryTime]]</div>"
			+ "    	      <div class='oc-full-day'>[[item.timePlace.fullDay]]</div>"
			+ "	          <div class='oc-place'>[[item.timePlace.place]]</div>"
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

}
