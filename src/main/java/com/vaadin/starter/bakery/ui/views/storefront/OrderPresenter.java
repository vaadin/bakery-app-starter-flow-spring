package com.vaadin.starter.bakery.ui.views.storefront;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import com.vaadin.flow.component.Focusable;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.starter.bakery.backend.data.entity.Order;
import com.vaadin.starter.bakery.backend.data.entity.User;
import com.vaadin.starter.bakery.backend.service.OrderService;
import com.vaadin.starter.bakery.ui.crud.EntityPresenter;
import com.vaadin.starter.bakery.ui.dataproviders.OrdersGridDataProvider;

@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
class OrderPresenter {

	private OrderCardHeaderGenerator headersGenerator;
	private StorefrontView view;

	private final EntityPresenter<Order> entityPresenter;
	private final OrdersGridDataProvider dataProvider;
	private final User currentUser;
	private final OrderService orderService;
	private boolean createNew = false;

	@Autowired
	OrderPresenter(OrderService orderService, OrdersGridDataProvider dataProvider,
			EntityPresenter<Order> entityPresenter, User currentUser) {
		this.orderService = orderService;
		this.entityPresenter = entityPresenter;
		this.dataProvider = dataProvider;
		this.currentUser = currentUser;
		headersGenerator = new OrderCardHeaderGenerator();
		headersGenerator.resetHeaderChain(false);
		dataProvider.setPageObserver(p -> headersGenerator.ordersRead(p.getContent()));
	}

	void init(StorefrontView view) {
		this.entityPresenter.setView(view);
		this.view = view;
		view.getGrid().setDataProvider(dataProvider);
		view.getOpenedOrderEditor().setCurrentUser(currentUser);
		view.getOpenedOrderEditor().addCancelListener(e -> cancel());
		view.getOpenedOrderEditor().addReviewListener(e -> review());
		view.getOpenedOrderDetails().addSaveListenter(e -> save());
		view.getOpenedOrderDetails().addCancelListener(e -> cancel());
		view.getOpenedOrderDetails().addBackListener(e -> back());
		view.getOpenedOrderDetails().addEditListener(e -> edit());
		view.getOpenedOrderDetails().addCommentListener(e -> addComment(e.getMessage()));
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
			navigateToOrder(id, false);
		});
	}

	void onNavigation(Long id, boolean edit) {
		entityPresenter.loadEntity(id, e -> open(e, edit));
	}

	void navigateToOrder(Long id, boolean edit) {
		view.navigateToEntity(id.toString(), edit);
	}

	void createNewOrder() {
		open(entityPresenter.createNew(), true);
	}
	
	void cancel() {
		entityPresenter.cancel(() -> close(false), () -> view.setOpened(true));
	}

	void edit() {
		setElementsVisibility(true);
		view.getOpenedOrderEditor().read(entityPresenter.getEntity());
	}

	void back() {
		setElementsVisibility(true);
	}

	void review() {
		HasValue<?, ?> firstErrorField = view.validate().findFirst().orElse(null);
		if (firstErrorField == null) {
			if (entityPresenter.writeEntity()) {
				setElementsVisibility(false);
				view.getOpenedOrderDetails().display(entityPresenter.getEntity(), true);
			}
		} else if (firstErrorField instanceof Focusable) {
			((Focusable<?>) firstErrorField).focus();
		}
	}

	void save() {
		entityPresenter.save(e -> close(true));
	}

	void addComment(String comment) {
		if (entityPresenter.executeUpdate(e -> orderService.addComment(currentUser, e, comment))) {
			// You can only add comments when in view mode, so reopening in that state.
			open(entityPresenter.getEntity(), false);
		}
	}

	private void open(Order order, boolean edit) {
		view.setOpened(true);
		setElementsVisibility(edit);
		if (edit) {
			view.getOpenedOrderEditor().read(order);
		} else {
			view.getOpenedOrderDetails().display(order, false);
		}
	}

	private void setElementsVisibility(boolean editing) {
		view.getOpenedOrderDetails().setVisible(!editing);
		view.getOpenedOrderEditor().setVisible(editing);
	}

	private void close(boolean updated) {
		view.getOpenedOrderEditor().close();
		view.setOpened(false);
			
		if (createNew) {
			dataProvider.refreshAll();
		} else {
			view.navigateToMainView();
			if (updated) {
				dataProvider.refreshItem(entityPresenter.getEntity());
			}
		}
		entityPresenter.close();
	}
}
