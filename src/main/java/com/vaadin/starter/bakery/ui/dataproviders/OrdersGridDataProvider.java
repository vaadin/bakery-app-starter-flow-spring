package com.vaadin.starter.bakery.ui.dataproviders;

import com.vaadin.data.provider.Query;
import com.vaadin.data.provider.QuerySortOrder;
import com.vaadin.data.provider.QuerySortOrderBuilder;
import com.vaadin.starter.bakery.backend.data.entity.Order;
import com.vaadin.starter.bakery.ui.utils.OrderFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.vaadin.artur.spring.dataprovider.FilterablePageableDataProvider;

import java.util.List;

public class OrdersGridDataProvider extends FilterablePageableDataProvider<Order, OrderFilter> {

	private final OrdersDataProvider ordersProvider;
	private final List<QuerySortOrder> defaultSortOrders;

	public OrdersGridDataProvider(OrdersDataProvider ordersProvider, Sort.Direction direction, String... properties) {
		this.ordersProvider = ordersProvider;
		this.defaultSortOrders = makeSortOrders(direction, properties);
	}

	private static List<QuerySortOrder> makeSortOrders(Sort.Direction direction, String[] properties) {
		QuerySortOrderBuilder builder = new QuerySortOrderBuilder();
		for (String property : properties) {
			if (direction.isAscending()) {
				builder.thenAsc(property);
			} else {
				builder.thenDesc(property);
			}
		}
		return builder.build();
	}

	@Override
	protected Page<Order> fetchFromBackEnd(Query<Order, OrderFilter> query, Pageable pageable) {
		OrderFilter filter = query.getFilter().orElse(OrderFilter.getEmptyFilter());
		return ordersProvider.fetchFromBackEnd(filter, pageable);
	}

	@Override
	protected List<QuerySortOrder> getDefaultSortOrders() {
		return defaultSortOrders;
	}

	@Override
	protected int sizeInBackEnd(Query<Order, OrderFilter> query) {
		return (int) ordersProvider
				.countAnyMatchingAfterDueDate(query.getFilter().orElse(OrderFilter.getEmptyFilter()));
	}
}
