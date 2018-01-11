package com.vaadin.starter.bakery.ui.view.storefront;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import com.vaadin.shared.Registration;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.starter.bakery.backend.data.entity.Order;
import com.vaadin.starter.bakery.backend.data.entity.User;
import com.vaadin.starter.bakery.backend.service.OrderService;
import com.vaadin.starter.bakery.backend.service.ProductService;
import com.vaadin.starter.bakery.ui.crud.EntityPresenter;
import com.vaadin.starter.bakery.ui.dataproviders.OrdersGridDataProvider;
import com.vaadin.starter.bakery.ui.entities.StorefrontItemHeader;
import com.vaadin.starter.bakery.ui.utils.OrderFilter;
import com.vaadin.starter.bakery.ui.utils.StorefrontItemHeaderGenerator;
import com.vaadin.ui.common.Focusable;
import com.vaadin.ui.common.HasValue;

@SpringComponent
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
class OrderPresenter {

	private StorefrontItemHeaderGenerator headersGenerator;
	private StorefrontView view;
	private List<Registration> registrations = Collections.emptyList();

	private final EntityPresenter<Order> entityPresenter;
	private final OrderService orderService;
	private final OrdersGridDataProvider dataProvider;
	private final User currentUser;

	
	@Autowired
	OrderPresenter(ProductService productService, OrderService orderService, OrdersGridDataProvider dataProvider,
			EntityPresenter<Order> entityPresenter, User currentUser) {
		this.entityPresenter = entityPresenter;
		this.orderService = orderService;
		this.dataProvider = dataProvider;
		this.currentUser = currentUser;
		headersGenerator = new StorefrontItemHeaderGenerator();
		headersGenerator.resetHeaderChain(false);
		dataProvider.setPageObserver(p -> headersGenerator.ordersRead(p.getContent()));
	}

	void init(StorefrontView view) {
		this.entityPresenter.setView(view);
		this.view = view;
		view.getGrid().setDataProvider(dataProvider);
		view.getOpenedOrderEditor().setCurrentUser(currentUser);
	}

	StorefrontItemHeader getHeaderByOrderId(Long id) {
		return headersGenerator.get(id);
	}

	public void filterChanged(String filter, boolean showPrevious) {
		headersGenerator.resetHeaderChain(showPrevious);
		dataProvider.setFilter(new OrderFilter(filter, showPrevious));
	}

	// StorefrontOrderCard presenter methods
	void onOrderCardExpanded(StorefrontOrderCard orderCard) {
		entityPresenter.loadEntity(orderCard.getOrder().getId(), entity -> {
			final Long id = entity.getId();
			if (view.isDesktopView()) {
				registrations = Arrays.asList(orderCard.addEditListener(e -> openEntity(id, true)),
						orderCard.addCommentListener(e -> onOrderCardAddComment(orderCard, e.getMessage())),
						orderCard.addCancelListener(e -> view.getGrid().deselectAll()));
				orderCard.openCard(entity);
				view.resizeGrid();
			} else {
				openEntity(id, false);
			}
		});
	}

	void onOrderCardCollapsed(StorefrontOrderCard orderCard) {
		registrations.forEach(Registration::remove);
		registrations = Collections.emptyList();
		entityPresenter.close();
		orderCard.closeCard();
		view.resizeGrid();
	}

	private void openEntity(Long id,boolean edit) {
		view.getGrid().deselectAll();
		view.navigateToEntity(id.toString(), edit);
	}

	private void onOrderCardAddComment(StorefrontOrderCard orderCard,String message) {
		entityPresenter.executeJPAOperation(() -> {
			Order updated = orderService.addComment(currentUser, orderCard.getOrder().getId(), message);
			orderCard.updateOrder(updated);
			view.resizeGrid();
		});
	}

	void openDialog(Order order, boolean edit) {
		view.setEditing(true);
		if (edit) {
			view.getOpenedOrderEditor().read(order);
			showOrderEdit();
		} else {
			openOrderDetails(order, false);
		}
	}

	void showOrderEdit() {
		view.getOpenedOrderDetails().setVisible(false);
		view.getOpenedOrderEditor().setVisible(true);
	}

	void showOrderDetails() {
		view.getOpenedOrderDetails().setVisible(true);
		view.getOpenedOrderEditor().setVisible(false);		
	}
 
	void openOrderDetails(Order order, boolean isReview) {
		showOrderDetails();
		view.getOpenedOrderDetails().display(order, isReview);
	}

	void loadEntity(Long id, boolean edit) {
		entityPresenter.loadEntity(id, entity -> openDialog(entity, edit));
	}

	void addComment(Long id, String comment) {
		entityPresenter.executeJPAOperation(() -> {
			orderService.addComment(currentUser, id, comment);
			loadEntity(id, false);
		});
	}

	void save() {
		entityPresenter.save(e -> {
			if (e.isNew()) {
				dataProvider.refreshAll();
			} else {
				dataProvider.refreshItem(e);
				closeDialog();
			}
		});
	}

	void review() {
		HasValue<?, ?> firstErrorField = view.validate().findFirst().orElse(null);
		if (firstErrorField == null) {
			if (this.entityPresenter.writeEntity()) {
				openOrderDetails(this.entityPresenter.getEntity(), true);
			}
		} else if (firstErrorField instanceof Focusable) {
			((Focusable<?>) firstErrorField).focus();
		}
	}

	void createNew() {
		openDialog(this.entityPresenter.createNew(), true);
	}

	void cancel() {
		entityPresenter.cancel(() -> closeDialog());
	}

	void closeDialog() {
		view.getOpenedOrderEditor().close();
		view.setEditing(false);
		view.navigateToEntity(null, false);
	}

}