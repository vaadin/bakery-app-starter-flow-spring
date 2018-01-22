package com.vaadin.starter.bakery.ui.view.admin.products;

import static com.vaadin.starter.bakery.ui.utils.BakeryConst.PAGE_PRODUCTS;

import java.util.Currency;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

import com.vaadin.flow.component.HasText;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.templatemodel.TemplateModel;
import com.vaadin.starter.bakery.backend.data.Role;
import com.vaadin.starter.bakery.backend.data.entity.Product;
import com.vaadin.starter.bakery.backend.data.entity.util.EntityUtil;
import com.vaadin.starter.bakery.ui.BakeryApp;
import com.vaadin.starter.bakery.ui.components.BakerySearch;
import com.vaadin.starter.bakery.ui.components.FormButtonsBar;
import com.vaadin.starter.bakery.ui.components.FormDialog;
import com.vaadin.starter.bakery.ui.crud.CrudView;
import com.vaadin.starter.bakery.ui.crud.DefaultEntityPresenter;
import com.vaadin.starter.bakery.ui.utils.BakeryConst;
import com.vaadin.starter.bakery.ui.utils.converters.CurrencyFormatter;

@Tag("products-view")
@HtmlImport("src/admin/products/products-view.html")
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
	private FormButtonsBar buttons;

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
	public ProductsView(DefaultEntityPresenter<Product> presenter) {
		super(EntityUtil.getName(Product.class));
		this.presenter = presenter;
		setupEventListeners();
		setupGrid();

		binder.bind(nameField, "name");
		binder.forField(priceField).withConverter(new PriceConverter()).bind("price");
		priceField.addToPrefix(new Span(Currency.getInstance(BakeryConst.APP_LOCALE).getSymbol()));
		
		presenter.init(this);
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
	protected BeanValidationBinder<Product> getBinder() {
		return binder;
	}

	@Override
	protected FormButtonsBar getButtons() {
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
}
