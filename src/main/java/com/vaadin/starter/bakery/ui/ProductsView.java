package com.vaadin.starter.bakery.ui;

import static com.vaadin.starter.bakery.ui.utils.BakeryConst.PAGE_PRODUCTS;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

import com.vaadin.data.ValidationException;
import com.vaadin.flow.model.Convert;
import com.vaadin.flow.model.Include;
import com.vaadin.flow.model.TemplateModel;
import com.vaadin.flow.router.LocationChangeEvent;
import com.vaadin.hummingbird.ext.spring.annotations.ParentView;
import com.vaadin.hummingbird.ext.spring.annotations.Route;
import com.vaadin.router.Title;
import com.vaadin.shared.Registration;
import com.vaadin.starter.bakery.app.HasLogger;
import com.vaadin.starter.bakery.backend.data.Role;
import com.vaadin.starter.bakery.backend.data.entity.Product;
import com.vaadin.starter.bakery.backend.service.ProductService;
import com.vaadin.starter.bakery.ui.components.ConfirmationDialog;
import com.vaadin.starter.bakery.ui.components.ItemsView;
import com.vaadin.starter.bakery.ui.components.ProductEdit;
import com.vaadin.starter.bakery.ui.converters.CurrencyFormatter;
import com.vaadin.starter.bakery.ui.converters.LongToStringConverter;
import com.vaadin.starter.bakery.ui.event.CancelEvent;
import com.vaadin.starter.bakery.ui.event.CloseDialogEvent;
import com.vaadin.starter.bakery.ui.event.DeleteEvent;
import com.vaadin.starter.bakery.ui.event.EditEvent;
import com.vaadin.starter.bakery.ui.event.SaveEvent;
import com.vaadin.starter.bakery.ui.presenter.Confirmer;
import com.vaadin.starter.bakery.ui.presenter.DefaultEntityPresenter;
import com.vaadin.starter.bakery.ui.presenter.ListableEntityView;
import com.vaadin.starter.bakery.ui.utils.BakeryConst;
import com.vaadin.starter.bakery.ui.view.DefaultEntityView;
import com.vaadin.ui.Tag;
import com.vaadin.ui.common.HtmlImport;
import com.vaadin.ui.event.ComponentEventListener;
import com.vaadin.ui.polymertemplate.Id;
import com.vaadin.ui.polymertemplate.PolymerTemplate;

@Tag("bakery-products")
@HtmlImport("context://src/products/bakery-products.html")
@Route(PAGE_PRODUCTS + "/{id}")
@ParentView(BakeryApp.class)
@Title(BakeryConst.TITLE_PRODUCTS)
@Secured(Role.ADMIN)
public class ProductsView extends DefaultEntityView<Product, ProductsView.Model> {

	public interface Model extends TemplateModel {

		@Include({ "id", "name", "price" })
		@Convert(value = LongToStringConverter.class, path = "id")
		@Convert(value = CurrencyFormatter.class, path = "price")
		void setProducts(List<Product> products);
	}


	@Id("bakery-products-items-view")
	private ItemsView view;

	@Id("product-editor")
	private ProductEdit editor;

	@Id("product-confirmation-dialog")
	private ConfirmationDialog confirmationDialog;

	private DefaultEntityPresenter<Product> presenter;

	@Autowired
	public ProductsView(ProductService service) {
		presenter = new DefaultEntityPresenter<Product>(service, this, "Product");
		setup(PAGE_PRODUCTS, presenter, editor, confirmationDialog, view, "New product");
		presenter.init();
	}

	@Override
	public void list(List<Product> entities) {
		getModel().setProducts(entities);
	}

}
