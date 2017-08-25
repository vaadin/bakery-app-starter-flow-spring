package com.vaadin.starter.bakery.ui.components;

import com.vaadin.annotations.EventHandler;
import com.vaadin.annotations.HtmlImport;
import com.vaadin.annotations.Id;
import com.vaadin.annotations.Tag;
import com.vaadin.flow.event.ComponentEventListener;
import com.vaadin.flow.router.View;
import com.vaadin.flow.template.PolymerTemplate;
import com.vaadin.flow.template.model.TemplateModel;
import com.vaadin.shared.Registration;
import com.vaadin.starter.bakery.backend.data.entity.Product;
import com.vaadin.ui.Button;
import com.vaadin.ui.HasClickListeners.ClickEvent;
import com.vaadin.ui.TextField;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static com.vaadin.starter.bakery.ui.utils.BakeryConst.PRODUCT_NAME_VALIDATION_MESSAGE;
import static com.vaadin.starter.bakery.ui.utils.BakeryConst.PRODUCT_PRICE_VALIDATION_MESSAGE;

@Tag("product-edit")
@HtmlImport("frontend://src/products/product-edit.html")
public class ProductEdit extends PolymerTemplate<TemplateModel> implements View {

	@Id("name")
	private TextField nameField;

	@Id("price")
	private TextField priceField;

	@Id("save")
	private Button saveButton;

	@Id("delete")
	private Button deleteButton;

	@Id("cancel")
	private Button cancelButton;

	private Product product;

	private static final String DECIMAL_ZERO = "0.00";
	private static final DecimalFormat df = new DecimalFormat("#" + DECIMAL_ZERO);

	public ProductEdit() {
		nameField.addValueChangeListener(valueChangeEvent -> saveButton.setDisabled(!isDirty()));
		priceField.addValueChangeListener(valueChangeEvent -> saveButton.setDisabled(!isDirty()));
	}

	public int getProductId() {
		if (product != null && product.getId() != null) {
			return product.getId().intValue();
		}

		return -1;
	}

	public Product getProduct() {
		if (product != null) {
			product.setName(nameField.getValue());
			product.setPrice(fromUiPrice());
		}

		return product;
	}

	public void setProduct(Product product) {
		if (product != null) {
			deleteButton.setDisabled(product.getId() == null);
			nameField.setValue(product.getName());
		}

		this.product = product;
		priceField.setValue(toUiPrice());
	}

	public Registration addSaveListener(ComponentEventListener<ClickEvent<Button>> listener) {
		return saveButton.addClickListener(listener);
	}

	public Registration addDeleteListener(ComponentEventListener<ClickEvent<Button>> listener) {
		return deleteButton.addClickListener(listener);
	}

	public Registration addCancelListener(ComponentEventListener<ClickEvent<Button>> listener) {
		return cancelButton.addClickListener(listener);
	}

	@EventHandler
	public void priceFocusGained() {
		if (DECIMAL_ZERO.equals(priceField.getValue())) {
			priceField.setValue("");
		}
	}

	@EventHandler
	public void priceFocusLost() {
		if (fromUiPrice() <= 0) {
			priceField.setValue(DECIMAL_ZERO);
		}
	}

	public boolean isDirty() {
		if (product != null && product.getId() != null) {
			return !product.getName().equals(nameField.getValue()) || product.getPrice() != fromUiPrice();
		}

		return !(nameField.isEmpty() || nameField.getValue().trim().isEmpty()) || fromUiPrice() > 0;
	}

	public List<String> validate() {
		final List<String> errors = new ArrayList<>(2);
		if (nameField.isEmpty() || nameField.getValue().trim().isEmpty()) {
			errors.add(PRODUCT_NAME_VALIDATION_MESSAGE);
		}
		if (fromUiPrice() <= 0) {
			errors.add(PRODUCT_PRICE_VALIDATION_MESSAGE);
		}

		return errors;
	}

	private String toUiPrice() {
		return product == null ? DECIMAL_ZERO : df.format(product.getPrice() / 100f);
	}

	private int fromUiPrice() {
		try {
			return (int) Math.round(Double.parseDouble(priceField.getValue()) * 100);
		} catch (NullPointerException | NumberFormatException e) {
			return -1;
		}
	}
}