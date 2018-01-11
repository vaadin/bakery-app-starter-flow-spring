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
	private final ModalDialogPresenter modalDialogPresenter = new ModalDialogPresenter();

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

	private void openEntity(Long id, boolean edit) {
		view.getGrid().deselectAll();
		view.navigateToEntity(id.toString(), edit);
	}

	private void onOrderCardAddComment(StorefrontOrderCard orderCard, String message) {
		entityPresenter.executeJPAOperation(() -> {
			Order updated = orderService.addComment(currentUser, orderCard.getOrder().getId(), message);
			orderCard.updateOrder(updated);
			view.resizeGrid();
		});
	}

	void loadEntity(Long id, boolean edit) {
		entityPresenter.loadEntity(id, entity -> modalDialogPresenter.open(entity, edit));
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

	void createNew() {
		 modalDialogPresenter.open(this.entityPresenter.createNew(), true);
	}

	void cancel() {
		entityPresenter.cancel(() -> closeDialog());
	}

	void nextModalState() {
		modalDialogPresenter.next.run();
	}
	
	void previousModalState() {
		modalDialogPresenter.previous.run();
	}

	void closeDialog() {
		modalDialogPresenter.close();
		view.navigateToEntity(null, false);
	}

	class ModalDialogPresenter {

		Runnable next;
		Runnable previous;
		
		void open(Order order, boolean edit) {
			view.setEditing(true);
			previous = null;
			if (edit) {
				view.getOpenedOrderEditor().read(order);
				edit();
			} else {
				openOrderDetails(order, false);
				next = () -> open(order, true);
			}
		}

		void close() {
			view.getOpenedOrderEditor().close();
			view.setEditing(false);
			next = null;
			previous = null;
		}

		void setElementsVisibility(boolean editing) {
			view.getOpenedOrderDetails().setVisible(!editing);
			view.getOpenedOrderEditor().setVisible(editing);
		}

		void openOrderDetails(Order order, boolean isReview) {
			setElementsVisibility(false);
			view.getOpenedOrderDetails().display(order, isReview);
		}

		void edit() {
			setElementsVisibility(true);
			next = this::review;
			previous = null;
		}

		void review() {
			HasValue<?, ?> firstErrorField = view.validate().findFirst().orElse(null);
			if (firstErrorField == null) {
				if (entityPresenter.writeEntity()) {
					openOrderDetails(entityPresenter.getEntity(), true);
					next = null;
					previous = this::edit;
				}
			} else if (firstErrorField instanceof Focusable) {
				((Focusable<?>) firstErrorField).focus();
			}
		}

	}
}