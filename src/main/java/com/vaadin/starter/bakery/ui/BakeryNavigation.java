package com.vaadin.starter.bakery.ui;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.model.TemplateModel;
import com.vaadin.starter.bakery.app.security.SecurityUtils;
import com.vaadin.starter.bakery.ui.entities.PageInfo;
import com.vaadin.starter.bakery.ui.utils.BakeryConst;
import com.vaadin.starter.bakery.ui.view.admin.products.ProductsView;
import com.vaadin.starter.bakery.ui.view.admin.users.UsersView;
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

	private boolean pagesAdded;

	public interface Model extends TemplateModel {
		void setPages(List<PageInfo> pages);
	}

	public void updateNavigationBar() {
		if (!pagesAdded) {
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
	    pages.add(new PageInfo("foo", ICON_PRODUCTS, "Bar"));
		getModel().setPages(pages);
	}

	@ClientDelegate
	private void navigateTo(String href) {
	    if (href.equals("foo")) {
	        return;
	    }
		UI.getCurrent().navigateTo(href);
	}
}
