package com.vaadin.starter.bakery.ui;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.access.annotation.Secured;

import com.vaadin.annotations.ClientDelegate;
import com.vaadin.annotations.EventHandler;
import com.vaadin.annotations.HtmlImport;
import com.vaadin.annotations.Id;
import com.vaadin.annotations.Tag;
import com.vaadin.flow.router.View;
import com.vaadin.flow.template.PolymerTemplate;
import com.vaadin.flow.template.model.TemplateModel;
import com.vaadin.hummingbird.ext.spring.annotations.ParentView;
import com.vaadin.hummingbird.ext.spring.annotations.Route;
import com.vaadin.starter.bakery.app.HasLogger;
import com.vaadin.starter.bakery.backend.data.Role;
import com.vaadin.starter.bakery.backend.data.entity.User;
import com.vaadin.starter.bakery.backend.service.OrderService;
import com.vaadin.starter.bakery.backend.service.ProductService;
import com.vaadin.starter.bakery.backend.service.UserService;
import com.vaadin.starter.bakery.ui.components.ConfirmationDialog;
import com.vaadin.starter.bakery.ui.components.storefront.OrderEditWrapper;
import com.vaadin.starter.bakery.ui.dataproviders.OrdersDataProvider;
import com.vaadin.starter.bakery.ui.entities.Order;
import com.vaadin.starter.bakery.ui.messages.Message;
import com.vaadin.starter.bakery.ui.utils.BakeryConst;
import com.vaadin.starter.bakery.ui.utils.TemplateUtil;

import static com.vaadin.starter.bakery.ui.utils.StorefrontItemHeaderGenerator.computeEntriesWithHeader;
import static com.vaadin.starter.bakery.ui.utils.TemplateUtil.addToSlot;

@Tag("bakery-storefront")
@HtmlImport("context://src/storefront/bakery-storefront.html")
@Route(BakeryConst.PAGE_STOREFRONT)
@Route(value = "")
@ParentView(BakeryApp.class)
@Secured(Role.BARISTA)
public class StorefrontView extends PolymerTemplate<StorefrontView.Model> implements View, HasLogger, HasToast {

	public interface Model extends TemplateModel {
		void setOrders(List<Order> orders);

		List<Order> getOrders();

		void setEditing(boolean editing);
	}

	@Id("search")
	private BakerySearch searchBar;

	private OrderEditWrapper editWrapper;

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

		editWrapper = new OrderEditWrapper();
		addToSlot(this, editWrapper, "order-edit-wrapper");

		searchBar.setActionText("New order");
		searchBar.setCheckboxText("Show past orders");
		searchBar.setPlaceHolder("Search");
		searchBar.addFilterChangeListener(this::filterItems);
		searchBar.addActionClickListener(e -> edit(null));

		filterItems(searchBar.getFilter(), searchBar.getShowPrevious());

		prepareOrderEditWrapper(orderService);
	}

	private void prepareOrderEditWrapper(OrderService orderService) {
		editWrapper.addSaveListener(e -> {
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
		editWrapper.addCancelListener(e -> {
			if (e.hasChanges()) {
				confirmationDialog.show(CONFIRM_CANCEL, ev -> this.closeEditor());
			} else {
				this.closeEditor();
			}
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
		com.vaadin.starter.bakery.backend.data.entity.Order order;
		User currentUser = userService.getCurrentUser();
		if (id == null) {
			order = new com.vaadin.starter.bakery.backend.data.entity.Order(currentUser);
			order.setDueTime(LocalTime.of(16, 0));
			order.setDueDate(LocalDate.now());
		} else {
			order = orderService.findOrder(Long.valueOf(id));
		}

		editWrapper.openEdit(order, currentUser, productService.getRepository().findAll());
		getModel().setEditing(true);

	}

	@EventHandler
	public void closeEditor() {
		getModel().setEditing(false);
		editWrapper.close();
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
