package com.vaadin.starter.bakery.ui.view.storefront;

import java.util.HashMap;
import java.util.Map;

import com.vaadin.starter.bakery.ui.dataproviders.OrdersGridDataProvider;
import com.vaadin.starter.bakery.ui.entities.StorefrontItemHeader;
import com.vaadin.starter.bakery.ui.utils.OrderFilter;
import com.vaadin.starter.bakery.ui.utils.StorefrontItemHeaderGenerator;
import com.vaadin.ui.grid.Grid;
import com.vaadin.ui.renderers.ComponentRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import com.vaadin.data.ValidationException;
import com.vaadin.data.provider.DataProvider;
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
import com.vaadin.starter.bakery.ui.event.CancelEvent;
import com.vaadin.starter.bakery.ui.event.SaveEvent;
import com.vaadin.starter.bakery.ui.utils.BakeryConst;
import com.vaadin.starter.bakery.ui.view.EntityPresenter;
import com.vaadin.starter.bakery.ui.view.EntityView;
import com.vaadin.ui.Tag;
import com.vaadin.ui.common.HtmlImport;
import com.vaadin.ui.polymertemplate.Id;
import com.vaadin.ui.polymertemplate.PolymerTemplate;
import org.springframework.data.domain.Sort;
import org.vaadin.artur.spring.dataprovider.FilterablePageableDataProvider;

@Tag("bakery-storefront")
@HtmlImport("src/storefront/bakery-storefront.html")
@Route(value = BakeryConst.PAGE_STOREFRONT, layout = BakeryApp.class)
@PageTitle(BakeryConst.TITLE_STOREFRONT)
public class StorefrontView extends PolymerTemplate<StorefrontView.Model>
		implements HasLogger, HasUrlParameter<Long>, EntityView<Order> {

	public interface Model extends TemplateModel {
		void setEditing(boolean editing);
	}

	@Id("search")
	private BakerySearch searchBar;

	@Id("storefront-grid")
	private Grid<Order> grid;

	@Id("order-edit")
	private OrderEdit orderEdit;

	@Id("order-detail")
	private OrderDetail orderDetail;

	private OrderEntityPresenter presenter;

	private OrderService orderService;
	private ProductService productService;
	private UserService userService;

	@Autowired
	public StorefrontView(ProductService productService, OrderService orderService,
			UserService userService) {
		this.productService = productService;
		this.orderService = orderService;
		this.userService = userService;

		// required for the `isDesktopView()` method
		getElement().synchronizeProperty("desktopView", "desktop-view-changed");

		searchBar.setActionText("New order");
		searchBar.setCheckboxText("Show past orders");
		searchBar.setPlaceHolder("Search");

		grid.setSelectionMode(Grid.SelectionMode.NONE);
		grid.addColumn("Order", new ComponentRenderer<>(order -> {
			StorefrontItemDetailWrapper orderCard = new StorefrontItemDetailWrapper();
			orderCard.setOrder(order);
			orderCard.setHeader(presenter.getHeaderByOrderId(order.getId()));
			orderCard.addExpandedListener(e -> presenter.onOrderCardExpanded(orderCard));
			orderCard.addCollapsedListener(e -> presenter.onOrderCardCollapsed(orderCard));
			orderCard.addEditListener(e -> presenter.onOrderCardEdit(orderCard));
			orderCard.addCommentListener(e -> presenter.onOrderCardAddComment(orderCard, e.getMessage()));
			return orderCard;
		}));

		getModel().setEditing(false);
		presenter = new OrderEntityPresenter();
	}

	@Override
	public void setParameter(BeforeNavigationEvent event, @OptionalParameter Long orderId) {
		if (orderId != null) {
			boolean editView = event.getLocation().getQueryParameters().getParameters().containsKey("edit");
			presenter.loadEntity(orderId, editView);
		}
	}

	private void showOrderEdit() {
		orderDetail.getElement().setAttribute("hidden", "");
		orderEdit.getElement().removeAttribute("hidden");
	}

	private void details(Order order, boolean isReview) {
		orderDetail.getElement().removeAttribute("hidden");
		orderEdit.getElement().setAttribute("hidden", "");
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
	public void setDataProvider(DataProvider<Order, ?> dataProvider) {
		grid.setDataProvider(dataProvider);
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

	private boolean isDesktopView() {
		return getElement().getProperty("desktopView", true);
	}

	private void resizeGrid() {
		grid.getElement().callFunction("notifyResize");
	}

	class OrderEntityPresenter extends EntityPresenter<Order> {

		private FilterablePageableDataProvider<Order, OrderFilter> dataProvider;
		private StorefrontItemHeaderGenerator headersGenerator;
		private final String[] orderSortFields = {"dueDate", "dueTime", "id"};

		public OrderEntityPresenter() {
			super(orderService, StorefrontView.this, "Order");
			searchBar.addFilterChangeListener(e -> filterChanged(searchBar.getFilter(), searchBar.isCheckboxChecked()));
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
			orderDetail.addCancelListener(e -> cancel());
			orderDetail.addBackListener(e -> showOrderEdit());
			orderDetail.addEditListener(e -> {
				orderEdit.read(getEntity());
				showOrderEdit();
			});
			orderDetail.addCommentListener(e -> {
				presenter.addComment(e.getOrderId(), e.getMessage());
			});

			dataProvider = new OrdersGridDataProvider(orderService, Sort.Direction.ASC, orderSortFields);
			headersGenerator = new StorefrontItemHeaderGenerator(orderService, orderSortFields);
			headersGenerator.updateHeaders("", false);
			setDataProvider(dataProvider);
		}

		public StorefrontItemHeader getHeaderByOrderId(Long id) {
			return headersGenerator.get(id);
		}

		void filterChanged(String filter, boolean showPrevious) {
			headersGenerator.updateHeaders(filter, showPrevious);
			dataProvider.setFilter(new OrderFilter(filter, showPrevious));
		}

		@Override
		protected void beforeSave() throws ValidationException {
			// Entity already updated
		}

		@Override
		protected void onSaveSuccess(boolean isNew) {
			if (isNew) {
				dataProvider.refreshAll();
			} else {
				dataProvider.refreshItem(getEntity());
			}

			super.onSaveSuccess(isNew);
		}

		@Override
		protected void openDialog(Order entity, boolean edit) {
			orderEdit.init(userService.getCurrentUser(), productService.getRepository().findAll());
			super.openDialog(entity, edit);
		}

		private void addComment(Long id, String comment) {
			executeJPAOperation(() -> {
				orderService.addComment(id, comment);
				loadEntity(id, false);
			});
		}

		private void edit(String id) {
			if (id != null && !id.isEmpty()) {
				Map<String, String> parameters = new HashMap<>();
				parameters.put("edit", "");
				getUI().ifPresent(ui -> ui.navigateTo(BakeryConst.PAGE_STOREFRONT + "/" + id,
						QueryParameters.simple(parameters)));
				return;
			}
			createNew();
		}

		// StorefrontItemDetailWrapper presenter methods
		private void onOrderCardExpanded(StorefrontItemDetailWrapper orderCard) {
			if (isDesktopView()) {
				orderCard.setSelected(true);
				resizeGrid();
			} else {
				loadEntity(orderCard.getOrder().getId(), false);
			}
		}

		private void onOrderCardCollapsed(StorefrontItemDetailWrapper orderCard) {
			orderCard.setSelected(false);
			resizeGrid();
		}

		private void onOrderCardEdit(StorefrontItemDetailWrapper orderCard) {
			onOrderCardCollapsed(orderCard);
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
