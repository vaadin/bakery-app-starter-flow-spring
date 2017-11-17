package com.vaadin.starter.bakery.ui.view.storefront;

import java.time.LocalDate;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.Locale;

import com.vaadin.flow.model.Convert;
import com.vaadin.flow.model.Include;
import com.vaadin.flow.model.TemplateModel;
import com.vaadin.starter.bakery.backend.data.entity.Order;
import com.vaadin.starter.bakery.ui.utils.converters.LocalTimeConverter;
import com.vaadin.starter.bakery.ui.utils.converters.LongToStringConverter;
import com.vaadin.starter.bakery.ui.utils.converters.OrderStateConverter;
import com.vaadin.starter.bakery.ui.view.storefront.converter.StorefrontLocalDateConverter;
import com.vaadin.ui.Tag;
import com.vaadin.ui.common.HtmlImport;
import com.vaadin.ui.polymertemplate.PolymerTemplate;

@Tag("storefront-item")
@HtmlImport("src/storefront/storefront-item.html")
public class StorefrontItem extends PolymerTemplate<StorefrontItem.Model> {

	public interface Model extends TemplateModel {
		@Include({ "id", "dueDate.day", "dueDate.weekday", "dueDate.date", "dueTime", "state", "pickupLocation.name",
			"customer.fullName", "items.product.name", "items.quantity" })
		@Convert(value = LongToStringConverter.class, path = "id")
		@Convert(value = StorefrontLocalDateConverter.class, path = "dueDate")
		@Convert(value = LocalTimeConverter.class, path = "dueTime")
		@Convert(value = OrderStateConverter.class, path = "state")
		void setItem(Order order);
	}

	public StorefrontItem() {

	}

	public void setOrder(Order order) {
		getModel().setItem(order);
	}

	boolean isYesterday(LocalDate date) {
		return LocalDate.now().minusDays(1).equals(date);
	}

	boolean isToday(LocalDate date) {
		return LocalDate.now().equals(date);
	}

	boolean isThisWeekBeforeYesterday(LocalDate date) {
		LocalDate now = LocalDate.now();
		return isSameWeek(now, date) && date.isBefore(now.minusDays(1));
	}

	private boolean isSameWeek(LocalDate date1, LocalDate date2) {
		TemporalField woy = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
		return date1.get(woy) == date2.get(woy);
	}

	boolean isThisWeekStartingTomorrow(LocalDate date) {
		LocalDate now = LocalDate.now();
		return isSameWeek(now, date) && date.isAfter(now);
	}

	boolean isUpcoming(LocalDate date) {
		return moment(new Date(date)).isAfter(moment().endOf('week').add(1, 'day'));
	}

	String getShortDay(LocalDate date) {
		return moment(new Date(date)).format('ddd D');
	}

	String getFullDay(LocalDate date) {
		return moment(new Date(date)).format('dddd');
	}

	String getMonth(LocalDate date) {
		return moment(new Date(date)).format('MMM D');
	}
}
