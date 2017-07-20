package com.vaadin.starter.bakery.ui;

import java.time.Year;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.vaadin.annotations.EventData;
import com.vaadin.annotations.EventHandler;
import com.vaadin.annotations.HtmlImport;
import com.vaadin.annotations.Tag;
import com.vaadin.flow.router.View;
import com.vaadin.flow.template.PolymerTemplate;
import com.vaadin.flow.template.model.TemplateModel;
import com.vaadin.hummingbird.ext.spring.annotations.ParentView;
import com.vaadin.hummingbird.ext.spring.annotations.Route;
import com.vaadin.starter.bakery.backend.data.DashboardData;
import com.vaadin.starter.bakery.backend.data.DeliveryStats;
import com.vaadin.starter.bakery.backend.data.entity.Product;
import com.vaadin.starter.bakery.ui.dataproviders.OrdersDataProvider;
import com.vaadin.starter.bakery.ui.entities.NamedSeries;
import com.vaadin.starter.bakery.ui.entities.Order;
import com.vaadin.starter.bakery.ui.entities.chart.ColumnChartData;
import com.vaadin.starter.bakery.ui.entities.chart.ProductDeliveriesChartData;
import com.vaadin.starter.bakery.ui.utils.BakeryConst;
import com.vaadin.starter.bakery.ui.utils.DashboardUtils;
import com.vaadin.starter.bakery.ui.utils.DashboardUtils.OrdersCountData;
import com.vaadin.starter.bakery.ui.utils.DashboardUtils.OrdersCountDataWithChart;
import com.vaadin.starter.bakery.ui.utils.DashboardUtils.PageInfo;
import com.vaadin.ui.AttachEvent;
import org.springframework.data.domain.PageRequest;

@Tag("bakery-dashboard")
@HtmlImport("frontend://src/dashboard/bakery-dashboard.html")
@Route(BakeryConst.PAGE_DASHBOARD)
@ParentView(BakeryApp.class)
public class DashboardView extends PolymerTemplate<DashboardView.Model> implements View {

	public DashboardView() {
	}

	@Override
	protected void onAttach(AttachEvent attachEvent) {
		populateOrdersCount();
		DashboardData data = OrdersDataProvider.get().getDashboardData();
		populateYearlySalesChart(data);
		populateDeliveriesCharts(data);
		populateOrdersCounts(data.getDeliveryStats());
		populateDeliveriesPerProductChart(data.getProductDeliveries());
	}

	private void populateOrdersCounts(DeliveryStats deliveryStats) {
		List<com.vaadin.starter.bakery.backend.data.entity.Order> orders = OrdersDataProvider.get()
				.getOriginalOrdersList();

		getModel().setTodayOrdersCount(DashboardUtils.getTodaysOrdersCountData(deliveryStats, orders.iterator()));
		getModel().setNotAvailableOrdersCount(DashboardUtils.getNotAvailableOrdersCountData(deliveryStats));
		getModel()
				.setNewOrdersCount(DashboardUtils.getNewOrdersCountData(deliveryStats, orders.get(orders.size() - 1)));
		getModel().setTomorrowOrdersCount(DashboardUtils.getTomorrowOrdersCountData(deliveryStats, orders.iterator()));
	}

	private void populateDeliveriesPerProductChart(Map<Product, Integer> productDeliveries) {
		getModel().setProductDeliveriesThisMonth(DashboardUtils.getDeliveriesPerProductPieChartData(productDeliveries));
	}

	private void populateOrdersCount() {
		getModel().setOrdersCount((int) OrdersDataProvider.get().countAnyMatchingAfterDueDate());
	}

	@EventHandler
	private void loadOrdersPage(@EventData("event.detail.page") int page,
								@EventData("event.detail.pageSize") int pageSize) {
		PageInfo pageInfo = OrdersDataProvider.get().getOrdersList(new PageRequest(page, pageSize));
		//It should be easier to use a complex object as function argument
		String json = new Gson().toJson(pageInfo);
		getElement().callFunction("loadPage", json);
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
					.map(Number::toString).collect(Collectors.toList()));

			series.setTitle(Integer.toString(year - i));
			yearlySales.add(series);
		}
		getModel().setSalesGraphSeries(yearlySales);
		getModel().setSalesGraphTitle("Sales last years");
	}

	public interface Model extends TemplateModel {
		void setOrders(List<Order> orders);

		void setOrdersCount(Integer ordersCount);

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
