package com.vaadin.starter.bakery.ui.views.storefront;

import static com.vaadin.starter.bakery.ui.utils.FormattingUtils.HOUR_FORMATTER;
import static com.vaadin.starter.bakery.ui.utils.FormattingUtils.MONTH_AND_DAY_FORMATTER;
import static com.vaadin.starter.bakery.ui.utils.FormattingUtils.SHORT_DAY_FORMATTER;
import static com.vaadin.starter.bakery.ui.utils.FormattingUtils.WEEKDAY_FULLNAME_FORMATTER;
import static com.vaadin.starter.bakery.ui.utils.FormattingUtils.WEEK_OF_YEAR_FIELD;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HtmlContainer;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.templatemodel.Convert;
import com.vaadin.flow.templatemodel.Include;
import com.vaadin.flow.templatemodel.TemplateModel;
import com.vaadin.starter.bakery.backend.data.entity.Order;
import com.vaadin.starter.bakery.backend.data.entity.OrderSummary;
import com.vaadin.starter.bakery.ui.utils.converters.LongToStringConverter;
import com.vaadin.starter.bakery.ui.utils.converters.OrderStateConverter;

/**
 * The component for expandable order cards for the list on the Storefront view.
 * When collapsed the order card shows brief order details, and switches to full
 * order details when expanded.
 * <p>
 * In addition, it includes an optional header above the order card. It is used
 * to visually separate orders into groups. Technically all order cards are
 * equivalent, but those that do have the header visible create a visual group
 * separation.
 */
@Tag("order-card")
@HtmlImport("src/views/storefront/order-card.html")
public class OrderCard extends PolymerTemplate<OrderCard.Model> {

	public interface Model extends TemplateModel {
		void setHeader(OrderCardHeader header);

		void setDisplayHeader(boolean displayHeader);

		@Include({ "id", "state", "customer.fullName", "items.product.name", "items.quantity" })
		@Convert(value = LongToStringConverter.class, path = "id")
		@Convert(value = OrderStateConverter.class, path = "state")
		void setItem(OrderSummary order);
	}

	private Long orderId;

	@Id("timePlace")
	private Div timePlace;

	public OrderCard() {
		getModel().setDisplayHeader(false);
	}

	public void setOrder(OrderSummary order) {
		orderId = order.getId();
		getModel().setItem(order);
		timePlace.removeAll();
		createComponents(order).forEach(timePlace::add);
	}

	public void updateOrder(Order fullOrder) {
		setOrder(fullOrder);
	}

	public Long getOrderId() {
		return orderId;
	}

	public void openCard(Order fullOrder) {
		updateOrder(fullOrder);
	}

	public void setHeader(OrderCardHeader header) {
		if (header == null) {
			getModel().setDisplayHeader(false);
			return;
		}
		getModel().setHeader(header);
		getModel().setDisplayHeader(true);
	}

	private List<Component> createComponents(OrderSummary order) {
		LocalDate now = LocalDate.now();
		LocalDate date = order.getDueDate();
		List<Component> result = new ArrayList<>(3);

		Function<OrderSummary, Component> PLACE = o -> createTimeComponent("place", o.getPickupLocation().getName());

		if (date.equals(now) || date.equals(now.minusDays(1))) {
			// Today or yesterday
			result.add(createHeader("time", HOUR_FORMATTER.format(order.getDueTime())));
			result.add(PLACE.apply(order));
		} else if (now.getYear() == date.getYear() && now.get(WEEK_OF_YEAR_FIELD) == date.get(WEEK_OF_YEAR_FIELD)) {
			// This week
			result.add(createHeader("short-day", SHORT_DAY_FORMATTER.format(order.getDueDate())));
			result.add(createTimeComponent("secondary-time", HOUR_FORMATTER.format(order.getDueTime())));
			result.add(PLACE.apply(order));
		} else {
			// Other dates
			result.add(createHeader("month", MONTH_AND_DAY_FORMATTER.format(order.getDueDate())));
			result.add(createTimeComponent("full-day", WEEKDAY_FULLNAME_FORMATTER.format(order.getDueDate())));
		}

		return result;
	}

	private Component fillComponent(HtmlContainer c, String id, String text) {
		c.setId(id);
		c.setText(text);
		return c;
	}

	private Component createTimeComponent(String id, String text) {
		return fillComponent(new Div(), id, text);
	}

	private Component createHeader(String id, String text) {
		return fillComponent(new H3(), id, text);
	}
}
