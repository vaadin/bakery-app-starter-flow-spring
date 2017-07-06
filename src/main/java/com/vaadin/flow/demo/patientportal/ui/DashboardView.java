package com.vaadin.flow.demo.patientportal.ui;

import java.util.List;

import com.vaadin.annotations.HtmlImport;
import com.vaadin.annotations.Tag;
import com.vaadin.flow.demo.patientportal.ui.dataproviders.OrdersDataProvider;
import com.vaadin.flow.demo.patientportal.ui.entities.Order;
import com.vaadin.flow.router.View;
import com.vaadin.flow.template.PolymerTemplate;
import com.vaadin.flow.template.model.TemplateModel;

@Tag("bakery-dashboard")
@HtmlImport("/src/dashboard/bakery-dashboard.html")
public class DashboardView extends PolymerTemplate<DashboardView.Model> implements View {

	public DashboardView() {
		getElement().addAttachListener(e -> populateOrdersList(0, 10));
	}

	private void populateOrdersList(int start, int count) {
		// TODO: create lazy loading using getOrdersList(start, count)
		List<Order> ordersList = OrdersDataProvider.get().getOrdersList();
		getModel().setOrders(ordersList);
	}

	public interface Model extends TemplateModel {
		void setOrders(List<Order> orders);
	}
}
