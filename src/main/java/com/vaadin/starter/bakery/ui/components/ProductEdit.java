package com.vaadin.starter.bakery.ui.components;

import com.vaadin.annotations.HtmlImport;
import com.vaadin.annotations.Id;
import com.vaadin.annotations.Tag;
import com.vaadin.components.data.HasValue;
import com.vaadin.flow.event.ComponentEventListener;
import com.vaadin.flow.router.View;
import com.vaadin.flow.template.PolymerTemplate;
import com.vaadin.flow.template.model.TemplateModel;
import com.vaadin.shared.Registration;
import com.vaadin.starter.bakery.backend.data.entity.Product;
import com.vaadin.ui.Button;
import com.vaadin.ui.HasClickListeners;
import com.vaadin.ui.TextField;

import java.text.DecimalFormat;

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

	DecimalFormat df = new DecimalFormat("#.00");

	public ProductEdit() {
		initListeners();
	}

	private void initListeners() {
		nameField.addValueChangeListener(new HasValue.ValueChangeListener<TextField, String>() {
			@Override
			public void onComponentEvent(HasValue.ValueChangeEvent<TextField, String> valueChangeEvent) {
				saveButton.setDisabled(!isDirty());
			}
		});

		priceField.addValueChangeListener(new HasValue.ValueChangeListener<TextField, String>() {
			@Override
			public void onComponentEvent(HasValue.ValueChangeEvent<TextField, String> valueChangeEvent) {
				saveButton.setDisabled(!isDirty());
			}
		});
	}

	public int getProductId() {
		if (product != null && product.getId() != null) {
			return (int) product.getId().longValue();
		}
		return -1;
	}

	public Product getProduct() {
		if (product == null) {
			return null;
		}

		product.setName(nameField.getValue());
		product.setPrice(fromUiPrice());

		return product;
	}

	public void setProduct(Product product) {
		if (product == null) {
			this.product = null;
			return;
		}

		deleteButton.setDisabled(product.getId() == null);

		this.product = product;

		nameField.setValue(product.getName());
		priceField.setValue(toUiPrice());

	}

	public Registration addSaveListener(ComponentEventListener<HasClickListeners.ClickEvent<Button>> listener) {
		return saveButton.addClickListener(listener);
	}

	public Registration addDeleteListener(ComponentEventListener<HasClickListeners.ClickEvent<Button>> listener) {
		return deleteButton.addClickListener(listener);
	}

	public Registration addCancelListener(ComponentEventListener<HasClickListeners.ClickEvent<Button>> listener) {
		return cancelButton.addClickListener(listener);
	}

	public boolean isDirty() {
		if (product == null || product.getName() == null)
			return true;

		return (!product.getName().equals(nameField.getValue()) || product.getPrice() != fromUiPrice());
	}

	public boolean isValid() {
		return nameField.getValue() != null && !nameField.getValue().isEmpty() && fromUiPrice() > 0;
	}

	private String toUiPrice() {
		if (product == null) {
			return "0.00";
		} else {
			return df.format(product.getPrice() / 100f);
		}
	}

	private int fromUiPrice() {
		try {
			return (int) Math.round(Double.parseDouble(priceField.getValue()) * 100);
		} catch (Exception e) {
			return -1;
		}
	}

}