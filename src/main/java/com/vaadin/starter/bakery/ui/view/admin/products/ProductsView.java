package com.vaadin.starter.bakery.ui.view.admin.products;

import static com.vaadin.starter.bakery.ui.utils.BakeryConst.PAGE_PRODUCTS;

import java.util.Currency;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

import com.vaadin.data.BeanValidationBinder;
import com.vaadin.flow.model.TemplateModel;
import com.vaadin.router.PageTitle;
import com.vaadin.router.Route;
import com.vaadin.starter.bakery.backend.data.Role;
import com.vaadin.starter.bakery.backend.data.entity.Product;
import com.vaadin.starter.bakery.backend.service.ProductService;
import com.vaadin.starter.bakery.ui.BakeryApp;
import com.vaadin.starter.bakery.ui.components.BakerySearch;
import com.vaadin.starter.bakery.ui.components.ButtonsBar;
import com.vaadin.starter.bakery.ui.components.FormDialog;
import com.vaadin.starter.bakery.ui.utils.BakeryConst;
import com.vaadin.starter.bakery.ui.utils.converters.CurrencyFormatter;
import com.vaadin.starter.bakery.ui.view.admin.DefaultEntityPresenter;
import com.vaadin.starter.bakery.ui.view.admin.EntityEditor;
import com.vaadin.starter.bakery.ui.view.crud.CrudView;
import com.vaadin.ui.Tag;
import com.vaadin.ui.common.HasText;
import com.vaadin.ui.common.HtmlImport;
import com.vaadin.ui.grid.Grid;
import com.vaadin.ui.html.H3;
import com.vaadin.ui.html.Span;
import com.vaadin.ui.polymertemplate.Id;
import com.vaadin.ui.textfield.TextField;

@Tag("bakery-products")
@HtmlImport("src/products/bakery-products.html")
@Route(value = PAGE_PRODUCTS, layout = BakeryApp.class)
@PageTitle(BakeryConst.TITLE_PRODUCTS)
@Secured(Role.ADMIN)
public class ProductsView extends CrudView<Product, TemplateModel>  {

	@Id("search-bar")
	private BakerySearch searchbar;

	@Id("products-grid")
	private Grid<Product> grid;

	@Id("dialog-editor")
	private FormDialog dialog;

	@Id("buttons")
	private ButtonsBar buttons;

	private DefaultEntityPresenter<Product> presenter;

	private final BeanValidationBinder<Product> binder = new BeanValidationBinder<>(Product.class);

	@Id("title")
	private H3 title;

	@Id("product-edit-name")
	private TextField nameField;

	@Id("price")
	private TextField priceField;

	private CurrencyFormatter currencyFormatter = new CurrencyFormatter();

	@Autowired
	public ProductsView(ProductService service) {
		presenter = new DefaultEntityPresenter<>(service, this, getEntityName());
		setupEventListeners();
		setupGrid();

		binder.bind(nameField, "name");
		binder.forField(priceField).withConverter(new PriceConverter()).bind("price");
		priceField.addToPrefix(new Span(Currency.getInstance(Locale.getDefault()).getSymbol()));
	}

	private void setupGrid() {
		final Grid.Column<Product> productNameColumn = grid.addColumn(Product::getName).setHeader("Product Name").setFlexGrow(10);
		grid.addColumn(p -> currencyFormatter.toPresentation(p.getPrice())).setHeader("Unit Price");

		grid.getElement().addEventListener("animationend", e -> productNameColumn.setFlexGrow(10));
	}

	@Override
	public Grid<Product> getGrid() {
		return grid;
	}

	@Override
	protected DefaultEntityPresenter<Product> getPresenter() {
		return presenter;
	}

	@Override
	protected String getBasePage() {
		return PAGE_PRODUCTS;
	}

	@Override
	protected EntityEditor<Product> getEditor() {
		return this;
	}

	@Override
	protected BeanValidationBinder<Product> getBinder() {
		return binder;
	}

	@Override
	protected ButtonsBar getButtons() {
		return buttons;
	}

	@Override
	protected FormDialog getDialog() {
		return dialog;
	}

	@Override
	protected BakerySearch getSearchBar() {
		return searchbar;
	}

	@Override
	protected HasText getTitle() {
		return title;
	}

	@Override
	protected String getEntityName() {
		return "Product";
	}
}
