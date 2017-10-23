package com.vaadin.starter.bakery.ui;

import static com.vaadin.starter.bakery.ui.utils.StorefrontItemHeaderGenerator.computeEntriesWithHeader;
import static com.vaadin.starter.bakery.ui.utils.TemplateUtil.addToSlot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vaadin.router.HasUrlParameter;
import com.vaadin.router.OptionalParameter;
import com.vaadin.router.QueryParameters;
import com.vaadin.router.event.BeforeNavigationEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;

import com.vaadin.data.ValidationException;
import com.vaadin.flow.model.TemplateModel;
import com.vaadin.router.Route;
import com.vaadin.router.PageTitle;
import com.vaadin.shared.Registration;
import com.vaadin.starter.bakery.app.HasLogger;
import com.vaadin.starter.bakery.backend.service.OrderService;
import com.vaadin.starter.bakery.backend.service.ProductService;
import com.vaadin.starter.bakery.backend.service.UserService;
import com.vaadin.starter.bakery.ui.components.ConfirmationDialog;
import com.vaadin.starter.bakery.ui.components.storefront.OrderDetail;
import com.vaadin.starter.bakery.ui.components.storefront.OrderEdit;
import com.vaadin.starter.bakery.ui.components.viewselector.ViewSelector;
import com.vaadin.starter.bakery.ui.dataproviders.OrdersDataProvider;
import com.vaadin.starter.bakery.ui.entities.Order;
import com.vaadin.starter.bakery.ui.event.CancelEvent;
import com.vaadin.starter.bakery.ui.event.DeleteEvent;
import com.vaadin.starter.bakery.ui.event.SaveEvent;
import com.vaadin.starter.bakery.ui.presenter.Confirmer;
import com.vaadin.starter.bakery.ui.presenter.EntityView;
import com.vaadin.starter.bakery.ui.presenter.EntityViewPresenter;
import com.vaadin.starter.bakery.ui.utils.BakeryConst;
import com.vaadin.ui.Component;
import com.vaadin.ui.Tag;
import com.vaadin.ui.common.ClientDelegate;
import com.vaadin.ui.common.HtmlImport;
import com.vaadin.ui.event.ComponentEventListener;
import com.vaadin.ui.polymertemplate.Id;
import com.vaadin.ui.polymertemplate.PolymerTemplate;

@Tag("bakery-storefront")
@HtmlImport("context://src/storefront/bakery-storefront.html")
@Route(value = BakeryConst.PAGE_STOREFRONT, layout = BakeryApp.class)
@PageTitle(BakeryConst.TITLE_STOREFRONT)
public class StorefrontView extends PolymerTemplate<StorefrontView.Model> implements HasLogger,
		HasUrlParameter<Long>, EntityView<com.vaadin.starter.bakery.backend.data.entity.Order> {

	public interface Model extends TemplateModel {
		void setOrders(List<Order> orders);

		List<Order> getOrders();

		void setEditing(boolean editing);
	}

	@Id("search")
	private BakerySearch searchBar;

	@Id("confirmation-dialog")
	private ConfirmationDialog confirmationDialog;

	private final ViewSelector viewSelector = new ViewSelector();
	private final OrderEdit orderEdit = new OrderEdit();
	private final OrderDetail orderDetail = new OrderDetail();

	private Presenter presenter;

	private OrdersDataProvider ordersProvider;
	private OrderService orderService;
	private ProductService productService;
	private UserService userService;

	@Autowired
	public StorefrontView(OrdersDataProvider ordersProvider, ProductService productService, OrderService orderService,
			UserService userService) {
		this.productService = productService;
		this.ordersProvider = ordersProvider;
		this.orderService = orderService;
		this.userService = userService;
		addToSlot(this, viewSelector, "view-selector-slot");
		this.presenter = new Presenter();

		searchBar.setActionText("New order");
		searchBar.setCheckboxText("Show past orders");
		searchBar.setPlaceHolder("Search");
		searchBar.addFilterChangeListener(this::filterItems);
		searchBar.addActionClickListener(e -> edit(null));
		orderDetail.addListener(SaveEvent.class, e -> presenter.save());
		orderEdit.addListener(CancelEvent.class, e -> presenter.cancel());
		confirmationDialog.addDecisionListener(presenter::confirmationDecisionReceived);
		filterItems(searchBar.getFilter(), searchBar.getShowPrevious());

		getModel().setEditing(false);
	}

	private void filterItems(String filter, boolean showPrevious) {
		// the hardcoded limit of 200 is here until lazy loading is implemented (see
		// BFF-120)
		PageRequest pr = new PageRequest(0, 200, Direction.ASC, "dueDate", "dueTime", "id");
		setOrders(ordersProvider.getOrdersList(filter, showPrevious, pr).getOrders(), showPrevious);
	}

	@ClientDelegate
	private void addComment(String orderId, String message) {
		presenter.addComment(Long.parseLong(orderId), message);
	}

	@ClientDelegate
	private void edit(String id) {
		if (id != null && !id.isEmpty()) {
			Map<String, String> parameters = new HashMap<>();
			parameters.put("edit", "");
			getUI().ifPresent(ui -> ui.navigateTo(BakeryConst.PAGE_STOREFRONT + "/" + id,
					QueryParameters.simple(parameters)));
			return;
		}
		presenter.createNew();
	}

	@Override
	public void setParameter(BeforeNavigationEvent event, @OptionalParameter Long orderId) {
		if (orderId != null) {
			boolean editView = event.getLocation().getQueryParameters().getParameters().containsKey("edit");
			presenter.loadEntity(orderId, editView);
		}
	}

	private void updateOrderInModel(com.vaadin.starter.bakery.backend.data.entity.Order dataOrder) {
		String orderId = dataOrder.getId().toString();
		getModel().getOrders().stream().filter(o -> o.getId().equals(orderId)).findFirst()
		.ifPresent(o -> ordersProvider.fillOrder(o, dataOrder));
	}

	private void setOrders(List<Order> orders, boolean showPrevious) {
		getModel().setOrders(orders);
		getElement().setPropertyJson("displayedHeaders", computeEntriesWithHeader(orders, showPrevious));
	}

	private void selectComponent(Component component) {
		// This is a workaround for a Safari 11 issue.
		// If the orderEdit is injected in the ViewSelector
		// on the constructor,
		// Safari fails to set the styles correctly.
		if (component.getElement().getParent() == null) {
			viewSelector.add(component);
		}
		viewSelector.select(component);
	}

	private void showOrderEdit() {
		selectComponent(orderEdit);
	}

	private void details(com.vaadin.starter.bakery.backend.data.entity.Order order, boolean isReview) {
		selectComponent(orderDetail);
		orderDetail.display(order, isReview);
	}

	@Override
	public boolean isDirty() {
		return orderEdit.hasChanges();
	}

	@Override
	public void write(com.vaadin.starter.bakery.backend.data.entity.Order entity) throws ValidationException {
		orderEdit.write(entity);
	}

	@Override
	public void closeDialog(boolean updated) {
		orderEdit.close();
		getModel().setEditing(false);
		getUI().ifPresent(ui -> ui.navigateTo(BakeryConst.PAGE_STOREFRONT));
	}

	@Override
	public void openDialog(com.vaadin.starter.bakery.backend.data.entity.Order order, boolean edit) {
		getModel().setEditing(true);
		if (edit) {
			orderEdit.read(order);
			showOrderEdit();
		} else {
			details(order, false);
		}
	}

	@Override
	public void update(com.vaadin.starter.bakery.backend.data.entity.Order order) {
		filterItems(searchBar.getFilter(), searchBar.getShowPrevious());
	}

	@Override
	public Confirmer getConfirmer() {
		return confirmationDialog;
	}

	class Presenter extends EntityViewPresenter<com.vaadin.starter.bakery.backend.data.entity.Order> {

		public Presenter() {
			super(orderService, StorefrontView.this, "Order");
			orderEdit.addReviewListener(e -> {
				try {
					writeEntity();
					details(getEntity(), true);
				} catch (ValidationException ex) {
					showValidationError();
				}
			});
			orderDetail.addBackListener(e -> showOrderEdit());
			orderDetail.addEditListener(e -> {
				orderEdit.read(getEntity());
				showOrderEdit();
			});
			orderDetail.addCommentListener(e -> {
				if (e.getOrderId() == null) {
					return;
				}

				addComment(e.getOrderId(), e.getMessage());
				details(orderService.findOrder(e.getOrderId()), false);
			});
		}

		@Override
		protected void beforeSave() throws ValidationException {
			// Entity already updated
		}

		@Override
		protected void openDialog(com.vaadin.starter.bakery.backend.data.entity.Order entity, boolean edit) {
			orderEdit.init(userService.getCurrentUser(), productService.getRepository().findAll());
			super.openDialog(entity, edit);
		}

		public void addComment(Long id, String comment) {
			if (executeJPAOperation(() -> setEntity(orderService.addComment(id, comment)))) {
				updateOrderInModel(getEntity());
			}
		}

	}

}
