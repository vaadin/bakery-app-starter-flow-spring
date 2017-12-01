package com.vaadin.starter.bakery.ui.view.storefront;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.data.ValidationException;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.selection.SingleSelectionListener;
import com.vaadin.flow.model.TemplateModel;
import com.vaadin.router.HasUrlParameter;
import com.vaadin.router.OptionalParameter;
import com.vaadin.router.PageTitle;
import com.vaadin.router.Route;
import com.vaadin.router.RouteAlias;
import com.vaadin.router.event.BeforeNavigationEvent;
import com.vaadin.starter.bakery.app.HasLogger;
import com.vaadin.starter.bakery.backend.data.entity.Order;
import com.vaadin.starter.bakery.ui.BakeryApp;
import com.vaadin.starter.bakery.ui.components.BakerySearch;
import com.vaadin.starter.bakery.ui.utils.BakeryConst;
import com.vaadin.starter.bakery.ui.view.EntityView;
import com.vaadin.ui.Tag;
import com.vaadin.ui.common.HtmlImport;
import com.vaadin.ui.grid.Grid;
import com.vaadin.ui.grid.GridSingleSelectionModel;
import com.vaadin.ui.polymertemplate.Id;
import com.vaadin.ui.polymertemplate.PolymerTemplate;
import com.vaadin.ui.renderers.ComponentRenderer;

@Tag("bakery-storefront")
@HtmlImport("src/storefront/bakery-storefront.html")
@Route(value = BakeryConst.PAGE_STOREFRONT, layout = BakeryApp.class)
@RouteAlias(value = BakeryConst.PAGE_ROOT, layout = BakeryApp.class)
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

	@Id("order-editor")
	private OrderEditor openedOrderEditor;

	@Id("order-details")
	private OrderDetailsFull openedOrderDetails;

	private OrderEntityPresenter presenter;

	@Autowired
	public StorefrontView(OrderEntityPresenter presenter) {

		this.presenter = presenter;
		// required for the `isDesktopView()` method
		getElement().synchronizeProperty("desktopView", "desktop-view-changed");

		searchBar.setActionText("New order");
		searchBar.setCheckboxText("Show past orders");
		searchBar.setPlaceHolder("Search");

		grid.setSelectionMode(Grid.SelectionMode.SINGLE);
		Map<Order, StorefrontOrderCard> components = new HashMap<>();
		grid.addColumn(new ComponentRenderer<>(order -> {
			StorefrontOrderCard orderCard = new StorefrontOrderCard();
			orderCard.setOrder(order);
			orderCard.setHeader(presenter.getHeaderByOrderId(order.getId()));
			orderCard.addEditListener(e -> presenter.onOrderCardEdit(orderCard));
			orderCard.addCommentListener(e -> presenter.onOrderCardAddComment(orderCard, e.getMessage()));
			orderCard.addCancelListener(e -> grid.deselectAll());
			components.put(order, orderCard);
			return orderCard;
		}));
		SingleSelectionListener<Grid<Order>, Order> listener = e -> {
			if (e.getOldValue() != null) {
				presenter.onOrderCardCollapsed(components.get(e.getOldValue()));
			}
			if (e.getValue() != null) {
				presenter.onOrderCardExpanded(components.get(e.getValue()));
			}
		};
		((GridSingleSelectionModel<Order>) grid.getSelectionModel()).addSingleSelectionListener(listener);
		getModel().setEditing(false);
		presenter.init(this);
	}

	@Override
	public void setParameter(BeforeNavigationEvent event, @OptionalParameter Long orderId) {
		if (orderId != null) {
			boolean editView = event.getLocation().getQueryParameters().getParameters().containsKey("edit");
			presenter.loadEntity(orderId, editView);
		}
	}

	@Override
	public boolean isDirty() {
		return openedOrderEditor.hasChanges();
	}

	@Override
	public void write(Order entity) throws ValidationException {
		openedOrderEditor.write(entity);
	}

	@Override
	public void closeDialog() {
		openedOrderEditor.close();
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
			openedOrderEditor.read(order);
			showOrderEdit();
		} else {
			openOrderDetails(order, false);
		}
	}

	void showOrderEdit() {
		openedOrderDetails.getElement().setAttribute("hidden", "");
		openedOrderEditor.getElement().removeAttribute("hidden");
	}

	void openOrderDetails(Order order, boolean isReview) {
		openedOrderDetails.getElement().removeAttribute("hidden");
		openedOrderEditor.getElement().setAttribute("hidden", "");
		openedOrderDetails.display(order, isReview);
	}

	boolean isDesktopView() {
		return getElement().getProperty("desktopView", true);
	}

	void resizeGrid() {
		grid.getElement().callFunction("notifyResize");
	}

	BakerySearch getSearchBar() {
		return searchBar;
	}

	OrderEditor getOpenedOrderEditor() {
		return openedOrderEditor;
	}

	OrderDetailsFull getOpenedOrderDetails() {
		return openedOrderDetails;
	}

}
