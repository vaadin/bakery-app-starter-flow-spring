package com.vaadin.starter.bakery.ui;

import com.vaadin.annotations.ClientDelegate;
import com.vaadin.starter.bakery.ui.dataproviders.ProductsDataProvider;
import com.vaadin.starter.bakery.ui.entities.Product;
import com.vaadin.ui.AttachEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

import com.vaadin.annotations.HtmlImport;
import com.vaadin.annotations.Tag;
import com.vaadin.flow.router.View;
import com.vaadin.flow.template.PolymerTemplate;
import com.vaadin.flow.template.model.TemplateModel;
import com.vaadin.hummingbird.ext.spring.annotations.ParentView;
import com.vaadin.hummingbird.ext.spring.annotations.Route;
import com.vaadin.starter.bakery.backend.data.Role;
import com.vaadin.starter.bakery.ui.utils.BakeryConst;

import java.util.List;
import java.util.stream.Collectors;

@Tag("bakery-products")
@HtmlImport("frontend://src/products/bakery-products.html")
@Route(BakeryConst.PAGE_PRODUCTS)
@ParentView(BakeryApp.class)
@Secured(Role.ADMIN)
public class ProductsView extends PolymerTemplate<ProductsView.Model> implements View {

	public interface Model extends TemplateModel {
		void setProducts(List<Product> products);
	}

	private final ProductsDataProvider productsDataProvider;

	@Autowired
	public ProductsView(ProductsDataProvider productsDataProvider) {
		this.productsDataProvider = productsDataProvider;
	}

	@Override
	protected void onAttach(AttachEvent attachEvent) {
		super.onAttach(attachEvent);
		getModel().setProducts(productsDataProvider.findAll());
	}

	@ClientDelegate
	public void onFilterProducts(String filterValue) {
		getModel().setProducts(productsDataProvider.findByName(filterValue));
	}
}
