package com.vaadin.starter.bakery.ui.view.dashboard;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.Background;
import com.vaadin.addon.charts.model.BackgroundShape;
import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.addon.charts.model.Configuration;
import com.vaadin.addon.charts.model.DataSeries;
import com.vaadin.addon.charts.model.Pane;
import com.vaadin.addon.charts.model.PlotOptionsSolidgauge;
import com.vaadin.flow.model.TemplateModel;
import com.vaadin.starter.bakery.ui.utils.OrdersCountData;
import com.vaadin.starter.bakery.ui.utils.OrdersCountDataWithChart;
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

	public DashboardCounterLabel() {

	}

	public void setOrdersCountData(OrdersCountData data) {
		this.title.setText(data.getTitle());
		this.subtitle.setText(data.getSubtitle());
		this.count.setText(String.valueOf(data.getCount()));

		if (data instanceof OrdersCountDataWithChart) {
			chartWrapper.getElement().setAttribute("hidden", false);
			setupSolidgaugeChart((OrdersCountDataWithChart) data);
		} else {
			chartWrapper.getElement().setAttribute("hidden", true);
		}

	}

	private void setupSolidgaugeChart(OrdersCountDataWithChart data) {
		Chart chart = new Chart(ChartType.SOLIDGAUGE);
		chart.getElement().getClassList().add("counter");

		Configuration configuration = chart.getConfiguration();

		configuration.setTitle("");
		configuration.getTooltip().setEnabled(false);

		configuration.getyAxis().setMin(0);
		configuration.getyAxis().setMax(data.getOverall());
		configuration.getyAxis().setLineWidth(0);
		configuration.getyAxis().getLabels().setEnabled(false);

		PlotOptionsSolidgauge opt = new PlotOptionsSolidgauge();
		opt.getDataLabels().setY(0);
		opt.getDataLabels().setBorderWidth(0);
		opt.getDataLabels().setUseHTML(true);
		// To hide original chart data label
		opt.getDataLabels().setFormat("<div></div>");
		configuration.setPlotOptions(opt);

		DataSeriesItemWithRadius point = new DataSeriesItemWithRadius();
		point.setY(data.getCount());
		point.setInnerRadius("100%");
		point.setRadius("110%");
		configuration.setSeries(new DataSeries(point));

		Pane pane = configuration.getPane();
		pane.setStartAngle(0);
		pane.setEndAngle(360);

		Background background = new Background();
		background.setShape(BackgroundShape.ARC);
		background.setInnerRadius("100%");
		background.setOuterRadius("110%");
		pane.setBackground(background);

		getElement().appendChild(chart.getElement());
	}

}
