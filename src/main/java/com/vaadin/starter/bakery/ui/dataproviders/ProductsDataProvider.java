package com.vaadin.starter.bakery.ui.dataproviders;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vaadin.starter.bakery.backend.service.ProductService;
import com.vaadin.starter.bakery.ui.entities.Product;

@Service
public class ProductsDataProvider {

	private final ProductService productService;

	@Autowired
	public ProductsDataProvider(ProductService productService) {
		this.productService = productService;
	}

	private ProductService getService() {
		return productService;
	}

	public com.vaadin.starter.bakery.backend.data.entity.Product getProduct(String name) {
		List<com.vaadin.starter.bakery.backend.data.entity.Product> products = findAnyMatchingProducts(name);
		if (products == null || products.isEmpty()) {
			return null;
		}

		return products.get(0);
	}

	public List<Product> findAll() {
		return productService.getRepository().findAll().stream().map(this::toUiEntity).collect(Collectors.toList());
	}

	private List<com.vaadin.starter.bakery.backend.data.entity.Product> findAnyMatchingProducts(String name) {
		return getService().findAnyMatching(Optional.of(name), null).getContent();
	}

	public List<Product> findByName(String name) {
		if (null == name) {
			return this.findAll();
		}

		return findAnyMatchingProducts(name).stream().map(this::toUiEntity).collect(Collectors.toList());
	}

	private Product toUiEntity(com.vaadin.starter.bakery.backend.data.entity.Product product) {
		Product uiEntity = new Product();

		uiEntity.setId(product.getId().toString());
		uiEntity.setName(product.getName());
		uiEntity.setPrice(product.getPrice());

		return uiEntity;
	}
}
