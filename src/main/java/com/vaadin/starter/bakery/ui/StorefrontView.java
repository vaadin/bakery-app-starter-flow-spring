package com.vaadin.starter.bakery.ui;

import static com.vaadin.starter.bakery.ui.utils.StorefrontItemHeaderGenerator.computeEntriesWithHeader;
import static com.vaadin.starter.bakery.ui.utils.TemplateUtil.addToSlot;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;

import com.vaadin.data.ValidationException;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.model.TemplateModel;
import com.vaadin.flow.router.LocationChangeEvent;
import com.vaadin.flow.router.View;
import com.vaadin.hummingbird.ext.spring.annotations.ParentView;
import com.vaadin.hummingbird.ext.spring.annotations.Route;
import com.vaadin.router.Title;
import com.vaadin.shared.Registration;
import com.vaadin.starter.bakery.app.HasLogger;
import com.vaadin.starter.bakery.backend.data.entity.User;
import com.vaadin.starter.bakery.backend.service.OrderService;
import com.vaadin.starter.bakery.backend.service.ProductService;
import com.vaadin.starter.bakery.backend.service.UserService;
import com.vaadin.starter.bakery.ui.components.ConfirmationDialog;
import com.vaadin.starter.bakery.ui.components.Confirmer;
import com.vaadin.starter.bakery.ui.components.EntityEditView;
import com.vaadin.starter.bakery.ui.components.EntityView;
import com.vaadin.starter.bakery.ui.components.storefront.OrderDetail;
import com.vaadin.starter.bakery.ui.components.storefront.OrderEdit;
import com.vaadin.starter.bakery.ui.components.viewselector.ViewSelector;
import com.vaadin.starter.bakery.ui.dataproviders.OrdersDataProvider;
import com.vaadin.starter.bakery.ui.entities.Order;
import com.vaadin.starter.bakery.ui.event.CancelEvent;
import com.vaadin.starter.bakery.ui.event.DeleteEvent;
import com.vaadin.starter.bakery.ui.event.SaveEvent;
import com.vaadin.starter.bakery.ui.messages.Message;
import com.vaadin.starter.bakery.ui.presenter.EntityEditPresenter;
import com.vaadin.starter.bakery.ui.utils.BakeryConst;
import com.vaadin.starter.bakery.ui.utils.TemplateUtil;
import com.vaadin.ui.Component;
import com.vaadin.ui.Tag;
import com.vaadin.ui.common.ClientDelegate;
import com.vaadin.ui.common.HtmlImport;
import com.vaadin.ui.event.ComponentEvent;
import com.vaadin.ui.event.ComponentEventListener;
import com.vaadin.ui.polymertemplate.Id;
import com.vaadin.ui.polymertemplate.PolymerTemplate;

@Tag("bakery-storefront")
@HtmlImport("context://src/storefront/bakery-storefront.html")
@Route(BakeryConst.PAGE_STOREFRONT + "/{id}")
@Route(BakeryConst.PAGE_STOREFRONT + "/{id}/edit")
@Route(value = "")
@ParentView(BakeryApp.class)
@Title(BakeryConst.TITLE_STOREFRONT)
public class StorefrontView extends PolymerTemplate<StorefrontView.Model> implements View, HasLogger, HasToast {

	public interface Model extends TemplateModel {
		void setOrders(List<Order> orders);

		List<Order> getOrders();

		void setEditing(boolean editing);
	}

	@Id("search")
	private BakerySearch searchBar;

	@Id("confirmation-dialog")
	private ConfirmationDialog confirmationDialog;

	private ViewSelector viewSelector = new ViewSelector();

	private EntityEditPresenter<com.vaadin.starter.bakery.backend.data.entity.Order> presenter;

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
		this.presenter = new Presenter(new SelectionControl());

		searchBar.setActionText("New order");
		searchBar.setCheckboxText("Show past orders");
		searchBar.setPlaceHolder("Search");
		searchBar.addFilterChangeListener(this::filterItems);
		searchBar.addActionClickListener(e -> edit(null));

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
		try {
			ordersProvider.addOrderComment(orderId, message);
		} catch (Exception e) {
			toast(e.getMessage(), true);
		} finally {
			updateOrderInModel(orderId);
		}
	}

	@ClientDelegate
	private void edit(String id) {
		if (id != null && !id.isEmpty()) {
			getUI().ifPresent(ui -> ui.navigateTo(BakeryConst.PAGE_STOREFRONT + "/" + id + "/edit"));
			return;
		}
		com.vaadin.starter.bakery.backend.data.entity.Order order;
		User currentUser = userService.getCurrentUser();
		order = new com.vaadin.starter.bakery.backend.data.entity.Order(currentUser);
		order.setDueTime(LocalTime.of(16, 0));
		order.setDueDate(LocalDate.now());
		presenter.createNew(order);
	}

	@Override
	public void onLocationChange(LocationChangeEvent locationChangeEvent) {
		TemplateUtil.handleEntityNavigation(presenter, locationChangeEvent,
				locationChangeEvent.getLocation().getSegments().contains("edit"));
	}

	private void updateOrderInModel(String orderId) {
		int idx = findOrderIndexInModel(orderId);
		if (idx == -1) {
			return;
		}

		try {
			getModel().getOrders().set(idx, ordersProvider.getOrder(orderId));
		} catch (Exception e) {
			// exclude the order from the model if ordersProvider.getOrder() throws
			setOrders(getModel().getOrders().stream().filter(order -> !order.getId().equals(orderId))
					.collect(Collectors.toList()), false);
		}
	}

	private int findOrderIndexInModel(String orderId) {
		List<Order> orders = getModel().getOrders();
		for (int i = 0; i < orders.size(); i += 1) {
			Order modelOrder = orders.get(i);
			if (modelOrder.getId().equals(orderId)) {
				return i;
			}
		}
		return -1;
	}

	private void setOrders(List<Order> orders, boolean showPrevious) {
		getModel().setOrders(orders);
		getElement().setPropertyJson("displayedHeaders", computeEntriesWithHeader(orders, showPrevious));
	}

	class Presenter extends EntityEditPresenter<com.vaadin.starter.bakery.backend.data.entity.Order> {

		private SelectionControl selectionControl;

		public Presenter(SelectionControl selectionControl) {
			super(orderService, selectionControl, selectionControl, "Order");
			this.selectionControl = selectionControl;
			selectionControl.orderEdit.addReviewListener(e -> {
				try {
					writeEntity();
					selectionControl.details(getEntity(), true);
				} catch (ValidationException ex) {
					showValidationError();
				}
			});
			selectionControl.orderDetail.addBackListener(e -> selectionControl.showOrderEdit());
			selectionControl.orderDetail.addEditListener(e -> {
				selectionControl.orderEdit.read(getEntity());
				selectionControl.showOrderEdit();
			});
			selectionControl.orderDetail.addCommentListener(e -> {
				if (e.getOrderId() == null) {
					return;
				}

				addComment(e.getOrderId().toString(), e.getMessage());
				selectionControl.details(orderService.findOrder(e.getOrderId()), false);
			});
		}

		@Override
		protected void beforeSave() throws ValidationException {
			// Entity already updated
		}

		@Override
		protected void openDialog(com.vaadin.starter.bakery.backend.data.entity.Order entity, boolean edit) {
			selectionControl.orderEdit.init(userService.getCurrentUser(), productService.getRepository().findAll());
			super.openDialog(entity, edit);
		}

	}

	class SelectionControl implements EntityEditView<com.vaadin.starter.bakery.backend.data.entity.Order>,
	EntityView<com.vaadin.starter.bakery.backend.data.entity.Order> {

		private final OrderEdit orderEdit = new OrderEdit();
		private final OrderDetail orderDetail = new OrderDetail();

		public SelectionControl() {
			orderEdit.addCancelListener(StorefrontView.this::fireEvent);
			orderDetail.addListener(SaveEvent.class, StorefrontView.this::fireEvent);
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
		public <E extends ComponentEvent<?>> Registration addListener(Class<E> eventType,
				ComponentEventListener<E> listener) {
			return StorefrontView.this.addListener(eventType, listener);
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
		public Element getElement() {
			return StorefrontView.this.getElement();
		}

		@Override
		public Confirmer getConfirmer() {
			return confirmationDialog;
		}

	}
}
