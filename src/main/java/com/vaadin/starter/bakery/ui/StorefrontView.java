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

import com.vaadin.annotations.ClientDelegate;
import com.vaadin.annotations.HtmlImport;
import com.vaadin.annotations.Id;
import com.vaadin.annotations.Tag;
import com.vaadin.annotations.Title;
import com.vaadin.flow.router.LocationChangeEvent;
import com.vaadin.flow.router.View;
import com.vaadin.flow.template.PolymerTemplate;
import com.vaadin.flow.template.model.TemplateModel;
import com.vaadin.hummingbird.ext.spring.annotations.ParentView;
import com.vaadin.hummingbird.ext.spring.annotations.Route;
import com.vaadin.starter.bakery.app.HasLogger;
import com.vaadin.starter.bakery.backend.service.OrderService;
import com.vaadin.starter.bakery.backend.service.ProductService;
import com.vaadin.starter.bakery.backend.service.UserService;
import com.vaadin.starter.bakery.ui.components.ConfirmationDialog;
import com.vaadin.starter.bakery.ui.components.viewselector.ViewSelector;
import com.vaadin.starter.bakery.ui.dataproviders.OrdersDataProvider;
import com.vaadin.starter.bakery.ui.entities.Order;
import com.vaadin.starter.bakery.ui.messages.Message;
import com.vaadin.starter.bakery.ui.utils.BakeryConst;

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

	private ViewSelector viewSelector;

	@Id("confirmation-dialog")
	private ConfirmationDialog confirmationDialog;

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

		viewSelector = new ViewSelector(productService, userService);
		addToSlot(this, viewSelector, "view-selector");
		searchBar.setActionText("New order");
		searchBar.setCheckboxText("Show past orders");
		searchBar.setPlaceHolder("Search");
		searchBar.addFilterChangeListener(this::filterItems);
		searchBar.addActionClickListener(e -> edit(null));

		filterItems(searchBar.getFilter(), searchBar.getShowPrevious());

		prepareOrderEditWrapper(orderService);
		getModel().setEditing(false);
	}

	private void prepareOrderEditWrapper(OrderService orderService) {
		viewSelector.addSaveListener(e -> {
			com.vaadin.starter.bakery.backend.data.entity.Order order = e.getOrder();
			boolean isNew = order.getId() == null;
			orderService.saveOrder(order);
			closeEditor();
			if (isNew) {
				filterItems(searchBar.getFilter(), searchBar.getShowPrevious());
			} else {
				updateOrderInModel(order.getId().toString());
			}
		});

		Message CONFIRM_CANCEL = Message.UNSAVED_CHANGES.createMessage("Order");
		viewSelector.addCancelListener(e -> {
			if (e.hasChanges()) {
				confirmationDialog.show(CONFIRM_CANCEL, ev -> this.closeEditor());
			} else {
				this.closeEditor();
			}
		});

		viewSelector.addCommentListener(e -> {
			if (e.getOrderId() == null) {
				return;
			}

			addComment(e.getOrderId().toString(), e.getMessage());
			viewSelector.openDetails(orderService.findOrder(e.getOrderId()));
		});
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
		order = new com.vaadin.starter.bakery.backend.data.entity.Order(userService.getCurrentUser());
		order.setDueTime(LocalTime.of(16, 0));
		order.setDueDate(LocalDate.now());
		openOrderEditor(order);
	}

	private void closeEditor() {
		viewSelector.close();
		getUI().ifPresent(ui -> ui.navigateTo(BakeryConst.PAGE_STOREFRONT));
		getModel().setEditing(false);
	}

	private void openOrderEditor(com.vaadin.starter.bakery.backend.data.entity.Order order) {
		viewSelector.openEdit(order, userService.getCurrentUser(), productService.getRepository().findAll());
		getModel().setEditing(true);
	}

	@Override
	public void onLocationChange(LocationChangeEvent locationChangeEvent) {
		String orderId = locationChangeEvent.getPathParameter("id");
		try {
			Long id = Long.parseLong(orderId);
			com.vaadin.starter.bakery.backend.data.entity.Order order = orderService.findOrder(id);
			if (locationChangeEvent.getLocation().getSegments().contains("edit")) {
				openOrderEditor(order);
			} else {
				viewSelector.openDetails(order);
			}
		} catch (Exception e) {
		}
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
}
