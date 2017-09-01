package com.vaadin.starter.bakery.ui.components;

import com.vaadin.annotations.EventHandler;
import com.vaadin.annotations.HtmlImport;
import com.vaadin.annotations.Id;
import com.vaadin.annotations.Tag;
import com.vaadin.flow.event.ComponentEventListener;
import com.vaadin.flow.html.H3;
import com.vaadin.flow.router.View;
import com.vaadin.flow.template.PolymerTemplate;
import com.vaadin.flow.template.model.TemplateModel;
import com.vaadin.shared.Registration;
import com.vaadin.starter.bakery.backend.data.entity.Product;
import com.vaadin.ui.Button;
import com.vaadin.ui.HasClickListeners.ClickEvent;
import com.vaadin.ui.TextField;

import java.text.DecimalFormat;
import java.text.ParseException;

@Tag("product-edit")
@HtmlImport("context://src/products/product-edit.html")
public class ProductEdit extends PolymerTemplate<TemplateModel> implements View {

	@Id("title")
	private H3 title;

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
		title.setText("New Product");
	}

	public Long getProductId() {
		return product == null ? null : product.getId();
	}

	public Product getProduct() throws ParseException {
		if (product != null) {
			product.setName(nameField.getValue());
			product.setPrice(fromUiPrice());
		}

		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
		nameField.setValue(product.getName());
		priceField.setValue(toUiPrice());
		deleteButton.setDisabled(product.getId() == null);
		title.setText((product.getId() == null ? "New" : "Edit") + " Product");
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
		if (priceField.getValue().isEmpty()) {
			priceField.setValue(DECIMAL_ZERO);
		}
	}

	public boolean isDirty() {
		try {
			if (product != null && product.getId() != null) {
				return !product.getName().equals(nameField.getValue()) || product.getPrice() != fromUiPrice();
			}
			return !nameField.getValue().trim().isEmpty() || fromUiPrice() > 0;
		} catch (ParseException e) {
			return true;
		}
	}

	private String toUiPrice() {
		return product == null ? DECIMAL_ZERO : df.format(product.getPrice() / 100f);
	}

	private int fromUiPrice() throws ParseException {
		return priceField.getValue().isEmpty() ? 0
				: (int) Math.round(df.parse(priceField.getValue()).doubleValue() * 100);
	}
}