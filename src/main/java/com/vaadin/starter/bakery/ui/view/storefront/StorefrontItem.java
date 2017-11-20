package com.vaadin.starter.bakery.ui.view.storefront;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;

import com.vaadin.flow.model.Convert;
import com.vaadin.flow.model.Include;
import com.vaadin.flow.model.TemplateModel;
import com.vaadin.starter.bakery.backend.data.entity.Order;
import com.vaadin.starter.bakery.ui.utils.converters.LocalTimeConverter;
import com.vaadin.starter.bakery.ui.utils.converters.LongToStringConverter;
import com.vaadin.starter.bakery.ui.utils.converters.OrderStateConverter;
import com.vaadin.ui.Component;
import com.vaadin.ui.Tag;
import com.vaadin.ui.common.HtmlContainer;
import com.vaadin.ui.common.HtmlImport;
import com.vaadin.ui.html.Div;
import com.vaadin.ui.html.H3;
import com.vaadin.ui.polymertemplate.Id;
import com.vaadin.ui.polymertemplate.PolymerTemplate;

@Tag("storefront-item")
@HtmlImport("src/storefront/storefront-item.html")
public class StorefrontItem extends PolymerTemplate<StorefrontItem.Model> {

	/**
	 * For getting the week of the year from the localdate.
	 */
	private static final TemporalField WEEK_OF_YEAR_FIELD = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();

	/**
	 * 3 letter day of the week + day number. E.g: Mon 20
	 */
	private static final DateTimeFormatter SHORT_DAY_FORMATTER = DateTimeFormatter.ofPattern("E d");

	/**
	 * Full day name. E.g: Monday.
	 */
	private static final DateTimeFormatter FULL_DAY_FORMATTER = DateTimeFormatter.ofPattern("EEEE");

	/**
	 * 3 letter month name + day number E.g: Nov 20
	 */
	private static final DateTimeFormatter MONTH_AND_DAY_FORMATTER = DateTimeFormatter.ofPattern("MMM d");

	public interface Model extends TemplateModel {
		@Include({ "id", "state", "customer.fullName", "items.product.name", "items.quantity" })
		@Convert(value = LongToStringConverter.class, path = "id")
		@Convert(value = OrderStateConverter.class, path = "state")
		void setItem(Order order);
	}

	@Id("time-place")
	private Div timePlace;

	public void setOrder(Order order) {
		getModel().setItem(order);
		timePlace.removeAll();
		createComponents(order).forEach(timePlace::add);
	}

	private List<Component> createComponents(Order order) {
		LocalDate now = LocalDate.now();
		LocalDate date = order.getDueDate();
		List<Component> result = new ArrayList<>(3);

		Function<Order, Component> PLACE = o -> createTimeComponent("place", o.getPickupLocation().getName());

		if (date.equals(now) || date.equals(now.minusDays(1))) {
			// Today or yesterday
			result.add(createHeader("time", LocalTimeConverter.formatter.format(order.getDueTime())));
			result.add(PLACE.apply(order));
		} else if (now.get(WEEK_OF_YEAR_FIELD) == date.get(WEEK_OF_YEAR_FIELD)) {
			// This week
			result.add(createHeader("short-day", SHORT_DAY_FORMATTER.format(order.getDueDate())));
			result.add(createTimeComponent("secondary-time", LocalTimeConverter.formatter.format(order.getDueTime())));
			result.add(PLACE.apply(order));
		} else {
			// Other dates
			result.add(createHeader("month", MONTH_AND_DAY_FORMATTER.format(order.getDueDate())));
			result.add(createTimeComponent("full-day", FULL_DAY_FORMATTER.format(order.getDueDate())));
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
