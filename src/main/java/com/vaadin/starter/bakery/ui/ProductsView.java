package com.vaadin.starter.bakery.ui;

import com.vaadin.annotations.*;
import com.vaadin.starter.bakery.app.HasLogger;
import com.vaadin.starter.bakery.ui.dataproviders.ProductsDataProvider;
import com.vaadin.starter.bakery.ui.entities.Product;
import com.vaadin.ui.AttachEvent;
import elemental.json.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

import com.vaadin.flow.router.View;
import com.vaadin.flow.template.PolymerTemplate;
import com.vaadin.flow.template.model.TemplateModel;
import com.vaadin.hummingbird.ext.spring.annotations.ParentView;
import com.vaadin.hummingbird.ext.spring.annotations.Route;
import com.vaadin.starter.bakery.backend.data.Role;
import com.vaadin.starter.bakery.ui.utils.BakeryConst;
import org.springframework.transaction.TransactionSystemException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Iterator;
import java.util.List;

@Tag("bakery-products")
@HtmlImport("frontend://src/products/bakery-products.html")
@Route(BakeryConst.PAGE_PRODUCTS)
@ParentView(BakeryApp.class)
@Secured(Role.ADMIN)
public class ProductsView extends PolymerTemplate<ProductsView.Model> implements View, HasLogger {

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

	@ClientDelegate
	public void saveProduct(JsonObject product){
		try {
			productsDataProvider.save(product);
			getModel().setProducts(productsDataProvider.findAll());
			getElement().callFunction("editProduct");
		} catch (ConstraintViolationException e) {
			String errorMessage = getErrorMessage(e);
			getElement().callFunction("showErrorMessage", errorMessage);
			getLogger().error("Error on saving product: " + errorMessage);
		} catch (TransactionSystemException e) {
			//TODO Figure out why ConstraintViolationException is being wrapped by TransactionSystemException on update
			String errorMessage = "Product could not be saved.";
			if (e.getRootCause() instanceof ConstraintViolationException) {
				errorMessage = getErrorMessage((ConstraintViolationException)e.getRootCause());
			}
			getElement().callFunction("showErrorMessage", errorMessage);
			getLogger().error("Error on saving product: " + errorMessage);
		} catch (Exception e) {
			getElement().callFunction("showErrorMessage", "Product could not be saved.");
			getLogger().error("Error on saving product: " + e.getMessage());
		}
	}

	private String getErrorMessage(ConstraintViolationException e) {
		String errorMessage = "";
		Iterator it = e.getConstraintViolations().iterator();
		while (it.hasNext()) {
			errorMessage += ((ConstraintViolation) it.next()).getMessage() + " ";
		}
		errorMessage = errorMessage.trim();
		return errorMessage;
	}
}
