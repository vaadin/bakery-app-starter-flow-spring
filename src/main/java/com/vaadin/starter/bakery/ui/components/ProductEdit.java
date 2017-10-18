package com.vaadin.starter.bakery.ui.components;

import com.vaadin.flow.model.TemplateModel;
import com.vaadin.shared.Registration;
import com.vaadin.starter.bakery.backend.data.entity.Product;
import com.vaadin.starter.bakery.ui.utils.FormattingUtils;
import com.vaadin.ui.Tag;
import com.vaadin.ui.button.Button;
import com.vaadin.ui.common.HasClickListeners;
import com.vaadin.ui.common.HtmlImport;
import com.vaadin.ui.event.ComponentEventListener;
import com.vaadin.ui.html.Div;
import com.vaadin.ui.html.H3;
import com.vaadin.ui.polymertemplate.EventHandler;
import com.vaadin.ui.polymertemplate.Id;
import com.vaadin.ui.polymertemplate.PolymerTemplate;
import com.vaadin.ui.textfield.TextField;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Currency;
import java.util.Locale;

import static com.vaadin.starter.bakery.ui.utils.FormattingUtils.DECIMAL_ZERO;

@Tag("product-edit")
@HtmlImport("context://src/products/product-edit.html")
public class ProductEdit extends PolymerTemplate<TemplateModel> {

	@Id("product-edit-title")
	private H3 title;

	@Id("product-edit-name")
	private TextField nameField;

	@Id("price")
	private TextField priceField;

	@Id("product-edit-save")
	private Button saveButton;

	@Id("product-edit-delete")
	private Button deleteButton;

	@Id("product-edit-cancel")
	private Button cancelButton;

	private Product product;

	private final DecimalFormat df = FormattingUtils.getUiPriceFormatter();

	public ProductEdit() {
		nameField.addValueChangeListener(valueChangeEvent -> saveButton.setDisabled(!isDirty()));
		priceField.addValueChangeListener(valueChangeEvent -> saveButton.setDisabled(!isDirty()));
		title.setText("New Product");
		setCurrencySymbol();
	}

	private void setCurrencySymbol() {
		Div currencySign = new Div();
		Currency currency = Currency.getInstance(Locale.getDefault());
		currencySign.setText(currency.getSymbol());
		priceField.addToPrefix(currencySign);
	}

	public Long getProductId() {
		return product == null ? null : product.getId();
	}

	public Product getProduct() {
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

	public Registration addSaveListener(ComponentEventListener<HasClickListeners.ClickEvent<Button>> listener) {
		return saveButton.addClickListener(listener);
	}

	public Registration addDeleteListener(ComponentEventListener<HasClickListeners.ClickEvent<Button>> listener) {
		return deleteButton.addClickListener(listener);
	}

	public Registration addCancelListener(ComponentEventListener<HasClickListeners.ClickEvent<Button>> listener) {
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

		return !nameField.getValue().trim().isEmpty() || fromUiPrice() > 0;
	}

	private String toUiPrice() {
		return product == null ? DECIMAL_ZERO : df.format(product.getPrice() / 100d);
	}

	private int fromUiPrice() {
		if (priceField.getValue() == null || priceField.getValue().isEmpty()) {
			return 0;
		}
		try {
			return (int) Math.round(df.parse(priceField.getValue()).doubleValue() * 100);
		} catch (ParseException e) {
			return 0;
		}
	}
}