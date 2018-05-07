package com.vaadin.starter.bakery.ui;

import static com.vaadin.starter.bakery.ui.utils.BakeryConst.ICON_DASHBOARD;
import static com.vaadin.starter.bakery.ui.utils.BakeryConst.ICON_LOGOUT;
import static com.vaadin.starter.bakery.ui.utils.BakeryConst.ICON_PRODUCTS;
import static com.vaadin.starter.bakery.ui.utils.BakeryConst.ICON_STOREFRONT;
import static com.vaadin.starter.bakery.ui.utils.BakeryConst.ICON_USERS;
import static com.vaadin.starter.bakery.ui.utils.BakeryConst.PAGE_DASHBOARD;
import static com.vaadin.starter.bakery.ui.utils.BakeryConst.PAGE_DEFAULT;
import static com.vaadin.starter.bakery.ui.utils.BakeryConst.PAGE_LOGOUT;
import static com.vaadin.starter.bakery.ui.utils.BakeryConst.PAGE_PRODUCTS;
import static com.vaadin.starter.bakery.ui.utils.BakeryConst.PAGE_STOREFRONT;
import static com.vaadin.starter.bakery.ui.utils.BakeryConst.PAGE_USERS;
import static com.vaadin.starter.bakery.ui.utils.BakeryConst.TITLE_DASHBOARD;
import static com.vaadin.starter.bakery.ui.utils.BakeryConst.TITLE_LOGOUT;
import static com.vaadin.starter.bakery.ui.utils.BakeryConst.TITLE_PRODUCTS;
import static com.vaadin.starter.bakery.ui.utils.BakeryConst.TITLE_STOREFRONT;
import static com.vaadin.starter.bakery.ui.utils.BakeryConst.TITLE_USERS;
import static com.vaadin.starter.bakery.ui.utils.BakeryConst.VIEWPORT;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.component.HasElement;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.templatemodel.TemplateModel;
import com.vaadin.starter.bakery.app.security.SecurityUtils;
import com.vaadin.starter.bakery.ui.components.AppNavigation;
import com.vaadin.starter.bakery.ui.components.ConfirmDialog;
import com.vaadin.starter.bakery.ui.entities.PageInfo;
import com.vaadin.starter.bakery.ui.exceptions.AccessDeniedException;
import com.vaadin.starter.bakery.ui.views.HasConfirmation;
import com.vaadin.starter.bakery.ui.views.admin.products.ProductsView;
import com.vaadin.starter.bakery.ui.views.admin.users.UsersView;
import org.springframework.beans.factory.annotation.Autowired;

@Tag("main-view")
@HtmlImport("src/main-view.html")

@PageTitle("###Bakery###")
@Viewport(VIEWPORT)
public class MainView extends PolymerTemplate<TemplateModel>
		implements RouterLayout, BeforeEnterObserver {

	@Id("appNavigation")
	private AppNavigation appNavigation;

	private final ConfirmDialog confirmDialog;

	@Autowired
	public MainView(ConfirmDialog confirmDialog) {
		this.confirmDialog = confirmDialog;

		List<PageInfo> pages = new ArrayList<>();

		pages.add(new PageInfo(PAGE_STOREFRONT, ICON_STOREFRONT, TITLE_STOREFRONT));
		pages.add(new PageInfo(PAGE_DASHBOARD, ICON_DASHBOARD, TITLE_DASHBOARD));
		if (SecurityUtils.isAccessGranted(UsersView.class)) {
			pages.add(new PageInfo(PAGE_USERS, ICON_USERS, TITLE_USERS));
		}
		if (SecurityUtils.isAccessGranted(ProductsView.class)) {
			pages.add(new PageInfo(PAGE_PRODUCTS, ICON_PRODUCTS, TITLE_PRODUCTS));
		}
		pages.add(new PageInfo(PAGE_LOGOUT, ICON_LOGOUT, TITLE_LOGOUT));

		appNavigation.init(pages, PAGE_DEFAULT, PAGE_LOGOUT);

		getElement().appendChild(confirmDialog.getElement());
	}

	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		if (!SecurityUtils.isAccessGranted(event.getNavigationTarget())) {
			event.rerouteToError(AccessDeniedException.class);
		}
	}

	@Override
	public void showRouterLayoutContent(HasElement content) {
		if (content != null) {
			getElement().appendChild(content.getElement());
		}

		this.confirmDialog.setOpened(false);
		if (content instanceof HasConfirmation) {
			((HasConfirmation) content).setConfirmDialog(this.confirmDialog);
		}
	}
}
