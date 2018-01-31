package com.vaadin.starter.bakery.ui.views.storefront;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.starter.bakery.backend.data.entity.Order;
import com.vaadin.starter.bakery.backend.data.entity.User;
import com.vaadin.starter.bakery.backend.service.OrderService;
import com.vaadin.starter.bakery.backend.service.ProductService;
import com.vaadin.starter.bakery.ui.crud.EntityPresenter;
import com.vaadin.starter.bakery.ui.dataproviders.OrdersGridDataProvider;

@SpringComponent
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
class OrderPresenter {

	private OrderCardHeaderGenerator headersGenerator;
	private StorefrontView view;
	private List<Registration> registrations = Collections.emptyList();

	private final EntityPresenter<Order> entityPresenter;
	private final OrderService orderService;
	private final OrdersGridDataProvider dataProvider;
	private final User currentUser;
	private final SingleOrderPresenter singleOrderPresenter;

	@Autowired
	OrderPresenter(ProductService productService, OrderService orderService, OrdersGridDataProvider dataProvider,
			EntityPresenter<Order> entityPresenter, SingleOrderPresenter singleOrderPresenter, User currentUser) {
		this.entityPresenter = entityPresenter;
		this.orderService = orderService;
		this.dataProvider = dataProvider;
		this.currentUser = currentUser;
		headersGenerator = new OrderCardHeaderGenerator();
		headersGenerator.resetHeaderChain(false);
		dataProvider.setPageObserver(p -> headersGenerator.ordersRead(p.getContent()));
		this.singleOrderPresenter = singleOrderPresenter;
	}

	void init(StorefrontView view) {
		this.entityPresenter.setView(view);
		this.view = view;
		view.getGrid().setDataProvider(dataProvider);
		view.getOpenedOrderEditor().setCurrentUser(currentUser);
		singleOrderPresenter.init(view);
	}

	OrderCardHeader getHeaderByOrderId(Long id) {
		return headersGenerator.get(id);
	}

	public void filterChanged(String filter, boolean showPrevious) {
		headersGenerator.resetHeaderChain(showPrevious);
		dataProvider.setFilter(new OrderFilter(filter, showPrevious));
	}

	void onOrderCardExpanded(OrderCard orderCard) {
		entityPresenter.loadEntity(orderCard.getOrderId(), entity -> {
			final Long id = entity.getId();
			if (view.isDesktopView()) {
				registrations = Arrays.asList(orderCard.addEditListener(e -> navigateToOrder(id, true)),
						orderCard.addCommentListener(e -> onOrderCardAddComment(orderCard, e.getMessage())),
						orderCard.addCancelListener(e -> cancelOrder()));
				orderCard.openCard(entity);
				view.resizeGrid();
			} else {
				navigateToOrder(id, false);
			}
		});
	}

	void onOrderCardCollapsed(OrderCard orderCard) {
		registrations.forEach(Registration::remove);
		registrations = Collections.emptyList();
		entityPresenter.close();
		orderCard.closeCard();
		view.resizeGrid();
	}

	void onNavigation(Long id, boolean edit) {
		singleOrderPresenter.openOrder(id, edit, (e, updated) -> {
			view.navigateToMainView();
			if (updated) {
				dataProvider.refreshItem(e);
			}
		});
	}

	void cancelOrder() {
		view.getGrid().getElement().setProperty("activeItem", null);
	}

	void navigateToOrder(Long id, boolean edit) {
		cancelOrder();
		view.navigateToEntity(id.toString(), edit);
	}

	void createNewOrder() {
		singleOrderPresenter.createOrder((e, updated) -> dataProvider.refreshAll());
	}

	private void onOrderCardAddComment(OrderCard orderCard, String message) {
		if (entityPresenter.executeUpdate(e -> orderService.addComment(currentUser, e, message))) {
			orderCard.updateOrder(entityPresenter.getEntity());
			view.resizeGrid();
		}
	}

}
