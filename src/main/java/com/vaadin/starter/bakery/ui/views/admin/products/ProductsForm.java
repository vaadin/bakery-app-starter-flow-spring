package com.vaadin.starter.bakery.ui.views.admin.products;

import java.util.Currency;

import com.vaadin.flow.component.HasText;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.templatemodel.TemplateModel;
import com.vaadin.starter.bakery.ui.components.FormButtonsBar;
import com.vaadin.starter.bakery.ui.crud.CrudView.CrudForm;
import com.vaadin.starter.bakery.ui.utils.BakeryConst;

@Tag("product-form")
@HtmlImport("src/views/admin/products/product-form.html")
public class ProductsForm<Product> extends PolymerTemplate<TemplateModel> implements CrudForm<Product> {

	@Id("title")
	private H3 title;

	@Id("buttons")
	private FormButtonsBar buttons;

	@Id("product-edit-name")
	private TextField nameField;

	@Id("price")
	private TextField priceField;

	@Override
	public void setBinder(BeanValidationBinder<Product> binder) {
		binder.bind(nameField, "name");
		binder.forField(priceField).withConverter(new PriceConverter()).bind("price");
		priceField.addToPrefix(new Span(Currency.getInstance(BakeryConst.APP_LOCALE).getSymbol()));
	}

	@Override
	public FormButtonsBar getButtons() {
		return buttons;
	}

	@Override
	public HasText getTitle() {
		return title;
	}
}
