package com.vaadin.starter.bakery.ui.views.storefront;

import java.util.Collections;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.selection.SelectionEvent;
import com.vaadin.flow.renderer.ComponentTemplateRenderer;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.BeforeLeaveEvent;
import com.vaadin.flow.router.BeforeLeaveObserver;
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
public class StorefrontView extends PolymerTemplate<TemplateModel>
		implements HasLogger, HasUrlParameter<Long>, EntityView<Order> {

	@Id("search")
	private SearchBar searchBar;

	@Id("grid")
	private Grid<Order> grid;

	@Id("dialog")
	private Dialog dialog;

	private final OrderEditor orderEditor;

	private final OrderDetailsFull orderDetails = new OrderDetailsFull();

	private final OrderPresenter presenter;

	@Autowired
	public StorefrontView(OrderPresenter presenter, OrderEditor orderEditor) {
		this.presenter = presenter;
		this.orderEditor = orderEditor;

		searchBar.setActionText("New order");
		searchBar.setCheckboxText("Show past orders");
		searchBar.setPlaceHolder("Search");

		dialog.add(orderEditor);
		dialog.add(orderDetails);

		grid.setSelectionMode(Grid.SelectionMode.SINGLE);
		grid.addColumn(new ComponentTemplateRenderer<>(order -> {
			OrderCard orderCard = new OrderCard();
			orderCard.setOrder(order);
			orderCard.setHeader(presenter.getHeaderByOrderId(order.getId()));
			orderCard.getElement().getClassList().add(BakeryConst.STOREFRONT_ORDER_CARD_STYLE);
			return orderCard;
		}));
		setOpened(false);
		grid.addSelectionListener(this::onOrdersGridSelectionChanged);
		getSearchBar().addFilterChangeListener(
				e -> presenter.filterChanged(getSearchBar().getFilter(), getSearchBar().isCheckboxChecked()));
		getSearchBar().addActionClickListener(e -> presenter.createNewOrder());

		presenter.init(this);

		dialog.getElement().addEventListener("opened-changed", e -> {
			if (!dialog.isOpened()) {
				// Handle client-side closing dialog on escape
				presenter.cancel();
			}
		});
	}

	void setOpened(boolean opened) {
		if (opened) {
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

	@Override
	public void clear() {
		orderEditor.clear();
	}

	private void onOrdersGridSelectionChanged(SelectionEvent<Order> e) {
		e.getFirstSelectedItem().ifPresent(order -> {
			getUI().ifPresent(ui -> ui.navigateTo(BakeryConst.PAGE_STOREFRONT + "/" + order.getId()));
			getGrid().deselect(order);
		});
	}
}
