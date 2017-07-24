package com.vaadin.starter.bakery.ui.dataproviders;

import com.google.gson.Gson;
import com.vaadin.starter.bakery.backend.service.ProductService;
import com.vaadin.starter.bakery.ui.entities.Product;
import elemental.json.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.ValidationException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

	public void save(JsonObject product) throws ValidationException {
		com.vaadin.starter.bakery.backend.data.entity.Product productToSave = toDataEntity(product);

		if (productToSave.getName() == null || productToSave.getName().trim().length() == 0) {
			throw new ValidationException("Name is required.");
		}

		if (productToSave.getPrice() <= 0) {
			throw new ValidationException("Price is required and must be higher than zero.");
		}

		productService.save(productToSave);
	}

	private Product toUiEntity(com.vaadin.starter.bakery.backend.data.entity.Product product) {
		Product uiEntity = new Product();

		uiEntity.setId(product.getId().toString());
		uiEntity.setName(product.getName());
		uiEntity.setPrice(product.getPrice());

		return uiEntity;
	}

	private com.vaadin.starter.bakery.backend.data.entity.Product toDataEntity(
			JsonObject user) {
		Gson gson = new Gson();
		Product uiEntity = gson.fromJson(user.toJson(), Product.class);
		return toDataEntity(uiEntity);
	}

	private com.vaadin.starter.bakery.backend.data.entity.Product toDataEntity(Product product) {
		com.vaadin.starter.bakery.backend.data.entity.Product dataEntity = null;

		if (product.getId() != null) {
			long id = Long.parseLong(product.getId());
			dataEntity = productService.load(id);
		}

		if (dataEntity == null) {
			dataEntity = new com.vaadin.starter.bakery.backend.data.entity.Product();
		}

		dataEntity.setName(product.getName());
		dataEntity.setPrice(product.getPrice());

		return dataEntity;
	}
}
