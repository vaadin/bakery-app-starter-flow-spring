package com.vaadin.starter.bakery.ui.view.dashboard;

import java.time.LocalDate;
import java.time.MonthDay;
import java.time.Year;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
import com.vaadin.starter.bakery.ui.entities.NamedSeries;
import com.vaadin.starter.bakery.ui.entities.chart.ColumnChartData;
import com.vaadin.starter.bakery.ui.entities.chart.ProductDeliveriesChartData;
import com.vaadin.starter.bakery.ui.utils.BakeryConst;
import com.vaadin.starter.bakery.ui.utils.DashboardUtils;
import com.vaadin.starter.bakery.ui.utils.OrdersCountData;
import com.vaadin.starter.bakery.ui.utils.OrdersCountDataWithChart;
import com.vaadin.starter.bakery.ui.view.storefront.StorefrontItem;
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

	@Id("orders-grid")
	private Grid<Order> grid;

	@Autowired
	public DashboardView(OrderService orderService, OrdersGridDataProvider orderDataProvider) {
		this.orderService = orderService;

		grid.addColumn("Order", new ComponentRenderer<>(order -> {
			StorefrontItem item = new StorefrontItem();
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
		ColumnChartData deliveriesThisMonth = DashboardUtils
				.getDeliveriesThisMonthChartData(data.getDeliveriesThisMonth());
		getModel().setDeliveriesThisMonth(deliveriesThisMonth);

		ColumnChartData deliveriesThisYear = DashboardUtils
				.getDeliveriesThisYearChartData(data.getDeliveriesThisYear());
		getModel().setDeliveriesThisYear(deliveriesThisYear);
	}

	private void populateYearlySalesChart(DashboardData data) {
		List<NamedSeries> yearlySales = new ArrayList<>();

		int year = Year.now().getValue();

		for (int i = 0; i < 3; i++) {
			NamedSeries series = new NamedSeries();

			series.setSeries(Arrays.asList(data.getSalesPerMonth(i)).stream().filter(number -> number != null)
					.map(Number::doubleValue).collect(Collectors.toList()));

			series.setTitle(Integer.toString(year - i));
			yearlySales.add(series);
		}
		getModel().setSalesGraphSeries(yearlySales);
		getModel().setSalesGraphTitle("Sales last years");
	}

	public interface Model extends TemplateModel {
		void setTomorrowOrdersCount(OrdersCountData ordersTomorrow);

		void setNewOrdersCount(OrdersCountData ordersNew);

		void setNotAvailableOrdersCount(OrdersCountData ordersNotAvailable);

		void setTodayOrdersCount(OrdersCountDataWithChart ordersToday);

		void setDeliveriesThisYear(ColumnChartData deliveriesThisYear);

		void setDeliveriesThisMonth(ColumnChartData deliveriesThisMonth);

		void setSalesGraphSeries(List<NamedSeries> sales);

		void setSalesGraphTitle(String title);

		void setProductDeliveriesThisMonth(ProductDeliveriesChartData productDeliveriesThisMonth);

	}

}
