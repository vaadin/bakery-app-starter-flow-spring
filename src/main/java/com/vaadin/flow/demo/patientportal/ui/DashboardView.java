package com.vaadin.flow.demo.patientportal.ui;

import java.time.Year;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.vaadin.annotations.HtmlImport;
import com.vaadin.annotations.Tag;
import com.vaadin.flow.demo.patientportal.backend.data.DashboardData;
import com.vaadin.flow.demo.patientportal.ui.dataproviders.OrdersDataProvider;
import com.vaadin.flow.demo.patientportal.ui.entities.NamedSeries;
import com.vaadin.flow.demo.patientportal.ui.entities.Order;
import com.vaadin.flow.demo.patientportal.ui.utils.BakeryConst;
import com.vaadin.flow.demo.patientportal.ui.utils.DashboardUtils;
import com.vaadin.flow.demo.patientportal.ui.utils.DashboardUtils.ChartData;
import com.vaadin.flow.router.View;
import com.vaadin.flow.template.PolymerTemplate;
import com.vaadin.flow.template.model.TemplateModel;
import com.vaadin.hummingbird.ext.spring.annotations.ParentView;
import com.vaadin.hummingbird.ext.spring.annotations.Route;

@Tag("bakery-dashboard")
@HtmlImport("frontend://src/dashboard/bakery-dashboard.html")
@Route(BakeryConst.PAGE_DASHBOARD)
@ParentView(BakeryApp.class)
public class DashboardView extends PolymerTemplate<DashboardView.Model> implements View {

	public DashboardView() {
		getElement().addAttachListener(e -> {
			populateOrdersList(0, 10);
			DashboardData data = OrdersDataProvider.get().getDashboardData();
			populateYearlySalesChart(data);
			populateDeliveriesCharts(data);
		});
	}

	private void populateOrdersList(int start, int count) {
		// TODO: create lazy loading using getOrdersList(start, count)
		List<Order> ordersList = OrdersDataProvider.get().getOrdersList();
		getModel().setOrders(ordersList);
	}

	private void populateDeliveriesCharts(DashboardData data) {
		ChartData deliveriesThisMonth = DashboardUtils.getDeliveriesThisMonthChartData(data.getDeliveriesThisMonth());
		getModel().setDeliveriesThisMonth(deliveriesThisMonth);

		ChartData deliveriesThisYear = DashboardUtils.getDeliveriesThisYearChartData(data.getDeliveriesThisYear());
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

		void setDeliveriesThisYear(ChartData deliveriesThisYear);

		void setDeliveriesThisMonth(ChartData deliveriesThisMonth);

		void setSalesGraphSeries(List<NamedSeries> sales);

		void setSalesGraphTitle(String title);

	}

}
