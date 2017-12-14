package com.vaadin.starter.bakery.ui.view.dashboard;

import com.vaadin.flow.model.TemplateModel;
import com.vaadin.starter.bakery.ui.utils.OrdersCountData;
import com.vaadin.ui.Tag;
import com.vaadin.ui.common.HtmlImport;
import com.vaadin.ui.html.Div;
import com.vaadin.ui.html.H4;
import com.vaadin.ui.html.Span;
import com.vaadin.ui.polymertemplate.Id;
import com.vaadin.ui.polymertemplate.PolymerTemplate;

@Tag("dashboard-counter-label")
@HtmlImport("src/dashboard/dashboard-counter-label.html")
public class DashboardCounterLabel extends PolymerTemplate<TemplateModel> {

	@Id("title")
	private H4 title;

	@Id("subtitle")
	private Div subtitle;

	@Id("count")
	private Span count;

	@Id("chart-wrapper")
	private Div chartWrapper;

	public void setOrdersCountData(OrdersCountData data) {
		title.setText(data.getTitle());
		subtitle.setText(data.getSubtitle());
		count.setText(String.valueOf(data.getCount()));
	}
}
