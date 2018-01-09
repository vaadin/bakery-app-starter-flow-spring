package com.vaadin.starter.bakery.ui.view.storefront;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import com.vaadin.router.QueryParameters;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.starter.bakery.backend.data.entity.Order;
import com.vaadin.starter.bakery.backend.data.entity.User;
import com.vaadin.starter.bakery.backend.service.OrderService;
import com.vaadin.starter.bakery.backend.service.ProductService;
import com.vaadin.starter.bakery.ui.dataproviders.OrdersGridDataProvider;
import com.vaadin.starter.bakery.ui.entities.StorefrontItemHeader;
import com.vaadin.starter.bakery.ui.event.CancelEvent;
import com.vaadin.starter.bakery.ui.event.SaveEvent;
import com.vaadin.starter.bakery.ui.utils.BakeryConst;
import com.vaadin.starter.bakery.ui.utils.OrderFilter;
import com.vaadin.starter.bakery.ui.utils.StorefrontItemHeaderGenerator;
import com.vaadin.starter.bakery.ui.view.CrudOperationListener;
import com.vaadin.starter.bakery.ui.view.EntityPresenter;
import com.vaadin.ui.common.Focusable;
import com.vaadin.ui.common.HasValue;

@SpringComponent
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
class OrderEntityPresenter {

	private StorefrontItemHeaderGenerator headersGenerator;
	private StorefrontView view;

	private final EntityPresenter<Order> entityPresenter;
	private final OrderService orderService;
	private final OrdersGridDataProvider dataProvider;
	private final User currentUser;

	@Autowired
	public OrderEntityPresenter(ProductService productService, OrderService orderService,
			OrdersGridDataProvider dataProvider, User currentUser) {
		this.entityPresenter = new EntityPresenter<>(orderService, "Order", currentUser);
		this.orderService = orderService;
		this.dataProvider = dataProvider;
		this.currentUser = currentUser;
		headersGenerator = new StorefrontItemHeaderGenerator();
		headersGenerator.resetHeaderChain(false);
		dataProvider.setPageObserver(p -> headersGenerator.ordersRead(p.getContent()));

	}

	void init(StorefrontView view) {
		this.entityPresenter.init(view);
		this.view = view;
		view.getSearchBar().addFilterChangeListener(
				e -> filterChanged(view.getSearchBar().getFilter(), view.getSearchBar().isCheckboxChecked()));
		view.getSearchBar().addActionClickListener(e -> this.entityPresenter.createNew());

		view.getOpenedOrderEditor().addListener(CancelEvent.class, e -> this.entityPresenter.cancel());
		view.getOpenedOrderEditor().addReviewListener(e -> {
			HasValue<?, ?> firstErrorField = view.validate().findFirst().orElse(null);
			if (firstErrorField == null) {
				if (this.entityPresenter.writeEntity()) {
					view.openOrderDetails(this.entityPresenter.getEntity(), true);
				}
			} else if (firstErrorField instanceof Focusable) {
				((Focusable<?>) firstErrorField).focus();
			}
		});

		CrudOperationListener<Order> onSaveSuccess = e -> {
			if(entityPresenter.getEntity().isNew()) {
				dataProvider.refreshAll();
			}else {
				dataProvider.refreshItem(e);
			}
		};
		view.getOpenedOrderDetails().addListener(SaveEvent.class, e -> this.entityPresenter.save(onSaveSuccess));
		view.getOpenedOrderDetails().addCancelListener(e -> this.entityPresenter.cancel());
		view.getOpenedOrderDetails().addBackListener(e -> view.showOrderEdit());
		view.getOpenedOrderDetails().addEditListener(e -> {
			view.getOpenedOrderEditor().read(this.entityPresenter.getEntity());
			view.showOrderEdit();
		});
		view.getOpenedOrderDetails().addCommentListener(e -> {
			entityPresenter.executeJPAOperation(() -> {
				orderService.addComment(currentUser, e.getOrderId(), e.getMessage());
				entityPresenter.loadEntity(e.getOrderId(), false);
			});
		});

		view.setDataProvider(dataProvider);
		view.getOpenedOrderEditor().setCurrentUser(currentUser);
	}

	StorefrontItemHeader getHeaderByOrderId(Long id) {
		return headersGenerator.get(id);
	}

	void filterChanged(String filter, boolean showPrevious) {
		headersGenerator.resetHeaderChain(showPrevious);
		dataProvider.setFilter(new OrderFilter(filter, showPrevious));
	}

	// StorefrontOrderCard presenter methods
	void onOrderCardExpanded(StorefrontOrderCard orderCard) {
		if (view.isDesktopView()) {
			entityPresenter.executeJPAOperation(() -> {
				Order fullOrder = orderService.load(orderCard.getOrder().getId());
				orderCard.openCard(fullOrder);
			});
			view.resizeGrid();
		} else {
			entityPresenter.loadEntity(orderCard.getOrder().getId(), false);
		}
	}

	void onOrderCardCollapsed(StorefrontOrderCard orderCard) {
		orderCard.closeCard();
		view.resizeGrid();
	}

	void onOrderCardEdit(StorefrontOrderCard orderCard) {
		Long id = orderCard.getOrder().getId();
		view.getGrid().deselectAll();
		view.getUI().ifPresent(ui -> ui.navigateTo(BakeryConst.PAGE_STOREFRONT + "/" + id,
				QueryParameters.simple(Collections.singletonMap("edit", ""))));
	}

	void onOrderCardAddComment(StorefrontOrderCard orderCard, String message) {
		entityPresenter.executeJPAOperation(() -> {
			Order updated = orderService.addComment(currentUser, orderCard.getOrder().getId(), message);
			orderCard.updateOrder(updated);
			view.resizeGrid();
		});
	}

	void loadEntity(Long id, boolean edit) {
		entityPresenter.loadEntity(id, edit);
	}

}