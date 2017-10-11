package com.vaadin.starter.bakery.ui.components;

import com.vaadin.flow.model.TemplateModel;
import com.vaadin.starter.bakery.ui.utils.BakeryConst;
import com.vaadin.ui.Tag;
import com.vaadin.ui.common.ClientDelegate;
import com.vaadin.ui.common.HtmlImport;
import com.vaadin.ui.polymertemplate.PolymerTemplate;

@Tag("dashboard-orders-list")
@HtmlImport("context://src/dashboard/dashboard-orders-list.html")
public class DashboardOrdersList extends PolymerTemplate<TemplateModel> {

	public DashboardOrdersList() {
	}

	@ClientDelegate
	private void navigateToOrder(String orderId) {
		getUI().ifPresent(c -> c.navigateTo(BakeryConst.PAGE_STOREFRONT + "/" + orderId));
	}
}
