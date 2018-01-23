package com.vaadin.starter.bakery.ui.views.storefront;

import java.util.stream.Stream;

import org.springframework.data.domain.PageRequest;

import com.vaadin.flow.data.provider.AbstractBackEndDataProvider;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.spring.annotation.SpringComponent;
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
		return productService.findAnyMatching(query.getFilter(), PageRequest.of(query.getOffset(), query.getLimit()))
				.stream();
	}

}
