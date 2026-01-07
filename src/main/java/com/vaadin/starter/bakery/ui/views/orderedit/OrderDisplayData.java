package com.vaadin.starter.bakery.ui.views.orderedit;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.vaadin.starter.bakery.backend.data.OrderState;
import com.vaadin.starter.bakery.backend.data.entity.Customer;
import com.vaadin.starter.bakery.backend.data.entity.Order;
import com.vaadin.starter.bakery.backend.data.entity.PickupLocation;
import com.vaadin.starter.bakery.ui.utils.converters.CurrencyFormatter;
import com.vaadin.starter.bakery.ui.utils.converters.LocalTimeConverter;
import com.vaadin.starter.bakery.ui.views.storefront.converters.StorefrontDate;
import com.vaadin.starter.bakery.ui.views.storefront.converters.StorefrontLocalDateConverter;

/**
 * Read-only DTO for displaying order details in the UI.
 * Contains all order data plus pre-formatted display values.
 */
public class OrderDisplayData {

    private final Long id;
    private final int version;
    private final LocalDate dueDate;
    private final LocalTime dueTime;
    private final PickupLocation pickupLocation;
    private final Customer customer;
    private final List<OrderItemDisplayData> items;
    private final OrderState state;
    private final List<HistoryItemDisplayData> history;
    private final Integer totalPrice;
    private final StorefrontDate formattedDueDate;
    private final String formattedDueTime;
    private final String formattedTotalPrice;

    private OrderDisplayData(Long id, int version, LocalDate dueDate, LocalTime dueTime,
                             PickupLocation pickupLocation, Customer customer,
                             List<OrderItemDisplayData> items, OrderState state,
                             List<HistoryItemDisplayData> history, Integer totalPrice,
                             StorefrontDate formattedDueDate, String formattedDueTime,
                             String formattedTotalPrice) {
        this.id = id;
        this.version = version;
        this.dueDate = dueDate;
        this.dueTime = dueTime;
        this.pickupLocation = pickupLocation;
        this.customer = customer;
        this.items = items;
        this.state = state;
        this.history = history;
        this.totalPrice = totalPrice;
        this.formattedDueDate = formattedDueDate;
        this.formattedDueTime = formattedDueTime;
        this.formattedTotalPrice = formattedTotalPrice;
    }

    public static OrderDisplayData fromOrder(Order order) {
        List<OrderItemDisplayData> items = order.getItems() != null
                ? order.getItems().stream().map(OrderItemDisplayData::fromOrderItem).toList()
                : null;

        List<HistoryItemDisplayData> history = order.getHistory() != null
                ? order.getHistory().stream().map(HistoryItemDisplayData::fromHistoryItem).toList()
                : null;

        return new OrderDisplayData(
                order.getId(),
                order.getVersion(),
                order.getDueDate(),
                order.getDueTime(),
                order.getPickupLocation(),
                order.getCustomer(),
                items,
                order.getState(),
                history,
                order.getTotalPrice(),
                new StorefrontLocalDateConverter().encode(order.getDueDate()),
                new LocalTimeConverter().encode(order.getDueTime()),
                new CurrencyFormatter().encode(order.getTotalPrice())
        );
    }

    public Long getId() {
        return id;
    }

    public int getVersion() {
        return version;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public LocalTime getDueTime() {
        return dueTime;
    }

    public PickupLocation getPickupLocation() {
        return pickupLocation;
    }

    public Customer getCustomer() {
        return customer;
    }

    public List<OrderItemDisplayData> getItems() {
        return items;
    }

    public OrderState getState() {
        return state;
    }

    public List<HistoryItemDisplayData> getHistory() {
        return history;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public StorefrontDate getFormattedDueDate() {
        return formattedDueDate;
    }

    public String getFormattedDueTime() {
        return formattedDueTime;
    }

    public String getFormattedTotalPrice() {
        return formattedTotalPrice;
    }
}
