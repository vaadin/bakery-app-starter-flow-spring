package com.vaadin.starter.bakery.ui.dataproviders;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.MonthDay;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
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
import com.vaadin.starter.bakery.ui.utils.DashboardUtils.PageInfo;

import elemental.json.JsonObject;

@Service
public class OrdersDataProvider {

	private final OrderService orderService;
	private final PickupLocationService locationService;
	private final ProductsDataProvider productsProvider;

	@Autowired
	public OrdersDataProvider(ProductsDataProvider productsProvider, OrderService orderService,
			PickupLocationService locationService) {
		this.orderService = orderService;
		this.locationService = locationService;
		this.productsProvider = productsProvider;
	}

	public PageInfo getOrdersList(String filter, boolean showPrevious, Pageable pageable) {
		List<Order> list = new ArrayList<>();
		fetchFromBackEnd(filter, showPrevious, pageable)
				.forEach(entityOrder -> list.add(OrdersDataProvider.toUIEntity(entityOrder)));
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

	public static Order toUIEntity(com.vaadin.starter.bakery.backend.data.entity.Order entityOrder) {
		if (entityOrder == null)
			return null;
		Order result = new Order();
		result.setId(entityOrder.getId().toString());
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
			uiItem.setName(dataItem.getCreatedBy().getFirstName());
			uiItem.setStatus(dataItem.getNewState().getDisplayName());
			uiHistory.add(uiItem);
		}
		return uiHistory;
	}

	protected Page<com.vaadin.starter.bakery.backend.data.entity.Order> fetchFromBackEnd(String filter,
			boolean showPrevious, Pageable pageable) {
		return getOrderService().findAnyMatchingAfterDueDate(getFilter(filter), getFilterDate(showPrevious), pageable);
	}

	private Optional<String> getFilter(String filter) {
		if (filter == null) {
			return Optional.empty();
		}

		return Optional.of(filter);
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

	private PickupLocationService getLocationService() {
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
			dataEntity = getOrderService().findOrder(Long.valueOf(uiEntity.getId()));
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

			item.setProduct(productsProvider.getProduct(good.getName()));
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
