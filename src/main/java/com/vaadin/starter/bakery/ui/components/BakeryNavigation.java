package com.vaadin.starter.bakery.ui.components;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.model.TemplateModel;
import com.vaadin.starter.bakery.app.security.SecurityUtils;
import com.vaadin.starter.bakery.ui.entities.PageInfo;
import com.vaadin.starter.bakery.ui.utils.BakeryConst;
import com.vaadin.starter.bakery.ui.view.admin.ProductsView;
import com.vaadin.starter.bakery.ui.view.admin.UsersView;
import com.vaadin.ui.History;
import com.vaadin.ui.Tag;
import com.vaadin.ui.UI;
import com.vaadin.ui.common.ClientDelegate;
import com.vaadin.ui.common.HtmlImport;
import com.vaadin.ui.polymertemplate.PolymerTemplate;

@Tag("bakery-navigation")
@HtmlImport("src/app/bakery-navigation.html")
public class BakeryNavigation extends PolymerTemplate<BakeryNavigation.Model> {

	private static final String ICON_STOREFRONT = "edit";
	private static final String ICON_DASHBOARD = "clock";
	private static final String ICON_USERS = "user";
	private static final String ICON_PRODUCTS = "calendar";
	private static final String ICON_LOGOUT = "arrow-right";

	private static final String PSEUDO_PAGE_LOGOUT = "logout";

	private boolean pagesAdded;

	public interface Model extends TemplateModel {
		void setPages(List<PageInfo> pages);
	}

	public void updateUser() {
		if (!pagesAdded && SecurityUtils.isUserLoggedIn()) {
			setupNavigationButtons();
			pagesAdded = true;
		}
	}

	private void setupNavigationButtons() {
		List<PageInfo> pages = new ArrayList<>();

		pages.add(new PageInfo(BakeryConst.PAGE_STOREFRONT, ICON_STOREFRONT, BakeryConst.TITLE_STOREFRONT));
		pages.add(new PageInfo(BakeryConst.PAGE_DASHBOARD, ICON_DASHBOARD, BakeryConst.TITLE_DASHBOARD));

		if (SecurityUtils.isAccessGranted(UsersView.class)) {
			pages.add(new PageInfo(BakeryConst.PAGE_USERS, ICON_USERS, BakeryConst.TITLE_USERS));
		}
		if (SecurityUtils.isAccessGranted(ProductsView.class)) {
			pages.add(new PageInfo(BakeryConst.PAGE_PRODUCTS, ICON_PRODUCTS, BakeryConst.TITLE_PRODUCTS));
		}
		pages.add(new PageInfo(PSEUDO_PAGE_LOGOUT, ICON_LOGOUT, "Logout"));
		getModel().setPages(pages);
	}

	@ClientDelegate
	private void navigateTo(String href) {
		if (PSEUDO_PAGE_LOGOUT.equals(href)) {
			logout();
		} else {
			getUI().ifPresent(ui -> ui.navigateTo(href));
		}
	}

	private void logout() {
		UI ui = getUI().get();
		History history = ui.getPage().getHistory();
		ui.getSession().getSession().invalidate();
		// Reload the page after invalidating the session will redirect
		// to login page
		history.go(0);
	}
}
