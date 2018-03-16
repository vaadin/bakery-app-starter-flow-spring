package com.vaadin.starter.bakery.ui.views.admin.products;

import static com.vaadin.starter.bakery.ui.utils.BakeryConst.PAGE_PRODUCTS;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.templatemodel.TemplateModel;
import com.vaadin.starter.bakery.backend.data.Role;
import com.vaadin.starter.bakery.backend.data.entity.Product;
import com.vaadin.starter.bakery.backend.data.entity.util.EntityUtil;
import com.vaadin.starter.bakery.ui.MainView;
import com.vaadin.starter.bakery.ui.components.ConfirmDialog;
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

	@Id("search")
	private SearchBar search;

	@Id("grid")
	private Grid<Product> grid;

	@Id("dialog")
	private Dialog dialog;

	private ProductForm form = new ProductForm();

	private DefaultEntityPresenter<Product> presenter;

	private final BeanValidationBinder<Product> binder = new BeanValidationBinder<>(Product.class);

	private CurrencyFormatter currencyFormatter = new CurrencyFormatter();

	@Autowired
	public ProductsView(DefaultEntityPresenter<Product> presenter) {
		super(EntityUtil.getName(Product.class));
		this.presenter = presenter;
		form.setBinder(binder);
		dialog.add(form);
		setupEventListeners();
		setupGrid();
		presenter.init(this);
	}

	private void setupGrid() {
		grid.addColumn(Product::getName).setHeader("Product Name").setFlexGrow(10);
		grid.addColumn(p -> currencyFormatter.toPresentation(p.getPrice())).setHeader("Unit Price");
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
	protected Dialog getDialog() {
		return dialog;
	}

	@Override
	protected SearchBar getSearchBar() {
		return search;
	}

	@Override
	protected CrudForm<Product> getForm() {
		return form;
	}
}
