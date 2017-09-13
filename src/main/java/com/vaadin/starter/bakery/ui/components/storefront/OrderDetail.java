/**
 * 
 */
package com.vaadin.starter.bakery.ui.components.storefront;

import com.vaadin.annotations.Convert;
import com.vaadin.annotations.HtmlImport;
import com.vaadin.annotations.Id;
import com.vaadin.annotations.Include;
import com.vaadin.annotations.Tag;
import com.vaadin.flow.html.Div;
import com.vaadin.flow.template.PolymerTemplate;
import com.vaadin.flow.template.model.TemplateModel;
import com.vaadin.starter.bakery.backend.data.entity.Order;
import com.vaadin.starter.bakery.ui.converters.LocalDateConverter;
import com.vaadin.starter.bakery.ui.converters.LocalTimeConverter;
import com.vaadin.starter.bakery.ui.converters.LongToStringConverter;
import com.vaadin.ui.Button;

/**
 * @author tulio
 *
 */
@Tag("order-detail")
@HtmlImport("frontend://src/storefront/order-detail.html")
public class OrderDetail extends PolymerTemplate<OrderDetail.Model> {

	@Id("footer")
	private Div footer;

	private Order order;

	private ReviewFooter reviewFooter;

	private Runnable onBack;
	private Runnable onSave;

	public OrderDetail() {
		reviewFooter = new ReviewFooter(()->this.onBack.run(), ()->this.onSave.run());
		footer.add(reviewFooter);
	}

	public void display(Order order, Runnable onBack, Runnable onSave) {
		this.order = order;
		this.onBack = onBack;
		this.onSave = onSave;

	}

	void onReview() {
		getModel().setReview(true);
		getModel().setItem(order);
		reviewFooter.updatePrice(order.getTotalPrice());
	}

	public interface Model extends TemplateModel {
		@Include({ "id", "dueDate", "dueTime", "state", "pickupLocation.name", "customer.fullName",
				"customer.phoneNumber", "customer.details", "items.product.name", "items.comment", "items.quantity",
				"items.product.price" })
		@Convert(value = LongToStringConverter.class, path = "id")
		@Convert(value = LocalDateConverter.class, path = "dueDate")
		@Convert(value = LocalTimeConverter.class, path = "dueTime")
		@Convert(value = OrderStateConverter.class, path = "state")
		void setItem(Order order);

		void setReview(boolean review);
	}
}

@Tag("order-edit-review-footer")
@HtmlImport("frontend://src/storefront/order-edit-review-footer.html")
class ReviewFooter extends Footer {

	@Id("back")
	private Button back;

	@Id("save")
	private Button save;

	public ReviewFooter(Runnable onBack, Runnable onSave) {
		back.addClickListener(e -> onBack.run());
		save.addClickListener(e -> onSave.run());
	}
}