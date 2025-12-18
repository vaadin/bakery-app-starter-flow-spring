package com.vaadin.starter.bakery.ui.views.orderedit;

import com.vaadin.starter.bakery.backend.data.entity.Product;
import com.vaadin.starter.bakery.ui.utils.converters.CurrencyFormatter;

/**
 * Read-only DTO for displaying product data in the UI.
 * Contains product fields plus formatted price.
 */
public class ProductDisplayData {

    private final Long id;
    private final int version;
    private final String name;
    private final Integer price;
    private final String formattedPrice;

    private ProductDisplayData(Long id, int version, String name, Integer price, String formattedPrice) {
        this.id = id;
        this.version = version;
        this.name = name;
        this.price = price;
        this.formattedPrice = formattedPrice;
    }

    public static ProductDisplayData fromProduct(Product product) {
        return new ProductDisplayData(
                product.getId(),
                product.getVersion(),
                product.getName(),
                product.getPrice(),
                new CurrencyFormatter().encode(product.getPrice())
        );
    }

    public Long getId() {
        return id;
    }

    public int getVersion() {
        return version;
    }

    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
    }

    public String getFormattedPrice() {
        return formattedPrice;
    }
}
