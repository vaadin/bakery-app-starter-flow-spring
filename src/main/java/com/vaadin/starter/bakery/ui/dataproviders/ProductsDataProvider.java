package com.vaadin.starter.bakery.ui.dataproviders;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vaadin.starter.bakery.app.BeanLocator;
import com.vaadin.starter.bakery.backend.data.entity.Product;
import com.vaadin.starter.bakery.backend.service.ProductService;

@Service
public class ProductsDataProvider {

	@Autowired
	private static ProductService productsService;

	private static ProductService getService() {
		if (productsService == null)
			productsService = BeanLocator.find(ProductService.class);

		return productsService;
	}

	public static List<Product> allProducts() {
		return getService().find(null).getContent();
	}

	public static Product getProduct(String name) {
		List<Product> products = getService().findAnyMatching(Optional.of(name), null).getContent();
		if (products == null || products.isEmpty()) {
			return null;
		}

		return products.get(0);
	}

	public static List<com.vaadin.starter.bakery.ui.entities.Product> getUiProducts() {
		return allProducts().stream().map(ProductsDataProvider::toUiEntity).collect(Collectors.toList());
	}

	private static com.vaadin.starter.bakery.ui.entities.Product toUiEntity(Product dataEntity) {
		com.vaadin.starter.bakery.ui.entities.Product uiProduct = new com.vaadin.starter.bakery.ui.entities.Product();
		uiProduct.setProductName(dataEntity.getName());
		uiProduct.setUnitPrice(dataEntity.getPrice());

		return uiProduct;

	}

}
