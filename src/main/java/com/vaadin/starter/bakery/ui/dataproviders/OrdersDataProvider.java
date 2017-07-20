package com.vaadin.starter.bakery.ui.dataproviders;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.MonthDay;
import java.time.Year;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.vaadin.starter.bakery.app.BeanLocator;
import com.vaadin.starter.bakery.backend.data.DashboardData;
import com.vaadin.starter.bakery.backend.data.OrderState;
import com.vaadin.starter.bakery.backend.data.entity.OrderItem;
import com.vaadin.starter.bakery.backend.data.entity.PickupLocation;
import com.vaadin.starter.bakery.backend.service.OrderService;
import com.vaadin.starter.bakery.backend.service.PickupLocationService;
import com.vaadin.starter.bakery.ui.entities.Customer;
import com.vaadin.starter.bakery.ui.entities.Good;
import com.vaadin.starter.bakery.ui.entities.HistoryItem;
import com.vaadin.starter.bakery.ui.entities.Order;

import elemental.json.JsonObject;

@Service
public class OrdersDataProvider {

	@Autowired
	private OrderService orderService;

	@Autowired
	private PickupLocationService locationService;

	private LocalDate filterDate = LocalDate.now().minusDays(1);

	private static OrdersDataProvider provider;

	public static OrdersDataProvider get() {
		if (provider == null) {
			provider = new OrdersDataProvider();
		}
		return provider;
	}

	public List<Order> getOrdersList(int start, int count) {
		List<Order> result = getOrdersList();
		if (start > result.size())
			return Collections.emptyList();

		if (start + count > result.size())
			return result.subList(start, result.size() - 1);

		return result.subList(start, start + count);
	}

	public List<com.vaadin.starter.bakery.backend.data.entity.Order> getOriginalOrdersList() {
		return fetchFromBackEnd(null).getContent();
	}

	public List<Order> getOrdersList() {
		List<Order> list = new ArrayList<>();
		fetchFromBackEnd(null).forEach(entityOrder -> list.add(OrdersDataProvider.toUIEntity(entityOrder)));

		return list;

	}

	public DashboardData getDashboardData() {
		return orderService.getDashboardData(MonthDay.now().getMonthValue(), Year.now().getValue());
	}

	public static Order toUIEntity(com.vaadin.starter.bakery.backend.data.entity.Order entityOrder) {
		if (entityOrder == null)
			return null;
		Order result = new Order();
		result.setId(entityOrder.getId().intValue());
		result.setCustomer(toUICustomerEntity(entityOrder.getCustomer()));

		result.setDate(entityOrder.getDueDate().toString());
		result.setTime(entityOrder.getDueTime().toString());
		List<Good> goods = new ArrayList<>();
		AtomicInteger totalPrice = new AtomicInteger(0);

		entityOrder.getItems().stream().forEach(item -> {
			goods.add(new Good(item.getQuantity(), item.getProduct().getName(), item.getProduct().getPrice(),
					item.getComment()));
			totalPrice.addAndGet(item.getQuantity() * item.getProduct().getPrice());
		});

		result.setGoods(goods);
		result.setTotalPrice(totalPrice.get());
		result.setPlace(entityOrder.getPickupLocation().getName());
		result.setStatus(entityOrder.getState().getDisplayName());
		result.setHistory(toUIHistoryItems(entityOrder.getHistory()));

		return result;

	}

	private static List<HistoryItem> toUIHistoryItems(
			List<com.vaadin.starter.bakery.backend.data.entity.HistoryItem> dataHistory) {
		ArrayList<HistoryItem> uiHistory = new ArrayList<HistoryItem>();
		for (com.vaadin.starter.bakery.backend.data.entity.HistoryItem dataItem : dataHistory) {
			HistoryItem uiItem = new HistoryItem();
			uiItem.setDate(dataItem.getTimestamp().toString());
			uiItem.setMessage(dataItem.getMessage());
			uiItem.setName(dataItem.getCreatedBy().getName());
			uiItem.setStatus(dataItem.getNewState().getDisplayName());
			uiHistory.add(uiItem);
		}
		return uiHistory;
	}

	protected Page<com.vaadin.starter.bakery.backend.data.entity.Order> fetchFromBackEnd(Pageable pageable) {
		return getOrderService().findAnyMatchingAfterDueDate(Optional.empty(), getOptionalFilterDate(), pageable);
	}

	private Optional<LocalDate> getOptionalFilterDate() {
		if (filterDate == null) {
			return Optional.empty();
		} else {
			return Optional.of(filterDate);
		}
	}

	public void setIncludePast(boolean includePast) {
		if (includePast) {
			filterDate = null;
		} else {
			filterDate = LocalDate.now().minusDays(1);
		}
	}

	private OrderService getOrderService() {
		if (orderService == null) {
			orderService = BeanLocator.find(OrderService.class);
		}
		return orderService;
	}

	private PickupLocationService getLocationService() {
		if (locationService == null) {
			locationService = BeanLocator.find(PickupLocationService.class);
		}
		return locationService;
	}

	public void save(JsonObject order) {
		getOrderService().saveOrder(toDataEntity(order));
	}

	private com.vaadin.starter.bakery.backend.data.entity.Order toDataEntity(JsonObject jsonOrder) {
		com.vaadin.starter.bakery.backend.data.entity.Order dataEntity = null;
		Gson gson = new Gson();
		Order uiEntity = gson.fromJson(jsonOrder.toJson(), Order.class);
		try {
			dataEntity = getOrderService().findOrder((long) uiEntity.getId());
		} catch (NumberFormatException e) {
		}

		if (dataEntity == null) {
			dataEntity = new com.vaadin.starter.bakery.backend.data.entity.Order();
			dataEntity.setState(OrderState.NEW);
		}

		dataEntity.setCustomer(toDataCustomerEntity(uiEntity.getCustomer()));
		LocalDate date;
		try {
			date = LocalDate.parse(uiEntity.getDate());
		} catch (Exception e) {
			date = LocalDate.now();
		}

		dataEntity.setDueDate(date);
		dataEntity.setDueTime(LocalTime.parse(uiEntity.getTime()));
		dataEntity.setItems(toDataOrderItemListEntity(uiEntity.getGoods()));
		dataEntity.setPickupLocation(toDataPickupLocation(uiEntity.getPlace()));

		return dataEntity;
	}

	private PickupLocation toDataPickupLocation(String place) {
		PickupLocation pickupLocation = getLocationService().findAnyMatching(Optional.of(place), null).getContent()
				.get(0);
		return pickupLocation;
	}

	private List<OrderItem> toDataOrderItemListEntity(List<Good> goods) {
		ArrayList<OrderItem> items = new ArrayList<OrderItem>();
		for (Good good : goods) {
			if (good.getName() == null)
				continue;
			OrderItem item = new OrderItem();
			item.setComment(good.getDescription());

			item.setProduct(ProductsDataProvider.getProduct(good.getName()));
			item.setQuantity(good.getCount());
			items.add(item);
		}
		return items;
	}

	private static Customer toUICustomerEntity(com.vaadin.starter.bakery.backend.data.entity.Customer dataEntity) {
		Customer uiEntity = new Customer();

		uiEntity.setDetails(dataEntity.getDetails());
		uiEntity.setName(dataEntity.getFullName());
		uiEntity.setNumber(dataEntity.getPhoneNumber());

		return uiEntity;
	}

	private static com.vaadin.starter.bakery.backend.data.entity.Customer toDataCustomerEntity(Customer uiCustomer) {
		com.vaadin.starter.bakery.backend.data.entity.Customer dataEntity = new com.vaadin.starter.bakery.backend.data.entity.Customer();

		dataEntity.setFullName(uiCustomer.getName());
		dataEntity.setPhoneNumber(uiCustomer.getNumber());
		dataEntity.setDetails(uiCustomer.getDetails());

		return dataEntity;
	}

}
