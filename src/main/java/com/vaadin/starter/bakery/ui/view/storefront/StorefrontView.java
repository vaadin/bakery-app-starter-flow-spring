package com.vaadin.starter.bakery.ui.view.storefront;

import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.data.ValidationException;
import com.vaadin.data.selection.SingleSelectionListener;
import com.vaadin.flow.model.TemplateModel;
import com.vaadin.router.HasUrlParameter;
import com.vaadin.router.OptionalParameter;
import com.vaadin.router.PageTitle;
import com.vaadin.router.QueryParameters;
import com.vaadin.router.Route;
import com.vaadin.router.RouteAlias;
import com.vaadin.router.event.BeforeNavigationEvent;
import com.vaadin.starter.bakery.app.HasLogger;
import com.vaadin.starter.bakery.backend.data.entity.Order;
import com.vaadin.starter.bakery.ui.BakeryApp;
import com.vaadin.starter.bakery.ui.components.BakerySearch;
import com.vaadin.starter.bakery.ui.utils.BakeryConst;
import com.vaadin.starter.bakery.ui.utils.TemplateUtil;
import com.vaadin.starter.bakery.ui.view.EntityView;
import com.vaadin.ui.Tag;
import com.vaadin.ui.common.HasValue;
import com.vaadin.ui.common.HtmlImport;
import com.vaadin.ui.grid.Grid;
import com.vaadin.ui.grid.GridSingleSelectionModel;
import com.vaadin.ui.polymertemplate.Id;
import com.vaadin.ui.polymertemplate.PolymerTemplate;
import com.vaadin.ui.renderers.ComponentTemplateRenderer;

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

	private OrderPresenter presenter;

	@Autowired
	public StorefrontView(OrderPresenter presenter) {

		this.presenter = presenter;
		// required for the `isDesktopView()` method
		getElement().synchronizeProperty("desktopView", "desktop-view-changed");

		searchBar.setActionText("New order");
		searchBar.setCheckboxText("Show past orders");
		searchBar.setPlaceHolder("Search");

		grid.setSelectionMode(Grid.SelectionMode.SINGLE);
		Map<Order, StorefrontOrderCard> components = new WeakHashMap<>();
		grid.addColumn(new ComponentTemplateRenderer<>(order -> {
			StorefrontOrderCard orderCard = new StorefrontOrderCard();
			orderCard.setOrder(order);
			orderCard.setHeader(presenter.getHeaderByOrderId(order.getId()));
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
		getSearchBar().addFilterChangeListener(
				e -> presenter.filterChanged(getSearchBar().getFilter(), getSearchBar().isCheckboxChecked()));
		getSearchBar().addActionClickListener(e -> presenter.createNewOrder());

		presenter.init(this);
	}

	void setupSingleOrderListeners(SingleOrderPresenter presenter) {
		getOpenedOrderEditor().addCancelListener(e -> presenter.cancel());
		getOpenedOrderEditor().addReviewListener(e -> presenter.review());

		getOpenedOrderDetails().addSaveListenter(e -> presenter.save());
		getOpenedOrderDetails().addCancelListener(e -> presenter.cancel());
		getOpenedOrderDetails().addBackListener(e -> presenter.edit());
		getOpenedOrderDetails().addEditListener(e -> presenter.edit());
		getOpenedOrderDetails().addCommentListener(e -> presenter.addComment(e.getMessage()));
	}

	void setEditing(boolean editing) {
		getModel().setEditing(editing);
	}

	@Override
	public void setParameter(BeforeNavigationEvent event, @OptionalParameter Long orderId) {
		if (orderId != null) {
			boolean editView = event.getLocation().getQueryParameters().getParameters().containsKey("edit");
			presenter.onNavigation(orderId, editView);
		}
	}

	void navigateToEntity(String id, boolean edit) {
		final String page = TemplateUtil.generateLocation(BakeryConst.PAGE_STOREFRONT, id);
		final QueryParameters parameters = edit ? QueryParameters.simple(Collections.singletonMap("edit", ""))
				: QueryParameters.empty();
		getUI().ifPresent(ui -> ui.navigateTo(page, parameters));
	}

	void navigateToMainView() {
		getUI().ifPresent(ui -> ui.navigateTo(BakeryConst.PAGE_STOREFRONT));
	}

	@Override
	public boolean isDirty() {
		return openedOrderEditor.hasChanges();
	}

	@Override
	public void write(Order entity) throws ValidationException {
		openedOrderEditor.write(entity);
	}

	public Stream<HasValue<?, ?>> validate() {
		return openedOrderEditor.validate();
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

	Grid<Order> getGrid() {
		return grid;
	}

}
