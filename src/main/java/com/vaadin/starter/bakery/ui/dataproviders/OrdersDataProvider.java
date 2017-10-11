package com.vaadin.starter.bakery.ui.dataproviders;

import java.time.LocalDate;
import java.time.MonthDay;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.vaadin.starter.bakery.backend.data.DashboardData;
import com.vaadin.starter.bakery.backend.data.OrderState;
import com.vaadin.starter.bakery.backend.data.entity.OrderItem;
import com.vaadin.starter.bakery.backend.service.OrderService;
import com.vaadin.starter.bakery.ui.entities.Customer;
import com.vaadin.starter.bakery.ui.entities.Order;
import com.vaadin.starter.bakery.ui.utils.DashboardUtils.PageInfo;

@Service
public class OrdersDataProvider {

	private final OrderService orderService;

	@Autowired
	public OrdersDataProvider(OrderService orderService) {
		this.orderService = orderService;
	}

	public PageInfo getOrdersList(String filter, boolean showPrevious, Pageable pageable) {
		List<Order> list = new ArrayList<>();
		fetchFromBackEnd(filter, showPrevious, pageable).forEach(entityOrder -> list.add(toUIEntity(entityOrder)));
		return new PageInfo(list, pageable.getPageNumber());
	}

	public long countAnyMatchingAfterDueDate() {
		return getOrderService().countAnyMatchingAfterDueDate(Optional.empty(), getFilterDate(false));
	}

	public List<com.vaadin.starter.bakery.backend.data.entity.Order> getOriginalOrdersList() {
		return fetchFromBackEnd(null, false, null).getContent();
	}

	public DashboardData getDashboardData() {
		return orderService.getDashboardData(MonthDay.now().getMonthValue(), Year.now().getValue());
	}

	protected Page<com.vaadin.starter.bakery.backend.data.entity.Order> fetchFromBackEnd(String filter,
			boolean showPrevious, Pageable pageable) {
		return getOrderService().findAnyMatchingAfterDueDate(Optional.ofNullable(filter), getFilterDate(showPrevious),
				pageable);
	}

	private Optional<LocalDate> getFilterDate(boolean showPrevious) {
		if (showPrevious) {
			return Optional.empty();
		}

		return Optional.of(LocalDate.now().minusDays(1));
	}

	private OrderService getOrderService() {
		return orderService;
	}

	public void addOrderComment(String orderId, String message) {
		orderService.addComment(DataProviderUtil.readId(orderId), message);
	}

	public Order getOrder(String orderId) {
		com.vaadin.starter.bakery.backend.data.entity.Order dataEntity = findOrderById(orderId);
		return toUIEntity(dataEntity);
	}

	private Order toUIEntity(com.vaadin.starter.bakery.backend.data.entity.Order dataEntity) {
		Order order = new Order();
		new UIOrderFiller().fill(order, dataEntity);
		return order;
	}

	private com.vaadin.starter.bakery.backend.data.entity.Order findOrderById(String orderId) {
		com.vaadin.starter.bakery.backend.data.entity.Order dataEntity = getOrderService()
				.findOrder(Long.valueOf(orderId));

		return dataEntity;
	}

	/**
	 * 
	 * Responsible for filling the UI Order object with the data from model object.
	 * 
	 */
	static class UIOrderFiller {

		void fill(Order uiEntity, com.vaadin.starter.bakery.backend.data.entity.Order dataEntity) {
			uiEntity.setId(dataEntity.getId().toString());
			uiEntity.setDate(dataEntity.getDueDate().toString());
			uiEntity.setTime(dataEntity.getDueTime().toString());

			uiEntity.setPlace(dataEntity.getPickupLocation().getName());
			uiEntity.setStatus(dataEntity.getState().getDisplayName());

			fillUICustomerEntity(uiEntity.getCustomer(), dataEntity.getCustomer());
			fillUIHistoryItems(uiEntity, dataEntity.getHistory());
			fillUIGoods(uiEntity, dataEntity.getItems());
		}

		private void fillUICustomerEntity(Customer uiCustomer,
				com.vaadin.starter.bakery.backend.data.entity.Customer dataEntity) {
			uiCustomer.setDetails(dataEntity.getDetails());
			uiCustomer.setName(dataEntity.getFullName());
			uiCustomer.setNumber(dataEntity.getPhoneNumber());
		}

		private void fillUIHistoryItems(Order uiEntity,
				List<com.vaadin.starter.bakery.backend.data.entity.HistoryItem> historyItems) {
			historyItems.forEach(h -> {
				uiEntity.addHistoryItem(h.getTimestamp().toString(), h.getMessage(), h.getCreatedBy().getFirstName(),
						DataProviderUtil.convertIfNotNull(h.getNewState(), OrderState::getDisplayName, () -> ""));
			});
		}

		private void fillUIGoods(Order uiEntity, List<OrderItem> orderItems) {
			orderItems.forEach(item -> {
				uiEntity.addUIGood(item.getQuantity(), item.getProduct().getName(), item.getProduct().getPrice(),
						item.getComment());
			});
		}
	}
}
