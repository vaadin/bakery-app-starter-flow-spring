package com.vaadin.starter.bakery.ui.dataproviders;

import com.vaadin.starter.bakery.backend.service.ProductService;
import com.vaadin.starter.bakery.ui.entities.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductsDataProvider {

	private final ProductService productService;

	@Autowired
	public ProductsDataProvider(ProductService productService) {
		this.productService = productService;
	}

	public List<Product> findAll() {
		return productService.getRepository().findAll().stream().map(this::toUiEntity).collect(Collectors.toList());
	}

	public List<Product> findByName(String name) {
		if (null == name) {
			return this.findAll();
		}
		String lowerName = name.toLowerCase();
		return productService.getRepository().findAll().stream().filter(product -> product.getName().toLowerCase().contains(lowerName)).map(this::toUiEntity).collect(Collectors.toList());
	}

	private Product toUiEntity(com.vaadin.starter.bakery.backend.data.entity.Product product) {
		Product uiEntity = new Product();

		uiEntity.setId(product.getId().toString());
		uiEntity.setName(product.getName());
		uiEntity.setPrice(product.getPrice());

		return uiEntity;
	}
}
