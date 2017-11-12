package com.vaadin.starter.bakery.ui.view.storefront;

import static com.vaadin.starter.bakery.ui.utils.StorefrontItemHeaderGenerator.computeEntriesWithHeader;
import static com.vaadin.starter.bakery.ui.utils.TemplateUtil.addToSlot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vaadin.starter.bakery.ui.entities.StorefrontItemHeader;
import com.vaadin.ui.grid.Grid;
import com.vaadin.ui.renderers.ComponentRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;

import com.vaadin.data.ValidationException;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.flow.model.Convert;
import com.vaadin.flow.model.Include;
import com.vaadin.flow.model.TemplateModel;
import com.vaadin.router.HasUrlParameter;
import com.vaadin.router.OptionalParameter;
import com.vaadin.router.PageTitle;
import com.vaadin.router.QueryParameters;
import com.vaadin.router.Route;
import com.vaadin.router.event.BeforeNavigationEvent;
import com.vaadin.starter.bakery.app.HasLogger;
import com.vaadin.starter.bakery.backend.data.entity.Order;
import com.vaadin.starter.bakery.backend.service.OrderService;
import com.vaadin.starter.bakery.backend.service.ProductService;
import com.vaadin.starter.bakery.backend.service.UserService;
import com.vaadin.starter.bakery.ui.BakeryApp;
import com.vaadin.starter.bakery.ui.components.BakerySearch;
import com.vaadin.starter.bakery.ui.dataproviders.OrdersDataProvider;
import com.vaadin.starter.bakery.ui.event.CancelEvent;
import com.vaadin.starter.bakery.ui.event.SaveEvent;
import com.vaadin.starter.bakery.ui.utils.BakeryConst;
import com.vaadin.starter.bakery.ui.utils.converters.CurrencyFormatter;
import com.vaadin.starter.bakery.ui.utils.converters.LocalDateTimeConverter;
import com.vaadin.starter.bakery.ui.utils.converters.LocalTimeConverter;
import com.vaadin.starter.bakery.ui.utils.converters.LongToStringConverter;
import com.vaadin.starter.bakery.ui.utils.converters.OrderStateConverter;
import com.vaadin.starter.bakery.ui.view.EntityPresenter;
import com.vaadin.starter.bakery.ui.view.EntityView;
import com.vaadin.starter.bakery.ui.view.storefront.converter.StorefrontLocalDateConverter;
import com.vaadin.ui.Component;
import com.vaadin.ui.Tag;
import com.vaadin.ui.common.ClientDelegate;
import com.vaadin.ui.common.HtmlImport;
import com.vaadin.ui.polymertemplate.Id;
import com.vaadin.ui.polymertemplate.PolymerTemplate;

@Tag("bakery-storefront")
@HtmlImport("src/storefront/bakery-storefront.html")
@Route(value = BakeryConst.PAGE_STOREFRONT, layout = BakeryApp.class)
@PageTitle(BakeryConst.TITLE_STOREFRONT)
public class StorefrontView extends PolymerTemplate<StorefrontView.Model>
		implements HasLogger, HasUrlParameter<Long>, EntityView<Order> {

	public interface Model extends TemplateModel {
		@Include({ "id", "dueDate.day", "dueDate.weekday", "dueDate.date", "dueTime", "state", "pickupLocation.name",
				"customer.fullName", "customer.phoneNumber", "customer.details", "items.product.name", "items.comment",
				"items.quantity", "items.product.price", "history.message", "history.createdBy.firstName",
				"history.timestamp", "history.newState", "totalPrice" })
		@Convert(value = LongToStringConverter.class, path = "id")
		@Convert(value = StorefrontLocalDateConverter.class, path = "dueDate")
		@Convert(value = LocalTimeConverter.class, path = "dueTime")
		@Convert(value = OrderStateConverter.class, path = "state")
		@Convert(value = CurrencyFormatter.class, path = "items.product.price")
		@Convert(value = LocalDateTimeConverter.class, path = "history.timestamp")
		@Convert(value = OrderStateConverter.class, path = "history.newState")
		@Convert(value = CurrencyFormatter.class, path = "totalPrice")
		void setSelectedItem(Order order);

		void setEditing(boolean editing);
	}

	@Id("search")
	private BakerySearch searchBar;

	private final Grid<Order> grid;
	private Map<Long, StorefrontItemHeader> ordersWithHeaders;

	private final ViewSelector viewSelector = new ViewSelector();
	private final OrderEdit orderEdit;
	private final OrderDetail orderDetail = new OrderDetail();

	private Presenter presenter;

	private OrdersDataProvider ordersProvider;
	private OrderService orderService;
	private ProductService productService;
	private UserService userService;

	@Autowired
	public StorefrontView(OrdersDataProvider ordersProvider, ProductService productService, OrderService orderService,
			UserService userService, OrderEdit orderEdit) {
		this.productService = productService;
		this.ordersProvider = ordersProvider;
		this.orderService = orderService;
		this.userService = userService;
		this.orderEdit = orderEdit;
		addToSlot(this, viewSelector, "view-selector-slot");
		presenter = new Presenter();

		searchBar.setActionText("New order");
		searchBar.setCheckboxText("Show past orders");
		searchBar.setPlaceHolder("Search");

		grid = new Grid<>();
		grid.getElement().setAttribute("theme", "storefront-grid");
		grid.setSelectionMode(Grid.SelectionMode.NONE);
		grid.addColumn("Order", new ComponentRenderer<>(order -> {
			StorefrontItemDetailWrapper orderCard = new StorefrontItemDetailWrapper();
			orderCard.setOrder(order);
			orderCard.setDisplayHeader(ordersWithHeaders.containsKey(order.getId()));
			orderCard.setHeader(ordersWithHeaders.get(order.getId()));
			orderCard.addOpenedListener(e -> presenter.onOrderCardOpened(orderCard));
			orderCard.addClosedListener(e -> presenter.onOrderCardClosed(orderCard));
			orderCard.addEditListener(e -> presenter.onOrderCardEdit(orderCard));
			orderCard.addCommentListener(e -> presenter.onOrderCardAddComment(orderCard, e.getMessage()));
			return orderCard;
		}));
		addToSlot(this, grid, "grid");

		presenter.filterChanged("", false);
		getModel().setEditing(false);
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
			getUI().ifPresent(
					ui -> ui.navigateTo(BakeryConst.PAGE_STOREFRONT + "/" + id, QueryParameters.simple(parameters)));
			return;
		}
		presenter.createNew();
	}

	@ClientDelegate
	private void unselectOrder() {
		getModel().setSelectedItem(null);
	}

	@Override
	public void setParameter(BeforeNavigationEvent event, @OptionalParameter Long orderId) {
		if (orderId != null) {
			boolean editView = event.getLocation().getQueryParameters().getParameters().containsKey("edit");
			presenter.loadEntity(orderId, editView);
		}
	}

	private void setOrders(List<Order> orders, boolean showPrevious) {
		ordersWithHeaders = computeEntriesWithHeader(orders, showPrevious);
		grid.setItems(orders);
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

	private void details(Order order, boolean isReview) {
		selectComponent(orderDetail);
		orderDetail.display(order, isReview);
	}

	@Override
	public boolean isDirty() {
		return orderEdit.hasChanges();
	}

	@Override
	public void write(Order entity) throws ValidationException {
		orderEdit.write(entity);
	}

	@Override
	public void closeDialog() {
		orderEdit.close();
		getModel().setEditing(false);
		getUI().ifPresent(ui -> ui.navigateTo(BakeryConst.PAGE_STOREFRONT));
	}

	@Override
	public void setDataProvider(DataProvider<Order, Void> dataProvider) {
	}

	@Override
	public void openDialog(Order order, boolean edit) {
		getModel().setEditing(true);
		if (edit) {
			orderEdit.read(order);
			showOrderEdit();
		} else {
			details(order, false);
		}
	}

	private void resizeGrid() {
		grid.getElement().callFunction("notifyResize");
	}

	class Presenter extends EntityPresenter<Order> {

		public Presenter() {
			super(orderService, StorefrontView.this, "Order");
			searchBar.addFilterChangeListener(this::filterChanged);
			searchBar.addActionClickListener(e -> edit(null));

			orderEdit.addListener(CancelEvent.class, e -> cancel());
			orderEdit.addReviewListener(e -> {
				try {
					writeEntity();
					details(getEntity(), true);
				} catch (ValidationException ex) {
					showValidationError();
				}
			});

			orderDetail.addListener(SaveEvent.class, e -> save());
			orderDetail.addCancelListener(e -> closeDialog());
			orderDetail.addBackListener(e -> showOrderEdit());
			orderDetail.addEditListener(e -> {
				orderEdit.read(getEntity());
				showOrderEdit();
			});
			orderDetail.addCommentListener(e -> {
				details(orderService.findOrder(e.getOrderId()), false);
			});
		}

		void filterChanged(String filter, boolean showPrevious) {
			// the hardcoded limit of 15 is here until lazy loading is implemented (see
			// BFF-120)
			PageRequest pr = new PageRequest(0, 15, Direction.ASC, "dueDate", "dueTime", "id");
			setOrders(ordersProvider.getOrdersList(filter, showPrevious, pr).getOrders(), showPrevious);
		}

		@Override
		protected void beforeSave() throws ValidationException {
			// Entity already updated
		}

		@Override
		protected void onSaveSuccess(boolean isNew) {
			super.onSaveSuccess(isNew);

			// refresh the orders list (to be able to see the changes from the just saved
			// order, if any)
			filterChanged(searchBar.getFilter(), searchBar.getShowPrevious());
		}

		@Override
		protected void openDialog(Order entity, boolean edit) {
			orderEdit.init(userService.getCurrentUser(), productService.getRepository().findAll());
			super.openDialog(entity, edit);
		}

		public void addComment(Long id, String comment) {
			executeJPAOperation(() -> {
				Order updated = orderService.addComment(id, comment);
				getModel().setSelectedItem(updated);
			});
		}

		// StorefrontItemDetailWrapper presenter methods
		private void onOrderCardOpened(StorefrontItemDetailWrapper orderCard) {
			getModel().setSelectedItem(orderCard.getOrder());
			orderCard.setSelected(true);
		}

		private void onOrderCardClosed(StorefrontItemDetailWrapper orderCard) {
			getModel().setSelectedItem(null);
			orderCard.setSelected(false);
		}

		private void onOrderCardEdit(StorefrontItemDetailWrapper orderCard) {
			onOrderCardClosed(orderCard);
			edit(orderCard.getOrder().getId().toString());
		}

		private void onOrderCardAddComment(StorefrontItemDetailWrapper orderCard, String message) {
			executeJPAOperation(() -> {
				Order updated = orderService.addComment(orderCard.getOrder().getId(), message);
				orderCard.setOrder(updated);
				resizeGrid();
			});
		}
	}

}
