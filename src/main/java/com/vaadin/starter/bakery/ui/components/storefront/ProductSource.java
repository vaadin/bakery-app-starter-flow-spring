package com.vaadin.starter.bakery.ui.components.storefront;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import com.vaadin.data.Result;
import com.vaadin.data.ValueContext;
import com.vaadin.starter.bakery.backend.data.entity.Product;
import com.vaadin.starter.bakery.ui.converters.binder.BinderConverter;
import com.vaadin.ui.combobox.ComboBox;

class ProductSource implements BinderConverter<String, Product> {
	private final Map<String, Product> products;

	ProductSource(Collection<Product> availableProducts) {
		products = availableProducts.stream().collect(LinkedHashMap::new, (map, item) -> map.put(item.getName(), item),
				(map1, map2) -> map1.putAll(map2));
	}

	@Override
	public Result<Product> convertToModelIfNotNull(String arg0, ValueContext arg1) {
		return Result.ok(products.get(arg0));
	}

	@Override
	public String convertToPresentationIfNotNull(Product arg0, ValueContext arg1) {
		return arg0.getName();
	}

	void setupBeanComboBox(ComboBox<String> cb) {
		cb.setItems(products.keySet());
	}

	Product getProductByName(String name) {
		return products.get(name);
	}
}
