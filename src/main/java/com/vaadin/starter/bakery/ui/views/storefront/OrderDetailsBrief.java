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
import com.vaadin.starter.bakery.backend.data.entity.OrderSummary;
import com.vaadin.starter.bakery.ui.utils.converters.LongToStringConverter;
import com.vaadin.starter.bakery.ui.utils.converters.OrderStateConverter;

/**
 * The component displaying a brief (read-only) summary of an order.
 */
@Tag("order-details-brief")
@HtmlImport("src/views/storefront/order-details-brief.html")
public class OrderDetailsBrief extends PolymerTemplate<OrderDetailsBrief.Model> {

	public interface Model extends TemplateModel {
		@Include({ "id", "state", "customer.fullName", "items.product.name", "items.quantity" })
		@Convert(value = LongToStringConverter.class, path = "id")
		@Convert(value = OrderStateConverter.class, path = "state")
		void setItem(OrderSummary order);
	}

	@Id("timePlace")
	private Div timePlace;

	public void setOrder(OrderSummary order) {
		getModel().setItem(order);
		timePlace.removeAll();
		createComponents(order).forEach(timePlace::add);
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
