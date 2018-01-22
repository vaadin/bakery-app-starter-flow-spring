package com.vaadin.starter.bakery.ui.view;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.component.ClientDelegate;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.templatemodel.TemplateModel;
import com.vaadin.starter.bakery.app.security.SecurityUtils;
import com.vaadin.starter.bakery.ui.entities.PageInfo;
import com.vaadin.starter.bakery.ui.utils.BakeryConst;
import com.vaadin.starter.bakery.ui.view.admin.products.ProductsView;
import com.vaadin.starter.bakery.ui.view.admin.users.UsersView;

@Tag("bakery-navigation")
@HtmlImport("src/view/bakery-navigation.html")
public class BakeryNavigation extends PolymerTemplate<BakeryNavigation.Model> implements AfterNavigationObserver {

	private static final String ICON_STOREFRONT = "edit";
	private static final String ICON_DASHBOARD = "clock";
	private static final String ICON_USERS = "user";
	private static final String ICON_PRODUCTS = "calendar";
	private static final String ICON_LOGOUT = "arrow-right";
	private final List<PageInfo> pages = new ArrayList<>();

	public interface Model extends TemplateModel {
		void setPageNumber(int page);
		void setPages(List<PageInfo> pages);
	}

	public BakeryNavigation() {
		pages.add(new PageInfo(BakeryConst.PAGE_STOREFRONT, ICON_STOREFRONT, BakeryConst.TITLE_STOREFRONT));
		pages.add(new PageInfo(BakeryConst.PAGE_DASHBOARD, ICON_DASHBOARD, BakeryConst.TITLE_DASHBOARD));

		if (SecurityUtils.isAccessGranted(UsersView.class)) {
			pages.add(new PageInfo(BakeryConst.PAGE_USERS, ICON_USERS, BakeryConst.TITLE_USERS));
		}
		if (SecurityUtils.isAccessGranted(ProductsView.class)) {
			pages.add(new PageInfo(BakeryConst.PAGE_PRODUCTS, ICON_PRODUCTS, BakeryConst.TITLE_PRODUCTS));
		}

		pages.add(new PageInfo(BakeryConst.PAGE_LOGOUT, ICON_LOGOUT, BakeryConst.TITLE_LOGOUT));

		getModel().setPages(pages);
	}

	@ClientDelegate
	private void navigateTo(String href) {
		if (BakeryConst.PAGE_LOGOUT.equals(href)) {
			// The logout button is a 'normal' URL, not Flow-managed but
			// handled by Spring Security.
			UI.getCurrent().getPage().executeJavaScript("location.assign('logout')");
		} else {
			UI.getCurrent().navigateTo(href);
		}
	}

	@Override
	public void afterNavigation(AfterNavigationEvent event) {
		String currentPath = event.getLocation().getFirstSegment().isEmpty() ? BakeryConst.PAGE_DEFAULT
				: event.getLocation().getFirstSegment();

		for (int i = 0; i < pages.size(); i++) {
			if (pages.get(i).getLink().equals(currentPath)) {
				this.getModel().setPageNumber(i);
			}
		}
	}
}
