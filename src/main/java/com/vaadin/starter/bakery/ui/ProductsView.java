package com.vaadin.starter.bakery.ui;

import com.vaadin.annotations.Convert;
import com.vaadin.annotations.EventHandler;
import com.vaadin.annotations.HtmlImport;
import com.vaadin.annotations.Id;
import com.vaadin.annotations.Include;
import com.vaadin.annotations.Tag;
import com.vaadin.flow.router.LocationChangeEvent;
import com.vaadin.flow.router.View;
import com.vaadin.flow.template.PolymerTemplate;
import com.vaadin.flow.template.model.TemplateModel;
import com.vaadin.hummingbird.ext.spring.annotations.ParentView;
import com.vaadin.hummingbird.ext.spring.annotations.Route;
import com.vaadin.starter.bakery.app.HasLogger;
import com.vaadin.starter.bakery.backend.data.Role;
import com.vaadin.starter.bakery.backend.data.entity.Product;
import com.vaadin.starter.bakery.backend.service.ProductService;
import com.vaadin.starter.bakery.ui.components.ConfirmationDialog;
import com.vaadin.starter.bakery.ui.components.ItemsView;
import com.vaadin.starter.bakery.ui.components.ProductEdit;
import com.vaadin.starter.bakery.ui.converters.LongToStringConverter;
import com.vaadin.ui.Button;
import com.vaadin.ui.HasClickListeners.ClickEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.annotation.Secured;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;

import static com.vaadin.starter.bakery.ui.utils.BakeryConst.CONFIRM_CANCELBUTTON_CANCEL;
import static com.vaadin.starter.bakery.ui.utils.BakeryConst.CONFIRM_CANCELBUTTON_DELETE;
import static com.vaadin.starter.bakery.ui.utils.BakeryConst.CONFIRM_CAPTION_CANCEL;
import static com.vaadin.starter.bakery.ui.utils.BakeryConst.CONFIRM_CAPTION_DELETE_PRODUCT;
import static com.vaadin.starter.bakery.ui.utils.BakeryConst.CONFIRM_MESSAGE_CANCEL_PRODUCT;
import static com.vaadin.starter.bakery.ui.utils.BakeryConst.CONFIRM_MESSAGE_DELETE;
import static com.vaadin.starter.bakery.ui.utils.BakeryConst.CONFIRM_OKBUTTON_CANCEL;
import static com.vaadin.starter.bakery.ui.utils.BakeryConst.CONFIRM_OKBUTTON_DELETE;
import static com.vaadin.starter.bakery.ui.utils.BakeryConst.PAGE_PRODUCTS;

@Tag("bakery-products")
@HtmlImport("context://src/products/bakery-products.html")
@Route(PAGE_PRODUCTS + "/{id}")
@ParentView(BakeryApp.class)
@Secured(Role.ADMIN)
public class ProductsView extends PolymerTemplate<ProductsView.Model> implements View, HasToast, HasLogger {

	public interface Model extends TemplateModel {

		@Include({ "id", "name", "price" })
		@Convert(value = LongToStringConverter.class, path = "id")
		void setProducts(List<Product> products);
	}

	private final ProductService service;

	@Id("view")
	private ItemsView view;

	@Id("product-editor")
	private ProductEdit editor;

	@Id("product-confirmation-dialog")
	private ConfirmationDialog confirmationDialog;

	@Autowired
	public ProductsView(ProductService service) {
		this.service = service;
		initEditor();
		getElement().addEventListener("edit", e -> navigateToProduct(e.getEventData().getString("event.detail")),
				"event.detail");

		filterProducts(view.getFilter());

		view.setActionText("New product");
		view.addActionClickListener(this::createNewProduct);
		view.addFilterChangeListener(this::filterProducts);
	}

	private void initEditor() {
		editor.addDeleteListener(this::onBeforeDelete);
		editor.addCancelListener(cancelClickEvent -> onBeforeClose());
		editor.addSaveListener(saveClickEvent -> saveProduct(editor.getProduct()));
	}

	private void onBeforeDelete(ClickEvent<Button> deleteEvent) {
		confirmationDialog.show(CONFIRM_CAPTION_DELETE_PRODUCT, CONFIRM_MESSAGE_DELETE, CONFIRM_OKBUTTON_DELETE,
				CONFIRM_CANCELBUTTON_DELETE, okButtonEvent -> deleteProduct(editor.getProductId()), null);
	}

	@EventHandler
	private void onBeforeClose() {
		if (editor.isDirty()) {
			confirmationDialog.show(CONFIRM_CAPTION_CANCEL, CONFIRM_MESSAGE_CANCEL_PRODUCT, CONFIRM_OKBUTTON_CANCEL,
					CONFIRM_CANCELBUTTON_CANCEL, okButtonEvent -> navigateToProduct(null), null);
		} else {
			navigateToProduct(null);
		}
	}

	@Override
	public void onLocationChange(LocationChangeEvent locationChangeEvent) {
		setEditableProduct(locationChangeEvent.getPathParameter("id"));
	}

	private void setEditableProduct(String id) {
		if (id == null || id.isEmpty()) {
			view.openDialog(false);
			return;
		}

		try {
			Long longId = Long.parseLong(id);
			Product product = service.getRepository().findOne(longId);
			if (product == null) {
				String errorMessage = "Product with id " + id + " was not found.";
				toast(errorMessage, false);
				getLogger().error(errorMessage);
				return;
			}

			view.openDialog(true);
			editor.setProduct(product);
		} catch (NumberFormatException e) {
			toast("Wrong product id: " + id, false);
			getLogger().error("Failed to parse id: " + id);
			view.openDialog(false);
		}
	}

	private void navigateToProduct(String id) {
		final String location = PAGE_PRODUCTS + (id == null || id.isEmpty() ? "" : "/" + id);
		getUI().ifPresent(ui -> ui.navigateTo(location));
	}

	private void filterProducts(String filter) {
		getModel().setProducts(service.findAnyMatching(Optional.ofNullable(filter), null).getContent());
	}

	public void createNewProduct(ClickEvent<Button> newProductEvent) {
		view.openDialog(true);
		editor.setProduct(new Product());
	}

	private void saveProduct(Product product) {
		try {
			service.save(product);
			navigateToProduct(null);
		} catch (ConstraintViolationException e) {
			String errorMessage = getErrorMessage(e);
			toast(errorMessage, true);
			getLogger().error("Error on saving product: " + errorMessage);
		} catch (Exception e) {
			toast("Product could not be saved", true);
			getLogger().error("Error on saving product: " + e.getMessage());
		} finally {
			filterProducts(view.getFilter());
		}
	}

	private void deleteProduct(long id) {
		try {
			service.delete(id);
			navigateToProduct(null);
		} catch (Exception e) {
			String message = "Product could not be deleted";
			if (e instanceof DataIntegrityViolationException) {
				message += " because it is currently in use";
			}
			toast(message, true);
			getLogger().error("Error on deleting product: " + e.getMessage());
		} finally {
			filterProducts(view.getFilter());
		}
	}

	private String getErrorMessage(ConstraintViolationException e) {
		StringBuilder errorMessage = new StringBuilder();
		e.getConstraintViolations().forEach(msg -> errorMessage.append(msg.getMessage()).append(" "));
		return errorMessage.toString().trim();
	}
}
