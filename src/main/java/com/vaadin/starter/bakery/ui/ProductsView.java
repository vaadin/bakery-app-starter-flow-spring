package com.vaadin.starter.bakery.ui;

import java.util.List;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.annotation.Secured;

import com.google.gson.Gson;
import com.vaadin.annotations.ClientDelegate;
import com.vaadin.annotations.HtmlImport;
import com.vaadin.annotations.Tag;
import com.vaadin.flow.router.LocationChangeEvent;
import com.vaadin.flow.router.View;
import com.vaadin.flow.template.PolymerTemplate;
import com.vaadin.flow.template.model.TemplateModel;
import com.vaadin.hummingbird.ext.spring.annotations.ParentView;
import com.vaadin.hummingbird.ext.spring.annotations.Route;
import com.vaadin.starter.bakery.app.HasLogger;
import com.vaadin.starter.bakery.backend.data.Role;
import com.vaadin.starter.bakery.ui.dataproviders.ProductsDataProvider;
import com.vaadin.starter.bakery.ui.entities.Product;
import com.vaadin.starter.bakery.ui.utils.BakeryConst;
import com.vaadin.ui.AttachEvent;

import elemental.json.JsonObject;

@Tag("bakery-products")
@HtmlImport("frontend://src/products/bakery-products.html")
@Route(BakeryConst.PAGE_PRODUCTS + "/{id}")
@ParentView(BakeryApp.class)
@Secured(Role.ADMIN)
public class ProductsView extends PolymerTemplate<ProductsView.Model> implements View, HasToast, HasLogger {

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

		getElement().addEventListener("save", e -> saveProduct(e.getEventData().getObject("event.detail")),
				"event.detail");

		getElement().addEventListener("delete", e -> deleteProduct(e.getEventData().getString("event.detail.id")),
				"event.detail.id");

		getElement().addEventListener("closed", e -> editProduct(null));
	}

	@Override
	public void onLocationChange(LocationChangeEvent locationChangeEvent) {
		setEditableProduct(locationChangeEvent.getPathParameter("id"));

	}

	private void setEditableProduct(String id) {
		Long longId = null;
		try {
			longId = Long.parseLong(id);
			Product product = productsDataProvider.getById(longId);
			getElement().callFunction("setEditableProduct", new Gson().toJson(product));
		} catch (Exception e) {
		}
	}

	@ClientDelegate
	private void editProduct(String id) {
		if (id != null && !id.isEmpty()) {
			getUI().get().navigateTo(BakeryConst.PAGE_PRODUCTS + "/" + id);
		} else {
			getUI().get().navigateTo(BakeryConst.PAGE_PRODUCTS);
		}
	}

	@ClientDelegate
	public void onFilterProducts(String filterValue) {
		getModel().setProducts(productsDataProvider.findByName(filterValue));
	}

	private void saveProduct(JsonObject product) {
		try {
			productsDataProvider.save(product);
			getModel().setProducts(productsDataProvider.findAll());
		} catch (ConstraintViolationException e) {
			String errorMessage = getErrorMessage(e);
			toast(errorMessage, true);
			getLogger().error("Error on saving product: " + errorMessage);
		} catch (Exception e) {
			toast("Product could not be saved", true);
			getLogger().error("Error on saving product: " + e.getMessage());
		}
	}

	private void deleteProduct(String id) {
		try {
			productsDataProvider.delete(Long.parseLong(id));
			getModel().setProducts(productsDataProvider.findAll());
		} catch (Exception e) {
			String message = "Product could not be deleted";
			if (e instanceof DataIntegrityViolationException) {
				message += " because it is currently in use";
			}
			toast(message, true);
			getLogger().error("Error on deleting product: " + e.getMessage());
		}
	}

	private String getErrorMessage(ConstraintViolationException e) {
		StringBuilder errorMessage = new StringBuilder();
		e.getConstraintViolations().forEach(msg -> errorMessage.append(msg.getMessage()).append(" "));
		return errorMessage.toString().trim();
	}
}
