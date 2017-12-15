package com.vaadin.starter.bakery.ui.view.dashboard;

import static com.vaadin.starter.bakery.ui.utils.FormattingUtils.getFullMonthName;

import java.time.LocalDate;
import java.time.MonthDay;
import java.time.Year;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.vaadin.addon.charts.model.Background;
import com.vaadin.addon.charts.model.BackgroundShape;
import com.vaadin.addon.charts.model.Pane;
import com.vaadin.addon.charts.model.PlotOptionsSolidgauge;
import com.vaadin.starter.bakery.ui.utils.OrdersCountDataWithChart;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.addon.charts.model.Configuration;
import com.vaadin.addon.charts.model.DataSeries;
import com.vaadin.addon.charts.model.DataSeriesItem;
import com.vaadin.addon.charts.model.ListSeries;
import com.vaadin.addon.charts.model.PlotOptionsPie;
import com.vaadin.data.selection.SelectionEvent;
import com.vaadin.flow.model.TemplateModel;
import com.vaadin.router.PageTitle;
import com.vaadin.router.Route;
import com.vaadin.starter.bakery.backend.data.DashboardData;
import com.vaadin.starter.bakery.backend.data.DeliveryStats;
import com.vaadin.starter.bakery.backend.data.entity.Order;
import com.vaadin.starter.bakery.backend.data.entity.OrderSummary;
import com.vaadin.starter.bakery.backend.data.entity.Product;
import com.vaadin.starter.bakery.backend.service.OrderService;
import com.vaadin.starter.bakery.ui.BakeryLayout;
import com.vaadin.starter.bakery.ui.dataproviders.OrdersGridDataProvider;
import com.vaadin.starter.bakery.ui.utils.BakeryConst;
import com.vaadin.starter.bakery.ui.utils.DashboardUtils;
import com.vaadin.starter.bakery.ui.utils.FormattingUtils;
import com.vaadin.starter.bakery.ui.view.storefront.OrderDetailsBrief;
import com.vaadin.ui.Tag;
import com.vaadin.ui.common.HtmlImport;
import com.vaadin.ui.grid.Grid;
import com.vaadin.ui.polymertemplate.Id;
import com.vaadin.ui.polymertemplate.PolymerTemplate;
import com.vaadin.ui.renderers.ComponentTemplateRenderer;

@Tag("bakery-dashboard")
@HtmlImport("src/dashboard/bakery-dashboard.html")
@Route(value = BakeryConst.PAGE_DASHBOARD, layout = BakeryLayout.class)
@PageTitle(BakeryConst.TITLE_DASHBOARD)
public class DashboardView extends PolymerTemplate<TemplateModel> {

	private static final String[] MONTH_LABELS = new String[] {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul",
			"Aug", "Sep", "Oct", "Nov", "Dec"};

	private final OrderService orderService;

	@Id("today-count")
	private DashboardCounterLabel todayCount;

	@Id("not-available-count")
	private DashboardCounterLabel notAvailableCount;

	@Id("new-count")
	private DashboardCounterLabel newCount;

	@Id("tomorrow-count")
	private DashboardCounterLabel tomorrowCount;

	@Id("deliveries-this-month")
	private Chart deliveriesThisMonthChart;

	@Id("deliveries-this-year")
	private Chart deliveriesThisYearChart;

	@Id("yearly-sales-graph")
	private Chart yearlySalesGraph;

	@Id("orders-grid")
	private Grid<Order> grid;

	@Id("monthly-product-split")
	private Chart monthlyProductSplit;

	@Id("today-count-chart")
	private Chart todayCountChart;

	@Autowired
	public DashboardView(OrderService orderService, OrdersGridDataProvider orderDataProvider) {
		this.orderService = orderService;

		grid.addColumn(new ComponentTemplateRenderer<>(order -> {
			OrderDetailsBrief item = new OrderDetailsBrief();
			item.setOrder(order);
			return item;
		}));
		grid.addSelectionListener(this::onOrdersGridSelectionChanged);

		grid.setDataProvider(orderDataProvider);

		DashboardData data = orderService.getDashboardData(MonthDay.now().getMonthValue(), Year.now().getValue());
		populateYearlySalesChart(data);
		populateDeliveriesCharts(data);
		populateOrdersCounts(data.getDeliveryStats());
		initProductSplitMonthlyGraph(data.getProductDeliveries());
	}

	private void initProductSplitMonthlyGraph(Map<Product, Integer> productDeliveries) {

		LocalDate today = LocalDate.now();

		Configuration conf = monthlyProductSplit.getConfiguration();
		conf.getChart().setType(ChartType.PIE);
		conf.getChart().setBorderRadius(4);
		conf.setTitle("Products delivered in " + getFullMonthName(today));
		DataSeries deliveriesPerProductSeries = new DataSeries(productDeliveries.entrySet().stream()
				.map(e -> new DataSeriesItem(e.getKey().getName(), e.getValue())).collect(Collectors.toList()));
		PlotOptionsPie plotOptionsPie = new PlotOptionsPie();
		plotOptionsPie.setInnerSize("60%");
		deliveriesPerProductSeries.setPlotOptions(plotOptionsPie);
		conf.addSeries(deliveriesPerProductSeries);
	}

	private void populateOrdersCounts(DeliveryStats deliveryStats) {
		List<OrderSummary> orders = orderService.findAnyMatchingStartingToday();

		OrdersCountDataWithChart todaysOrdersCountData = DashboardUtils
				.getTodaysOrdersCountData(deliveryStats, orders.iterator());
		todayCount.setOrdersCountData(todaysOrdersCountData);
		initTodayCountSolidgaugeChart(todaysOrdersCountData);
		notAvailableCount.setOrdersCountData(DashboardUtils.getNotAvailableOrdersCountData(deliveryStats));
		Order lastOrder = orderService.load(orders.get(orders.size() - 1).getId());
		newCount.setOrdersCountData(DashboardUtils.getNewOrdersCountData(deliveryStats, lastOrder));
		tomorrowCount.setOrdersCountData(DashboardUtils.getTomorrowOrdersCountData(deliveryStats, orders.iterator()));
	}


	private void initTodayCountSolidgaugeChart(OrdersCountDataWithChart data) {
		Configuration configuration = todayCountChart.getConfiguration();
		configuration.getChart().setType(ChartType.SOLIDGAUGE);
		configuration.setTitle("");
		configuration.getTooltip().setEnabled(false);

		configuration.getyAxis().setMin(0);
		configuration.getyAxis().setMax(data.getOverall());
		configuration.getyAxis().setLineWidth(0);
		configuration.getyAxis().getLabels().setEnabled(false);

		PlotOptionsSolidgauge opt = new PlotOptionsSolidgauge();
		opt.getDataLabels().setEnabled(false);
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
	}

	private void onOrdersGridSelectionChanged(SelectionEvent<Order> e) {
		e.getFirstSelectedItem().ifPresent(order -> {
			getUI().ifPresent(ui -> ui.navigateTo(BakeryConst.PAGE_STOREFRONT + "/" + order.getId()));
			grid.deselect(order);
		});
	}

	private void populateDeliveriesCharts(DashboardData data) {
		LocalDate today = LocalDate.now();

		// init the 'Deliveries in [this year]' chart
		Configuration yearConf = deliveriesThisYearChart.getConfiguration();
		configureColumnChart(yearConf);

		yearConf.setTitle("Deliveries in " + today.getYear());
		yearConf.getxAxis().setCategories(MONTH_LABELS);
		yearConf.addSeries(new ListSeries("per Month", data.getDeliveriesThisYear()));

		// init the 'Deliveries in [this month]' chart
		Configuration monthConf = deliveriesThisMonthChart.getConfiguration();
		configureColumnChart(monthConf);

		List<Number> deliveriesThisMonth = data.getDeliveriesThisMonth();
		String[] deliveriesThisMonthCategories = IntStream.rangeClosed(1, deliveriesThisMonth.size())
				.mapToObj(String::valueOf).toArray(String[]::new);

		monthConf.setTitle("Deliveries in " + FormattingUtils.getFullMonthName(today));
		monthConf.getxAxis().setCategories(deliveriesThisMonthCategories);
		monthConf.addSeries(new ListSeries("per Day", deliveriesThisMonth));
	}

	private void configureColumnChart(Configuration conf) {
		conf.getChart().setType(ChartType.COLUMN);
		conf.getChart().setBorderRadius(4);

		conf.getxAxis().setTickInterval(1);
		conf.getxAxis().setMinorTickLength(0);
		conf.getxAxis().setTickLength(0);

		conf.getyAxis().getTitle().setText(null);

		conf.getLegend().setEnabled(false);
	}

	private void populateYearlySalesChart(DashboardData data) {
		Configuration conf = yearlySalesGraph.getConfiguration();
		conf.getChart().setType(ChartType.AREASPLINE);
		conf.getChart().setBorderRadius(4);

		conf.setTitle("Sales last years");

		conf.getxAxis().setVisible(false);
		conf.getxAxis().setCategories(MONTH_LABELS);

		conf.getyAxis().getTitle().setText(null);

		int year = Year.now().getValue();
		for (int i = 0; i < 3; i++) {
			conf.addSeries(new ListSeries(Integer.toString(year - i), data.getSalesPerMonth(i)));
		}
	}
}
