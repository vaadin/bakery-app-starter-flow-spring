package com.vaadin.starter.bakery.ui.view.storefront;

import static com.vaadin.starter.bakery.ui.utils.TemplateUtil.addToSlot;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vaadin.data.provider.Query;
import com.vaadin.data.provider.QuerySortOrder;
import com.vaadin.data.provider.QuerySortOrderBuilder;
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
import com.vaadin.starter.bakery.ui.dataproviders.OrdersDataProvider;
import com.vaadin.starter.bakery.ui.event.CancelEvent;
import com.vaadin.starter.bakery.ui.event.SaveEvent;
import com.vaadin.starter.bakery.ui.utils.BakeryConst;
import com.vaadin.starter.bakery.ui.view.EntityPresenter;
import com.vaadin.starter.bakery.ui.view.EntityView;
import com.vaadin.ui.Tag;
import com.vaadin.ui.common.HtmlImport;
import com.vaadin.ui.polymertemplate.Id;
import com.vaadin.ui.polymertemplate.PolymerTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

	private final Grid<Order> grid = new Grid<>();
	private Map<Long, StorefrontItemHeader> ordersWithHeaders = new HashMap<>();

	@Id("order-edit")
	private OrderEdit orderEdit;

	@Id("order-detail")
	private OrderDetail orderDetail;

	private OrderEntityPresenter presenter;

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

		// required for the `isDesktopView()` method
		getElement().synchronizeProperty("desktopView", "desktop-view-changed");

		searchBar.setActionText("New order");
		searchBar.setCheckboxText("Show past orders");
		searchBar.setPlaceHolder("Search");

		grid.getElement().setAttribute("theme", "storefront-grid");
		grid.setSelectionMode(Grid.SelectionMode.NONE);
		grid.addColumn("Order", new ComponentRenderer<>(order -> {
			StorefrontItemDetailWrapper orderCard = new StorefrontItemDetailWrapper();
			orderCard.setOrder(order);
			orderCard.setHeader(ordersWithHeaders.get(order.getId()));
			orderCard.addExpandedListener(e -> presenter.onOrderCardExpanded(orderCard));
			orderCard.addCollapsedListener(e -> presenter.onOrderCardCollapsed(orderCard));
			orderCard.addEditListener(e -> presenter.onOrderCardEdit(orderCard));
			orderCard.addCommentListener(e -> presenter.onOrderCardAddComment(orderCard, e.getMessage()));
			return orderCard;
		}));
		addToSlot(this, grid, "grid");

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
		final private Sort defaultSort = new Sort(Sort.Direction.ASC, "dueDate", "dueTime", "id");

		public OrderEntityPresenter() {
			super(orderService, StorefrontView.this, "Order");
			searchBar.addFilterChangeListener(e -> filterChanged(searchBar.getFilter(), searchBar.isCheckboxChecked()));
			searchBar.addCheckboxValueChangeListener(
					e -> filterChanged(searchBar.getFilter(), searchBar.isCheckboxChecked()));
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

			dataProvider = new FilterablePageableDataProvider<Order, OrderFilter>() {
				@Override
				protected Page<Order> fetchFromBackEnd(Query<Order, OrderFilter> query, Pageable pageable) {
					OrderFilter filter = query.getFilter().orElse(OrderFilter.getEmptyFilter());
					Page<Order> page = ordersProvider.fetchFromBackEnd(filter, pageable);
					return page;
				}

				@Override
				protected List<QuerySortOrder> getDefaultSortOrders() {
					return new QuerySortOrderBuilder().thenAsc("dueDate").thenAsc("dueTime").thenAsc("id").build();
				}

				@Override
				protected int sizeInBackEnd(Query<Order, OrderFilter> query) {
					return (int) ordersProvider
							.countAnyMatchingAfterDueDate(query.getFilter().orElse(OrderFilter.getEmptyFilter()));
				}
			};
			updateHeaders("", false);
			setDataProvider(dataProvider);
		}

		void updateHeaders(String filter, boolean showPrevious) {
			ordersWithHeaders.clear();

			if (showPrevious) {
				LocalDate date = LocalDate.now().minusDays(2);
				Long id = ordersProvider.findFirstOrderAfterDueDate(filter, date, defaultSort);
				if (id != null) {
					ordersWithHeaders.put(id, StorefrontItemHeaderGenerator.getYesterdayHeader());
				}

				date = date.minusDays(date.getDayOfWeek().getValue());
				id = ordersProvider.findFirstOrderAfterDueDate(filter, date, defaultSort);
				if (id != null) {
					ordersWithHeaders.put(id, StorefrontItemHeaderGenerator.getThisWeekBeforeYesterdayHeader());
				}

				id = ordersProvider.findFirstOrderAfterDueDate(filter, null, defaultSort);
				if (id != null) {
					ordersWithHeaders.put(id, StorefrontItemHeaderGenerator.getRecentHeader());
				}
			}

			LocalDate date = LocalDate.now().minusDays(1);
			Long id = ordersProvider.findFirstOrderAfterDueDate(filter, date, defaultSort);
			if (id != null) {
				ordersWithHeaders.put(id, StorefrontItemHeaderGenerator.getTodayHeader());
			}

			date = LocalDate.now();
			id = ordersProvider.findFirstOrderAfterDueDate(filter, date, defaultSort);
			if (id != null) {
				ordersWithHeaders.put(id, StorefrontItemHeaderGenerator.getThisWeekStartingTomorrow(showPrevious));
			}

			date = date.minusDays(date.getDayOfWeek().getValue()).plusWeeks(1);
			id = ordersProvider.findFirstOrderAfterDueDate(filter, date, defaultSort);
			if (id != null) {
				ordersWithHeaders.put(id, StorefrontItemHeaderGenerator.getUpcomingHeader());
			}
		}

		void filterChanged(String filter, boolean showPrevious) {
			updateHeaders(filter, showPrevious);
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
