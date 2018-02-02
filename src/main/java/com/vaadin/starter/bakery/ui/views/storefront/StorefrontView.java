package com.vaadin.starter.bakery.ui.views.storefront;

import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSingleSelectionModel;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.selection.SingleSelectionListener;
import com.vaadin.flow.renderer.ComponentTemplateRenderer;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.templatemodel.TemplateModel;
import com.vaadin.starter.bakery.app.HasLogger;
import com.vaadin.starter.bakery.backend.data.entity.Order;
import com.vaadin.starter.bakery.ui.MainView;
import com.vaadin.starter.bakery.ui.components.SearchBar;
import com.vaadin.starter.bakery.ui.utils.BakeryConst;
import com.vaadin.starter.bakery.ui.utils.TemplateUtil;
import com.vaadin.starter.bakery.ui.views.EntityView;

@Tag("storefront-view")
@HtmlImport("src/views/storefront/storefront-view.html")
@Route(value = BakeryConst.PAGE_STOREFRONT, layout = MainView.class)
@RouteAlias(value = BakeryConst.PAGE_ROOT, layout = MainView.class)
@PageTitle(BakeryConst.TITLE_STOREFRONT)
public class StorefrontView extends PolymerTemplate<StorefrontView.Model>
		implements HasLogger, HasUrlParameter<Long>, EntityView<Order> {

	public interface Model extends TemplateModel {
	}

	@Id("search")
	private SearchBar searchBar;

	@Id("grid")
	private Grid<Order> grid;

	@Id("dialog")
	private Dialog dialog;

	private OrderEditor orderEditor;

	private OrderDetailsFull orderDetails;

	private OrderPresenter presenter;

	@Autowired
	public StorefrontView(OrderPresenter presenter, PickupLocationDataProvider locationProvider,
			ProductDataProvider productDataProvider) {

		this.presenter = presenter;
		// required for the `isDesktopView()` method
		getElement().synchronizeProperty("desktopView", "desktop-view-changed");

		searchBar.setActionText("New order");
		searchBar.setCheckboxText("Show past orders");
		searchBar.setPlaceHolder("Search");

		orderEditor = new OrderEditor(locationProvider, productDataProvider);
		orderDetails = new OrderDetailsFull();
		dialog.add(orderEditor);
		dialog.add(orderDetails);
		UI.getCurrent().getPage().executeJavaScript("$0.$.overlay.setAttribute('theme', 'middle')", dialog);

		grid.setSelectionMode(Grid.SelectionMode.SINGLE);
		Map<Order, OrderCard> components = new WeakHashMap<>();
		grid.addColumn(new ComponentTemplateRenderer<>(order -> {
			OrderCard orderCard = new OrderCard();
			orderCard.setOrder(order);
			orderCard.setHeader(presenter.getHeaderByOrderId(order.getId()));
			components.put(order, orderCard);
			return orderCard;
		}));
		SingleSelectionListener<Grid<Order>, Order> listener = e -> {
			if (e.getOldValue() != null) {
				OrderCard card = components.get(e.getOldValue());
				if (card != null) {
					presenter.onOrderCardCollapsed(card);
				}
			}
			if (e.getValue() != null) {
				OrderCard card = components.get(e.getValue());
				if (card != null) {
					presenter.onOrderCardExpanded(card);
				}
			}
		};
		((GridSingleSelectionModel<Order>) grid.getSelectionModel()).addSingleSelectionListener(listener);
		setEditing(false);
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
		getOpenedOrderDetails().addBackListener(e -> presenter.back());
		getOpenedOrderDetails().addEditListener(e -> presenter.edit());
		getOpenedOrderDetails().addCommentListener(e -> presenter.addComment(e.getMessage()));
	}

	void setEditing(boolean editing) {
		if (editing) {
			dialog.open();
		} else {
			dialog.close();
		}
	}

	@Override
	public void setParameter(BeforeEvent event, @OptionalParameter Long orderId) {
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
		return orderEditor.hasChanges();
	}

	@Override
	public void write(Order entity) throws ValidationException {
		orderEditor.write(entity);
	}

	public Stream<HasValue<?, ?>> validate() {
		return orderEditor.validate();
	}

	boolean isDesktopView() {
		return getElement().getProperty("desktopView", true);
	}

	void resizeGrid() {
		grid.getElement().callFunction("notifyResize");
	}

	SearchBar getSearchBar() {
		return searchBar;
	}

	OrderEditor getOpenedOrderEditor() {
		return orderEditor;
	}

	OrderDetailsFull getOpenedOrderDetails() {
		return orderDetails;
	}

	Grid<Order> getGrid() {
		return grid;
	}

}
