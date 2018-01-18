package com.vaadin.starter.bakery.ui.view.dashboard;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.templatemodel.TemplateModel;
import com.vaadin.starter.bakery.ui.utils.OrdersCountData;

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
