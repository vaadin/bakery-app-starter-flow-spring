package com.vaadin.starter.bakery.ui.view.storefront;

import java.util.stream.Stream;

import org.springframework.data.domain.PageRequest;

import com.vaadin.data.provider.AbstractBackEndDataProvider;
import com.vaadin.data.provider.Query;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.starter.bakery.backend.data.entity.Product;
import com.vaadin.starter.bakery.backend.service.ProductService;

@SpringComponent
class ProductDataProvider extends AbstractBackEndDataProvider<Product, String> {

	private final ProductService productService;

	public ProductDataProvider(ProductService productService) {
		this.productService = productService;
	}

	@Override
	protected int sizeInBackEnd(Query<Product, String> query) {
		return (int) productService.countAnyMatching(query.getFilter());
	}

	@Override
	public Stream<Product> fetchFromBackEnd(Query<Product, String> query) {
		return productService.findAnyMatching(query.getFilter(), new PageRequest(query.getOffset(), query.getLimit()))
				.stream();
	}

}
