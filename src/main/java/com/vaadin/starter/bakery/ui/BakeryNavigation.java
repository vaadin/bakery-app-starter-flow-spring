package com.vaadin.starter.bakery.ui;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.model.Include;
import com.vaadin.flow.model.TemplateModel;
import com.vaadin.starter.bakery.app.BeanLocator;
import com.vaadin.starter.bakery.app.security.SecurityUtils;
import com.vaadin.starter.bakery.backend.data.entity.User;
import com.vaadin.starter.bakery.backend.service.UserService;
import com.vaadin.starter.bakery.ui.entities.PageInfo;
import com.vaadin.starter.bakery.ui.utils.BakeryConst;
import com.vaadin.starter.bakery.ui.view.admin.products.ProductsView;
import com.vaadin.starter.bakery.ui.view.admin.users.UsersView;
import com.vaadin.ui.History;
import com.vaadin.ui.Tag;
import com.vaadin.ui.UI;
import com.vaadin.ui.common.ClientDelegate;
import com.vaadin.ui.common.HtmlImport;
import com.vaadin.ui.polymertemplate.PolymerTemplate;

@Tag("bakery-navigation")
@HtmlImport("src/app/bakery-navigation.html")
public class BakeryNavigation extends PolymerTemplate<BakeryNavigation.Model> {
	private boolean loggedIn;

	public interface Model extends TemplateModel {
		@Include({ "firstName", "email", "photoUrl" })
		void setUser(User user);

		void setPages(List<PageInfo> pages);
	}

	private UserService userService;

	private UserService getUserService() {
		if (userService == null) {
			userService = BeanLocator.find(UserService.class);
		}
		return userService;
	}

	public void updateUser() {
		if (!loggedIn && SecurityUtils.isUserLoggedIn()) {
			setupNavigationButtons();
			User user = getUserService().getCurrentUser();
			getModel().setUser(user);
			loggedIn = true;
		} else if (!loggedIn) {
			getModel().setUser(null);
		}
	}

	private void setupNavigationButtons() {
		List<PageInfo> pages = new ArrayList<>();

		pages.add(new PageInfo(BakeryConst.PAGE_STOREFRONT, BakeryConst.ICON_STOREFRONT, BakeryConst.TITLE_STOREFRONT));
		pages.add(new PageInfo(BakeryConst.PAGE_DASHBOARD, BakeryConst.ICON_DASHBOARD, BakeryConst.TITLE_DASHBOARD));

		if (SecurityUtils.isAccessGranted(UsersView.class)) {
			pages.add(new PageInfo(BakeryConst.PAGE_USERS, BakeryConst.ICON_USERS, BakeryConst.TITLE_USERS));
		}
		if (SecurityUtils.isAccessGranted(ProductsView.class)) {
			pages.add(new PageInfo(BakeryConst.PAGE_PRODUCTS, BakeryConst.ICON_PRODUCTS, BakeryConst.TITLE_PRODUCTS));
		}

		getModel().setPages(pages);
	}

	@ClientDelegate
	private void navigateTo(String href) {
		if (href != null) {
			getUI().ifPresent(ui -> ui.navigateTo(href));
		}
	}

	@ClientDelegate
	private void logout() {
		UI ui = getUI().get();
		History history = ui.getPage().getHistory();
		ui.getSession().getSession().invalidate();
		// Reload the page after invalidating the session will redirect
		// to login page
		history.go(0);
	}
}
