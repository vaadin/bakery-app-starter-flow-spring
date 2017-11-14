package com.vaadin.starter.bakery.ui.view.storefront;

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
}
