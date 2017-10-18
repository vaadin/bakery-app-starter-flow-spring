package com.vaadin.starter.bakery.ui;

import com.vaadin.flow.model.Convert;
import com.vaadin.flow.model.Include;
import com.vaadin.flow.model.TemplateModel;
import com.vaadin.flow.router.LocationChangeEvent;
import com.vaadin.flow.router.View;
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
import com.vaadin.starter.bakery.ui.event.DecisionEvent;
import com.vaadin.starter.bakery.ui.event.EditEvent;
import com.vaadin.starter.bakery.ui.utils.BakeryConst;
import com.vaadin.ui.Tag;
import com.vaadin.ui.button.Button;
import com.vaadin.ui.common.HasClickListeners;
import com.vaadin.ui.common.HtmlImport;
import com.vaadin.ui.polymertemplate.EventHandler;
import com.vaadin.ui.polymertemplate.Id;
import com.vaadin.ui.polymertemplate.PolymerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.annotation.Secured;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;

import static com.vaadin.starter.bakery.ui.utils.BakeryConst.CONFIRM_CANCELBUTTON_CANCEL;
import static com.vaadin.starter.bakery.ui.utils.BakeryConst.CONFIRM_CANCELBUTTON_DELETE;
import static com.vaadin.starter.bakery.ui.utils.BakeryConst.CONFIRM_CAPTION_CANCEL;
import static com.vaadin.starter.bakery.ui.utils.BakeryConst.CONFIRM_CAPTION_DELETE;
import static com.vaadin.starter.bakery.ui.utils.BakeryConst.CONFIRM_MESSAGE_CANCEL_PRODUCT;
import static com.vaadin.starter.bakery.ui.utils.BakeryConst.CONFIRM_MESSAGE_DELETE;
import static com.vaadin.starter.bakery.ui.utils.BakeryConst.CONFIRM_OKBUTTON_CANCEL;
import static com.vaadin.starter.bakery.ui.utils.BakeryConst.CONFIRM_OKBUTTON_DELETE;
import static com.vaadin.starter.bakery.ui.utils.BakeryConst.PAGE_PRODUCTS;

@Tag("bakery-products")
@HtmlImport("context://src/products/bakery-products.html")
@Route(PAGE_PRODUCTS + "/{id}")
@ParentView(BakeryApp.class)
@Title(BakeryConst.TITLE_PRODUCTS)
@Secured(Role.ADMIN)
public class ProductsView extends PolymerTemplate<ProductsView.Model> implements View, HasToast, HasLogger {

	public interface Model extends TemplateModel {

		@Include({ "id", "name", "price" })
		@Convert(value = LongToStringConverter.class, path = "id")
		@Convert(value = CurrencyFormatter.class, path = "price")
		void setProducts(List<Product> products);
	}

	private final ProductService service;

	@Id("bakery-products-items-view")
	private ItemsView view;

	@Id("product-editor")
	private ProductEdit editor;

	@Id("product-confirmation-dialog")
	private ConfirmationDialog confirmationDialog;

	@Autowired
	public ProductsView(ProductService service) {
		this.service = service;
		initEditor();
		addListener(EditEvent.class, e -> navigateToProduct(e.getId()));
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

	private void onBeforeDelete(HasClickListeners.ClickEvent<Button> deleteEvent) {
		confirmationDialog.show(CONFIRM_CAPTION_DELETE, CONFIRM_MESSAGE_DELETE, CONFIRM_OKBUTTON_DELETE,
				CONFIRM_CANCELBUTTON_DELETE);
		RegistrationHolder registrationHolder = new RegistrationHolder();
		registrationHolder.registration = confirmationDialog.addListener(DecisionEvent.class,
				e -> {
					registrationHolder.registration.remove();
					e.ifConfirmed(() -> deleteProduct(editor.getProductId()));
				});
	}

	@EventHandler
	private void onBeforeClose() {
		if (editor.isDirty()) {
			confirmationDialog.show(CONFIRM_CAPTION_CANCEL, CONFIRM_MESSAGE_CANCEL_PRODUCT, CONFIRM_OKBUTTON_CANCEL,
					CONFIRM_CANCELBUTTON_CANCEL);
			RegistrationHolder registrationHolder = new RegistrationHolder();
			registrationHolder.registration = confirmationDialog.addListener(DecisionEvent.class, e -> {
				registrationHolder.registration.remove();
				e.ifConfirmed(() -> navigateToProduct(null));
			});
		} else {
			navigateToProduct(null);
		}
	}

	class RegistrationHolder {
		private Registration registration;
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

	public void createNewProduct(HasClickListeners.ClickEvent<Button> newProductEvent) {
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
