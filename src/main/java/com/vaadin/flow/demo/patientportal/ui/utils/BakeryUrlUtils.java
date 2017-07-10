package com.vaadin.flow.demo.patientportal.ui.utils;

import com.vaadin.flow.demo.patientportal.ui.AdminView;
import com.vaadin.flow.demo.patientportal.ui.DashboardView;
import com.vaadin.flow.demo.patientportal.ui.ProductsView;
import com.vaadin.flow.demo.patientportal.ui.StorefrontView;
import com.vaadin.flow.demo.patientportal.ui.UsersView;
import com.vaadin.flow.router.View;

public class BakeryUrlUtils {

	public static final String DEFAULT_PAGE = BakeryConst.PAGE_STOREFRONT;

	public static String getUrlByView(View view) {
		if (view instanceof AdminView)
			return BakeryConst.PAGE_ADMIN;

		if (view instanceof DashboardView)
			return BakeryConst.PAGE_DASHBOARD;

		if (view instanceof UsersView)
			return BakeryConst.PAGE_USERS;

		if (view instanceof StorefrontView)
			return BakeryConst.PAGE_STOREFRONT;

		if (view instanceof ProductsView)
			return BakeryConst.PAGE_PRODUCTS;

		return BakeryConst.PAGE_NOTFOUND;

	}
}
