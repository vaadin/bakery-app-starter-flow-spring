package com.vaadin.starter.bakery.ui.view.dashboard;

import java.time.Year;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.vaadin.flow.model.Convert;
import com.vaadin.flow.model.Include;
import com.vaadin.flow.model.TemplateModel;
import com.vaadin.router.PageTitle;
import com.vaadin.starter.bakery.ui.BakeryApp;
import com.vaadin.starter.bakery.ui.utils.converters.LocalTimeConverter;
import com.vaadin.starter.bakery.ui.utils.converters.LongToStringConverter;
import com.vaadin.starter.bakery.ui.utils.converters.OrderStateConverter;
import com.vaadin.starter.bakery.ui.view.storefront.converter.StorefrontLocalDateConverter;
import com.vaadin.ui.Tag;
import com.vaadin.ui.event.AttachEvent;
import com.vaadin.ui.common.ClientDelegate;
import com.vaadin.ui.common.HtmlImport;
import com.vaadin.ui.event.EventData;
import com.vaadin.ui.polymertemplate.EventHandler;
import com.vaadin.ui.polymertemplate.PolymerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import com.vaadin.router.Route;
import com.vaadin.starter.bakery.backend.data.DashboardData;
import com.vaadin.starter.bakery.backend.data.DeliveryStats;
import com.vaadin.starter.bakery.backend.data.entity.Product;
import com.vaadin.starter.bakery.ui.dataproviders.OrdersDataProvider;
import com.vaadin.starter.bakery.ui.entities.NamedSeries;
import com.vaadin.starter.bakery.ui.entities.chart.ColumnChartData;
import com.vaadin.starter.bakery.ui.entities.chart.ProductDeliveriesChartData;
import com.vaadin.starter.bakery.ui.utils.BakeryConst;
import com.vaadin.starter.bakery.ui.utils.DashboardUtils;
import com.vaadin.starter.bakery.ui.utils.DashboardUtils.OrdersCountData;
import com.vaadin.starter.bakery.ui.utils.DashboardUtils.OrdersCountDataWithChart;
import com.vaadin.starter.bakery.ui.utils.DashboardUtils.PageInfo;

@Tag("bakery-dashboard")
@HtmlImport("src/dashboard/bakery-dashboard.html")
@Route(value = BakeryConst.PAGE_DASHBOARD, layout = BakeryApp.class)
@PageTitle(BakeryConst.TITLE_DASHBOARD)
public class DashboardView extends PolymerTemplate<DashboardView.Model> {

	private final OrdersDataProvider ordersProvider;

	@Autowired
	public DashboardView(OrdersDataProvider ordersProvider) {
		this.ordersProvider = ordersProvider;
	}

	@Override
	protected void onAttach(AttachEvent attachEvent) {
		populateOrdersCount();
		DashboardData data = ordersProvider.getDashboardData();
		populateYearlySalesChart(data);
		populateDeliveriesCharts(data);
		populateOrdersCounts(data.getDeliveryStats());
		populateDeliveriesPerProductChart(data.getProductDeliveries());
	}

	private void populateOrdersCounts(DeliveryStats deliveryStats) {
		List<com.vaadin.starter.bakery.backend.data.entity.Order> orders = ordersProvider.getOriginalOrdersList();

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
		getModel().setOrdersCount((int) ordersProvider.countAnyMatchingAfterDueDate());
	}

	@EventHandler
	private void loadOrdersPage(@EventData("event.detail.page") int page,
			@EventData("event.detail.pageSize") int pageSize) {
		PageInfo pageInfo = ordersProvider.getOrdersList(null, false, new PageRequest(page, pageSize));
		getModel().setPageInfo(pageInfo);
		getElement().callFunction("loadPage");
	}

	@ClientDelegate
	private void navigateToOrderDetails(String orderId) {
		getUI().ifPresent(ui -> ui.navigateTo(BakeryConst.PAGE_STOREFRONT + "/" + orderId));
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
		@Include({ "orders.id", "orders.dueDate.day", "orders.dueDate.weekday", "orders.dueDate.date", "orders.dueTime",
				"orders.items.product.name", "orders.items.quantity",
				"orders.state", "orders.pickupLocation.name", "orders.customer.fullName", "orders.customer.details", "pageNumber" })
		@Convert(value = LongToStringConverter.class, path = "orders.id")
		@Convert(value = StorefrontLocalDateConverter.class, path = "orders.dueDate")
		@Convert(value = LocalTimeConverter.class, path = "orders.dueTime")
		@Convert(value = OrderStateConverter.class, path = "orders.state")
		void setPageInfo(PageInfo pageInfo);

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
