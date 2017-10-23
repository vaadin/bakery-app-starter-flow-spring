package com.vaadin.starter.bakery.ui;

import com.vaadin.flow.model.Convert;
import com.vaadin.flow.model.Include;
import com.vaadin.flow.model.TemplateModel;
import com.vaadin.router.HasUrlParameter;
import com.vaadin.router.OptionalParameter;
import com.vaadin.router.PageTitle;
import com.vaadin.router.Route;
import com.vaadin.router.event.BeforeNavigationEvent;
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
@Route(value = PAGE_PRODUCTS, layout = BakeryApp.class)
@PageTitle(BakeryConst.TITLE_PRODUCTS)
@Secured(Role.ADMIN)
public class ProductsView extends PolymerTemplate<ProductsView.Model> implements HasUrlParameter<Long>, HasToast,
		HasLogger {

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
		editor.addCancelListener(cancelClickEvent -> onCloseDialog());
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
	private void onCloseDialog() {
		Runnable closeDialog = () -> {
			view.openDialog(false);
			navigateToProduct(null);
		};
		if (editor.isDirty()) {
			confirmationDialog.show(CONFIRM_CAPTION_CANCEL, CONFIRM_MESSAGE_CANCEL_PRODUCT, CONFIRM_OKBUTTON_CANCEL,
					CONFIRM_CANCELBUTTON_CANCEL);
			RegistrationHolder registrationHolder = new RegistrationHolder();
			registrationHolder.registration = confirmationDialog.addListener(DecisionEvent.class, e -> {
				registrationHolder.registration.remove();
				e.ifConfirmed(closeDialog);
			});
		} else {
			closeDialog.run();
		}
	}

	class RegistrationHolder {
		private Registration registration;
	}

	@Override
	public void setParameter(BeforeNavigationEvent event, @OptionalParameter Long productId) {
		if (productId != null) {
			Product product = service.getRepository().findOne(productId);
			if (product == null) {
				getLogger().error("Product with id " + productId + " was not found.");
				event.rerouteTo(NotFoundView.class);
			}

			view.openDialog(true);
			editor.setProduct(product);
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
			view.openDialog(false);
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
