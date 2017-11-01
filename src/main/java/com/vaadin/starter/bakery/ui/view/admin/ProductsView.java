package com.vaadin.starter.bakery.ui.view.admin;

import static com.vaadin.starter.bakery.ui.utils.BakeryConst.PAGE_PRODUCTS;
import static com.vaadin.starter.bakery.ui.utils.TemplateUtil.addToSlot;

import com.vaadin.starter.bakery.ui.BakeryApp;
import com.vaadin.ui.grid.Grid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

import com.vaadin.flow.model.TemplateModel;
import com.vaadin.router.PageTitle;
import com.vaadin.router.Route;
import com.vaadin.starter.bakery.backend.data.Role;
import com.vaadin.starter.bakery.backend.data.entity.Product;
import com.vaadin.starter.bakery.backend.service.ProductService;
import com.vaadin.starter.bakery.ui.components.ConfirmationDialog;
import com.vaadin.starter.bakery.ui.components.ItemsView;
import com.vaadin.starter.bakery.ui.components.ProductEdit;
import com.vaadin.starter.bakery.ui.utils.converters.CurrencyFormatter;
import com.vaadin.starter.bakery.ui.utils.BakeryConst;
import com.vaadin.ui.Tag;
import com.vaadin.ui.common.HtmlImport;
import com.vaadin.ui.polymertemplate.Id;

@Tag("bakery-products")
@HtmlImport("src/products/bakery-products.html")
@Route(value = PAGE_PRODUCTS, layout = BakeryApp.class)
@PageTitle(BakeryConst.TITLE_PRODUCTS)
@Secured(Role.ADMIN)
public class ProductsView extends PolymerEntityView<Product, TemplateModel> {

	@Id("bakery-products-items-view")
	private ItemsView view;

	private ProductEdit editor;

	private Grid<Product> grid = new Grid<>();

	@Override
	public Grid<Product> getGrid() {
		return grid;
	}

	@Id("product-confirmation-dialog")
	private ConfirmationDialog confirmationDialog;

	private DefaultEntityPresenter<Product> presenter;

	private CurrencyFormatter currencyFormatter = new CurrencyFormatter();

	@Autowired
	public ProductsView(ProductService service) {
		editor = new ProductEdit();
		addToSlot(this, editor, "product-editor");
		presenter = new DefaultEntityPresenter<>(service, this, "Product");
		setupEventListeners();
		view.setActionText("New product");
		setupGrid();
	}

	private void setupGrid() {
		grid.setId("grid");
		grid.getElement().setAttribute("theme", "borderless");

		grid.addColumn("Product Name", Product::getName).setFlexGrow(10);
		grid.addColumn("Unit Price", p -> currencyFormatter.toPresentation(p.getPrice()));

		addToSlot(this, grid, "items-grid");
	}

	@Override
	public ConfirmationDialog getConfirmationDialog() {
		return confirmationDialog;
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
		return editor;
	}

	@Override
	protected ItemsView getItemsView() {
		return view;
	}
}
