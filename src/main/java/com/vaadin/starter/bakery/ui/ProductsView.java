package com.vaadin.starter.bakery.ui;

import static com.vaadin.starter.bakery.ui.utils.BakeryConst.PAGE_PRODUCTS;
import static com.vaadin.starter.bakery.ui.utils.TemplateUtil.addToSlot;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

import com.vaadin.flow.model.Convert;
import com.vaadin.flow.model.Include;
import com.vaadin.flow.model.TemplateModel;
import com.vaadin.router.PageTitle;
import com.vaadin.router.Route;
import com.vaadin.starter.bakery.backend.data.Role;
import com.vaadin.starter.bakery.backend.data.entity.Product;
import com.vaadin.starter.bakery.backend.service.ProductService;
import com.vaadin.starter.bakery.ui.components.ConfirmationDialog;
import com.vaadin.starter.bakery.ui.components.ItemsView;
import com.vaadin.starter.bakery.ui.components.ProductEdit;
import com.vaadin.starter.bakery.ui.converters.CurrencyFormatter;
import com.vaadin.starter.bakery.ui.converters.LongToStringConverter;
import com.vaadin.starter.bakery.ui.presenter.DefaultEntityPresenter;
import com.vaadin.starter.bakery.ui.utils.BakeryConst;
import com.vaadin.starter.bakery.ui.view.EntityEditor;
import com.vaadin.starter.bakery.ui.view.PolymerEntityView;
import com.vaadin.ui.Tag;
import com.vaadin.ui.common.HtmlImport;
import com.vaadin.ui.polymertemplate.Id;

@Tag("bakery-products")
@HtmlImport("src/products/bakery-products.html")
@Route(value = PAGE_PRODUCTS, layout = BakeryApp.class)
@PageTitle(BakeryConst.TITLE_PRODUCTS)
@Secured(Role.ADMIN)
public class ProductsView extends PolymerEntityView<Product, ProductsView.Model> {

	public interface Model extends TemplateModel {

		@Include({ "id", "name", "price" })
		@Convert(value = LongToStringConverter.class, path = "id")
		@Convert(value = CurrencyFormatter.class, path = "price")
		void setProducts(List<Product> products);
	}


	@Id("bakery-products-items-view")
	private ItemsView view;

	private ProductEdit editor;

	@Id("product-confirmation-dialog")
	private ConfirmationDialog confirmationDialog;

	private DefaultEntityPresenter<Product> presenter;

	@Autowired
	public ProductsView(ProductService service) {
		editor = new ProductEdit();
		addToSlot(this, editor, "product-editor");
		presenter = new DefaultEntityPresenter<>(service, this, "Product");
		setupEventListeners();
		view.setActionText("New product");
	}

	@Override
	public void setItems(List<Product> entities) {
		getModel().setProducts(entities);
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
