package com.vaadin.starter.bakery.ui.view.storefront;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import com.vaadin.data.ValidationException;
import com.vaadin.router.QueryParameters;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.starter.bakery.backend.data.entity.Order;
import com.vaadin.starter.bakery.backend.service.OrderService;
import com.vaadin.starter.bakery.backend.service.ProductService;
import com.vaadin.starter.bakery.backend.service.UserService;
import com.vaadin.starter.bakery.ui.dataproviders.OrdersGridDataProvider;
import com.vaadin.starter.bakery.ui.entities.StorefrontItemHeader;
import com.vaadin.starter.bakery.ui.event.CancelEvent;
import com.vaadin.starter.bakery.ui.event.SaveEvent;
import com.vaadin.starter.bakery.ui.utils.BakeryConst;
import com.vaadin.starter.bakery.ui.utils.OrderFilter;
import com.vaadin.starter.bakery.ui.utils.StorefrontItemHeaderGenerator;
import com.vaadin.starter.bakery.ui.view.EntityPresenter;

@SpringComponent
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
class OrderEntityPresenter extends EntityPresenter<Order> {

	private StorefrontItemHeaderGenerator headersGenerator;
	private StorefrontView view;

	private final OrderService orderService;
	private final OrdersGridDataProvider dataProvider;

	@Autowired
	public OrderEntityPresenter(ProductService productService, OrderService orderService, UserService userService,
			OrdersGridDataProvider dataProvider) {
		super(orderService, "Order");
		this.orderService = orderService;
		this.dataProvider = dataProvider;
		headersGenerator = new StorefrontItemHeaderGenerator(orderService);
		headersGenerator.updateHeaders("", false);
	}

	void init(StorefrontView view) {
		super.init(view);
		this.view = view;
		view.getSearchBar().addFilterChangeListener(
				e -> filterChanged(view.getSearchBar().getFilter(), view.getSearchBar().isCheckboxChecked()));
		view.getSearchBar().addActionClickListener(e -> createNew());

		view.getOpenedOrderEditor().addListener(CancelEvent.class, e -> cancel());
		view.getOpenedOrderEditor().addReviewListener(e -> {
			try {
				writeEntity();
				view.openOrderDetails(getEntity(), true);
			} catch (ValidationException ex) {
				showValidationError();
			}
		});

		view.getOpenedOrderDetails().addListener(SaveEvent.class, e -> save());
		view.getOpenedOrderDetails().addCancelListener(e -> cancel());
		view.getOpenedOrderDetails().addBackListener(e -> view.showOrderEdit());
		view.getOpenedOrderDetails().addEditListener(e -> {
			view.getOpenedOrderEditor().read(getEntity());
			view.showOrderEdit();
		});
		view.getOpenedOrderDetails().addCommentListener(e -> {
			this.addComment(e.getOrderId(), e.getMessage());
		});

		view.setDataProvider(dataProvider);
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

	StorefrontItemHeader getHeaderByOrderId(Long id) {
		return headersGenerator.get(id);
	}

	void filterChanged(String filter, boolean showPrevious) {
		headersGenerator.updateHeaders(filter, showPrevious);
		dataProvider.setFilter(new OrderFilter(filter, showPrevious));
	}

	// StorefrontOrderCard presenter methods
	void onOrderCardExpanded(StorefrontOrderCard orderCard) {
		if (view.isDesktopView()) {
			orderCard.setSelected(true);
			view.resizeGrid();
		} else {
			loadEntity(orderCard.getOrder().getId(), false);
		}
	}

	void onOrderCardCollapsed(StorefrontOrderCard orderCard) {
		orderCard.setSelected(false);
		view.resizeGrid();
	}

	void onOrderCardEdit(StorefrontOrderCard orderCard) {
		onOrderCardCollapsed(orderCard);
		edit(orderCard.getOrder().getId().toString());
	}

	void onOrderCardAddComment(StorefrontOrderCard orderCard, String message) {
		executeJPAOperation(() -> {
			Order updated = orderService.addComment(orderCard.getOrder().getId(), message);
			orderCard.setOrder(updated);
			view.resizeGrid();
		});
	}

	private void addComment(Long id, String comment) {
		executeJPAOperation(() -> {
			orderService.addComment(id, comment);
			loadEntity(id, false);
		});
	}

	private void edit(String id) {
		view.getUI().ifPresent(ui -> ui.navigateTo(BakeryConst.PAGE_STOREFRONT + "/" + id,
				QueryParameters.simple(Collections.singletonMap("edit", ""))));
	}

}