package com.vaadin.starter.bakery.ui;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.annotations.ClientDelegate;
import com.vaadin.annotations.HtmlImport;
import com.vaadin.annotations.Tag;
import com.vaadin.flow.router.View;
import com.vaadin.flow.template.PolymerTemplate;
import com.vaadin.flow.template.model.TemplateModel;
import com.vaadin.hummingbird.ext.spring.annotations.UIScope;
import com.vaadin.starter.bakery.app.BeanLocator;
import com.vaadin.starter.bakery.app.security.SecuredViewAccessControl;
import com.vaadin.starter.bakery.app.security.SecurityUtils;
import com.vaadin.starter.bakery.ui.dataproviders.UserDataProvider;
import com.vaadin.starter.bakery.ui.entities.PageInfo;
import com.vaadin.starter.bakery.ui.entities.User;
import com.vaadin.starter.bakery.ui.utils.BakeryConst;
import com.vaadin.ui.AttachEvent;
import com.vaadin.ui.History;
import com.vaadin.ui.UI;

@UIScope
@Tag("bakery-navigation")
@HtmlImport("frontend://src/app/bakery-navigation.html")
public class BakeryNavigation extends PolymerTemplate<BakeryNavigation.Model> implements View {
	public static class UserModel {
		private String name;
		private String image;
		private boolean alarms;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getImage() {
			return image;
		}

		public void setImage(String image) {
			this.image = image;
		}

		public boolean isAlarms() {
			return alarms;
		}

		public void setAlarms(boolean alarms) {
			this.alarms = alarms;
		}
	}

	public interface Model extends TemplateModel {
		void setUser(UserModel user);

		void setPages(List<PageInfo> pages);
	}

	private UserDataProvider usersProvider;

	private UserDataProvider getUsersProvider() {
		if (usersProvider == null) {
			usersProvider = BeanLocator.find(UserDataProvider.class);
		}
		return usersProvider;
	}

	@Override
	protected void onAttach(AttachEvent attachEvent) {
		if (SecurityUtils.isUserLoggedIn()) {
			setupNavigationButtons();
			User uiUser = getUsersProvider().getCurrentUser();
			UserModel user = new UserModel();
			user.setName(uiUser.getName());
			user.setImage(uiUser.getPicture());
			user.setAlarms(true);
			getModel().setUser(user);
		}else {
			UserModel user = new UserModel();
			user.setName("Anonymous");
			user.setAlarms(true);
			getModel().setUser(user);
			
		}
	}

	private void setupNavigationButtons() {
		List<PageInfo> pages = new ArrayList<>();

		pages.add(new PageInfo(BakeryConst.PAGE_STOREFRONT, BakeryConst.ICON_STOREFRONT, BakeryConst.TITLE_STOREFRONT));
		pages.add(new PageInfo(BakeryConst.PAGE_DASHBOARD, BakeryConst.ICON_DASHBOARD, BakeryConst.TITLE_DASHBOARD));

		if (SecuredViewAccessControl.isAccessGranted(UsersView.class)) {
			pages.add(new PageInfo(BakeryConst.PAGE_USERS, BakeryConst.ICON_USERS, BakeryConst.TITLE_USERS));
		}
		if (SecuredViewAccessControl.isAccessGranted(ProductsView.class)) {
			pages.add(new PageInfo(BakeryConst.PAGE_PRODUCTS, BakeryConst.ICON_PRODUCTS, BakeryConst.TITLE_PRODUCTS));
		}

		getModel().setPages(pages);
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
