package com.vaadin.starter.bakery.ui.view.storefront;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import com.vaadin.flow.model.Convert;
import com.vaadin.flow.model.Include;
import com.vaadin.flow.model.TemplateModel;
import com.vaadin.starter.bakery.backend.data.entity.Order;
import com.vaadin.starter.bakery.ui.utils.DateTimeUtils;
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
		DataGeneratorForTimePeriod.get(order.getDueDate()).createComponents(order, timePlace::add);
	}

	/**
	 * Creates a html component using data from the order.
	 */
	enum ItemComponentCreator implements Function<Order, Component> {
		MONTH(o -> createHeader("month", MONTH_AND_DAY_FORMATTER.format(o.getDueDate()))),

		TIME(o -> createHeader("time", LocalTimeConverter.formatter.format(o.getDueTime()))),

		SHORT_DAY(o -> createHeader("short-day", SHORT_DAY_FORMATTER.format(o.getDueDate()))),

		FULL_DAY(o -> createTimeComponent("full-day", FULL_DAY_FORMATTER.format(o.getDueDate()))),

		PLACE(o -> createTimeComponent("place", o.getPickupLocation().getName())),

		SECONDARY_TIME(o -> createTimeComponent("secondary-time", LocalTimeConverter.formatter.format(o.getDueTime())));

		private final Function<Order, Component> componentTextGenerator;

		ItemComponentCreator(Function<Order, Component> componentTextGenerator) {
			this.componentTextGenerator = componentTextGenerator;
		}

		@Override
		public Component apply(Order t) {
			return componentTextGenerator.apply(t);
		}

		static Component fillComponent(HtmlContainer c, String id, String text) {
			c.setId(id);
			c.setText(text);
			return c;
		}

		static Component createTimeComponent(String id, String text) {
			return fillComponent(new Div(), id, text);
		}

		static Component createHeader(String id, String text) {
			return fillComponent(new H3(), id, text);
		}
	}

	/**
	 * Displays data depending on the time period of the order's due date.
	 */
	enum DataGeneratorForTimePeriod {
		YESTERDAY_OR_TODAY(ItemComponentCreator.TIME, ItemComponentCreator.PLACE),

		THIS_WEEK(ItemComponentCreator.SHORT_DAY, ItemComponentCreator.SECONDARY_TIME, ItemComponentCreator.PLACE),

		OTHER_DATES(ItemComponentCreator.MONTH, ItemComponentCreator.FULL_DAY);

		private final List<Function<Order, Component>> componentCreators;

		DataGeneratorForTimePeriod(ItemComponentCreator... componentCreators) {
			this.componentCreators = Arrays.asList(componentCreators);
		}

		static DataGeneratorForTimePeriod get(LocalDate date) {
			LocalDate now = LocalDate.now();
			if (date.equals(now) || date.equals(now.minusDays(1))) {
				return YESTERDAY_OR_TODAY;
			} else if (DateTimeUtils.isSameWeek(now, date)) {
				return DataGeneratorForTimePeriod.THIS_WEEK;
			} else {
				return OTHER_DATES;
			}
		}

		void createComponents(Order order, Consumer<Component> consumer) {
			componentCreators.forEach(c -> consumer.accept(c.apply(order)));
		}
	}
}
