package com.vaadin.starter.bakery.ui.views.orderedit;

import com.vaadin.starter.bakery.backend.data.entity.OrderItem;

/**
 * Read-only DTO for displaying order item data in the UI.
 */
public class OrderItemDisplayData {

    private final Long id;
    private final int version;
    private final ProductDisplayData product;
    private final Integer quantity;
    private final String comment;
    private final int totalPrice;

    private OrderItemDisplayData(Long id, int version, ProductDisplayData product,
                                 Integer quantity, String comment, int totalPrice) {
        this.id = id;
        this.version = version;
        this.product = product;
        this.quantity = quantity;
        this.comment = comment;
        this.totalPrice = totalPrice;
    }

    public static OrderItemDisplayData fromOrderItem(OrderItem orderItem) {
        return new OrderItemDisplayData(
                orderItem.getId(),
                orderItem.getVersion(),
                ProductDisplayData.fromProduct(orderItem.getProduct()),
                orderItem.getQuantity(),
                orderItem.getComment(),
                orderItem.getTotalPrice()
        );
    }

    public Long getId() {
        return id;
    }

    public int getVersion() {
        return version;
    }

    public ProductDisplayData getProduct() {
        return product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public String getComment() {
        return comment;
    }

    public int getTotalPrice() {
        return totalPrice;
    }
}
