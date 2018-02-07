package com.vaadin.starter.bakery.ui.views.admin.products;

import static com.vaadin.starter.bakery.ui.utils.BakeryConst.PAGE_PRODUCTS;

import java.util.Currency;

import com.vaadin.starter.bakery.ui.utils.TemplateUtil;
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
import com.vaadin.starter.bakery.ui.MainView;
import com.vaadin.starter.bakery.ui.components.FormButtonsBar;
import com.vaadin.starter.bakery.ui.components.FormDialog;
import com.vaadin.starter.bakery.ui.components.SearchBar;
import com.vaadin.starter.bakery.ui.crud.CrudView;
import com.vaadin.starter.bakery.ui.crud.DefaultEntityPresenter;
import com.vaadin.starter.bakery.ui.utils.BakeryConst;
import com.vaadin.starter.bakery.ui.utils.converters.CurrencyFormatter;

@Tag("products-view")
@HtmlImport("src/views/admin/products/products-view.html")
@Route(value = PAGE_PRODUCTS, layout = MainView.class)
@PageTitle(BakeryConst.TITLE_PRODUCTS)
@Secured(Role.ADMIN)
public class ProductsView extends CrudView<Product, TemplateModel>  {

	@Id("searchBar")
	private SearchBar searchbar;

	@Id("grid")
	private Grid<Product> grid;

	@Id("dialog")
	private FormDialog dialog;

	@Id("buttons")
	private FormButtonsBar buttons;

	private DefaultEntityPresenter<Product> presenter;

	private final BeanValidationBinder<Product> binder = new BeanValidationBinder<>(Product.class);

	@Id("title")
	private H3 title;

	@Id("name")
	private TextField name;

	@Id("price")
	private TextField price;

	private CurrencyFormatter currencyFormatter = new CurrencyFormatter();

	@Autowired
	public ProductsView(DefaultEntityPresenter<Product> presenter) {
		super(EntityUtil.getName(Product.class));
		this.presenter = presenter;
		setupEventListeners();
		setupGrid();

		binder.bind(name, "name");
		binder.forField(price).withConverter(new PriceConverter()).bind("price");
		TemplateUtil.addToSlot(price, new Span(Currency.getInstance(BakeryConst.APP_LOCALE).getSymbol()), "prefix");

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
	protected SearchBar getSearchBar() {
		return searchbar;
	}

	@Override
	protected HasText getTitle() {
		return title;
	}
}
