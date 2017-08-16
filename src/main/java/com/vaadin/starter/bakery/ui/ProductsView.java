package com.vaadin.starter.bakery.ui;

import com.google.gson.Gson;
import com.vaadin.annotations.*;
import com.vaadin.flow.router.LocationChangeEvent;
import com.vaadin.flow.router.View;
import com.vaadin.flow.template.PolymerTemplate;
import com.vaadin.flow.template.model.TemplateModel;
import com.vaadin.hummingbird.ext.spring.annotations.ParentView;
import com.vaadin.hummingbird.ext.spring.annotations.Route;
import com.vaadin.starter.bakery.app.HasLogger;
import com.vaadin.starter.bakery.backend.data.Role;
import com.vaadin.starter.bakery.backend.data.entity.Product;
import com.vaadin.starter.bakery.backend.service.ProductService;
import com.vaadin.starter.bakery.ui.converters.LongToStringConverter;
import com.vaadin.starter.bakery.ui.dataproviders.ProductsDataProvider;
import com.vaadin.starter.bakery.ui.utils.BakeryConst;
import com.vaadin.ui.AttachEvent;
import elemental.json.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.annotation.Secured;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;

@Tag("bakery-products")
@HtmlImport("frontend://src/products/bakery-products.html")
@Route(BakeryConst.PAGE_PRODUCTS + "/{id}")
@ParentView(BakeryApp.class)
@Secured(Role.ADMIN)
public class ProductsView extends PolymerTemplate<ProductsView.Model> implements View, HasToast, HasLogger {

	public interface Model extends TemplateModel {
		@Include({ "id", "name", "price" })
		@Convert(value = LongToStringConverter.class, path = "id")
		void setProducts(List<Product> products);

		String getFilterValue();
	}

	private final ProductsDataProvider productsDataProvider;
	private final ProductService service;

	@Autowired
	public ProductsView(ProductsDataProvider productsDataProvider, ProductService service) {
		this.productsDataProvider = productsDataProvider;
		this.service = service;
	}

	@Override
	protected void onAttach(AttachEvent attachEvent) {
		super.onAttach(attachEvent);

		getElement()
				.addEventListener("save", e -> saveProduct(e.getEventData().getObject("event.detail")), "event.detail");

		getElement().addEventListener("delete",
				e -> deleteProduct(Double.valueOf(e.getEventData().getNumber("event.detail.id")).longValue()),
				"event.detail.id");

		getElement()
				.addEventListener("edit", e -> editProduct(e.getEventData().getString("event.detail")), "event.detail");

		getElement().addEventListener("closed", e -> editProduct(null));
	}

	@Override
	public void onLocationChange(LocationChangeEvent locationChangeEvent) {
		setEditableProduct(locationChangeEvent.getPathParameter("id"));

	}

	private void setEditableProduct(String id) {
		if (id == null || id.isEmpty()) {
			return;
		}

		Long longId = null;
		try {
			longId = Long.parseLong(id);
			Product product = service.getRepository().findOne(longId);
			if (product == null) {
				String errorMessage = "Product with id " + id + " was not found.";
				toast(errorMessage, false);
				getLogger().error(errorMessage);
				return;
			}
			//Used direct call of client method, cause _editableItem is not accessible by Flow
			//if using getModel().set_editableItem()
			//InvalidTemplateModelException: has no property named _editableItem (or it has been excluded)
			getElement().callFunction("setEditableProduct", new Gson().toJson(product));
		} catch (NumberFormatException e) {
			toast("Wrong product id: " + id, false);
			getLogger().error("Failed to parse id: " + id);
		}
	}

	private void editProduct(String id) {
		if (id != null && !id.isEmpty()) {
			getUI().ifPresent(ui -> ui.navigateTo(BakeryConst.PAGE_PRODUCTS + "/" + id));
		} else {
			getUI().ifPresent(ui -> ui.navigateTo(BakeryConst.PAGE_PRODUCTS));
		}
	}

	@ClientDelegate
	public void onFilterProducts(String filterValue) {
		if (filterValue == null) {
			filterValue = "";
		}

		getModel().setProducts(service.findAnyMatching(Optional.of(filterValue), null).getContent());
	}

	private void saveProduct(JsonObject product) {
		try {
			productsDataProvider.save(product);
			onFilterProducts(getModel().getFilterValue());
		} catch (ConstraintViolationException e) {
			String errorMessage = getErrorMessage(e);
			toast(errorMessage, true);
			getLogger().error("Error on saving product: " + errorMessage);
		} catch (Exception e) {
			toast("Product could not be saved", true);
			getLogger().error("Error on saving product: " + e.getMessage());
		}
	}

	private void deleteProduct(long id) {
		try {
			service.delete(id);
			onFilterProducts(getModel().getFilterValue());
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
