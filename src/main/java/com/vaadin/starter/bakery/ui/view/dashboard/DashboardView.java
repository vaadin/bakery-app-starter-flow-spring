package com.vaadin.starter.bakery.ui.view.dashboard;

import java.time.LocalDate;
import java.time.MonthDay;
import java.time.Year;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.AxisTitle;
import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.addon.charts.model.Configuration;
import com.vaadin.addon.charts.model.ListSeries;
import com.vaadin.addon.charts.model.XAxis;
import com.vaadin.addon.charts.model.YAxis;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.data.selection.SelectionEvent;
import com.vaadin.flow.model.TemplateModel;
import com.vaadin.router.PageTitle;
import com.vaadin.router.Route;
import com.vaadin.starter.bakery.backend.data.DashboardData;
import com.vaadin.starter.bakery.backend.data.DeliveryStats;
import com.vaadin.starter.bakery.backend.data.entity.Order;
import com.vaadin.starter.bakery.backend.data.entity.Product;
import com.vaadin.starter.bakery.backend.service.OrderService;
import com.vaadin.starter.bakery.ui.BakeryApp;
import com.vaadin.starter.bakery.ui.dataproviders.OrdersGridDataProvider;
import com.vaadin.starter.bakery.ui.entities.chart.ColumnChartData;
import com.vaadin.starter.bakery.ui.entities.chart.ProductDeliveriesChartData;
import com.vaadin.starter.bakery.ui.utils.BakeryConst;
import com.vaadin.starter.bakery.ui.utils.DashboardUtils;
import com.vaadin.starter.bakery.ui.utils.OrdersCountData;
import com.vaadin.starter.bakery.ui.utils.OrdersCountDataWithChart;
import com.vaadin.starter.bakery.ui.view.storefront.OrderDetailsBrief;
import com.vaadin.ui.Tag;
import com.vaadin.ui.common.HtmlImport;
import com.vaadin.ui.grid.Grid;
import com.vaadin.ui.polymertemplate.Id;
import com.vaadin.ui.polymertemplate.PolymerTemplate;
import com.vaadin.ui.renderers.ComponentRenderer;

@Tag("bakery-dashboard")
@HtmlImport("src/dashboard/bakery-dashboard.html")
@Route(value = BakeryConst.PAGE_DASHBOARD, layout = BakeryApp.class)
@PageTitle(BakeryConst.TITLE_DASHBOARD)
public class DashboardView extends PolymerTemplate<DashboardView.Model> {

	private final OrderService orderService;

	@Id("deliveries-this-month")
	private Chart deliveriesThisMonthChart;

	@Id("deliveries-this-year")
	private Chart deliveriesThisYearChart;

	@Id("yearly-sales-graph")
	private Chart yearlySalesGraph;

	@Id("orders-grid")
	private Grid<Order> grid;

	@Autowired
	public DashboardView(OrderService orderService, OrdersGridDataProvider orderDataProvider) {
		this.orderService = orderService;

		grid.addColumn(new ComponentRenderer<>(order -> {
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
		populateDeliveriesPerProductChart(data.getProductDeliveries());
	}

	private void populateOrdersCounts(DeliveryStats deliveryStats) {
		List<Order> orders = orderService
				.findAnyMatchingAfterDueDate(Optional.empty(), Optional.of(LocalDate.now().minusDays(1)), null)
				.getContent();

		getModel().setTodayOrdersCount(DashboardUtils.getTodaysOrdersCountData(deliveryStats, orders.iterator()));
		getModel().setNotAvailableOrdersCount(DashboardUtils.getNotAvailableOrdersCountData(deliveryStats));
		getModel()
		.setNewOrdersCount(DashboardUtils.getNewOrdersCountData(deliveryStats, orders.get(orders.size() - 1)));
		getModel().setTomorrowOrdersCount(DashboardUtils.getTomorrowOrdersCountData(deliveryStats, orders.iterator()));
	}

	private void populateDeliveriesPerProductChart(Map<Product, Integer> productDeliveries) {
		getModel().setProductDeliveriesThisMonth(DashboardUtils.getDeliveriesPerProductPieChartData(productDeliveries));
	}

	private void onOrdersGridSelectionChanged(SelectionEvent<Order> e) {
		e.getFirstSelectedItem().ifPresent(order -> {
			getUI().ifPresent(ui -> ui.navigateTo(BakeryConst.PAGE_STOREFRONT + "/" + order.getId()));
			grid.deselect(order);
		});
	}

	private void populateDeliveriesCharts(DashboardData data) {
		DashboardUtils.writeDeliveriesThisMonthChartConfig(
				deliveriesThisMonthChart.getConfiguration(), data.getDeliveriesThisMonth());

		DashboardUtils.writeDeliveriesThisYearChartConfig(
				deliveriesThisYearChart.getConfiguration(), data.getDeliveriesThisYear());
	}

	private void populateYearlySalesChart(DashboardData data) {
		Configuration conf = yearlySalesGraph.getConfiguration();
		conf.getChart().setType(ChartType.AREASPLINE);
		conf.getChart().setBorderRadius(4);
		
		conf.setTitle("Sales last years");

		XAxis xAxis = new XAxis();
		xAxis.setVisible(false);
		xAxis.setCategories(DashboardUtils.MONTH_LABELS);
		conf.addxAxis(xAxis);

		YAxis yAxis = new YAxis();
		AxisTitle title = new AxisTitle();
		title.setText(null);
		yAxis.setTitle(title);
		conf.addyAxis(yAxis);

		int year = Year.now().getValue();
		for (int i = 0; i < 3; i++) {
			conf.addSeries(new ListSeries(Integer.toString(year - i), data.getSalesPerMonth(i)));
		}
	}

	public interface Model extends TemplateModel {
		void setTomorrowOrdersCount(OrdersCountData ordersTomorrow);

		void setNewOrdersCount(OrdersCountData ordersNew);

		void setNotAvailableOrdersCount(OrdersCountData ordersNotAvailable);

		void setTodayOrdersCount(OrdersCountDataWithChart ordersToday);

		void setProductDeliveriesThisMonth(ProductDeliveriesChartData productDeliveriesThisMonth);

	}

}
