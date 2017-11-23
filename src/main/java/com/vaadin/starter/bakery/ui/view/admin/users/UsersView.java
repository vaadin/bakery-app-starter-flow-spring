package com.vaadin.starter.bakery.ui.view.admin.users;

import static com.vaadin.starter.bakery.ui.utils.BakeryConst.PAGE_USERS;
import static com.vaadin.starter.bakery.ui.utils.TemplateUtil.addToSlot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.vaadin.flow.model.TemplateModel;
import com.vaadin.router.PageTitle;
import com.vaadin.router.Route;
import com.vaadin.starter.bakery.backend.data.Role;
import com.vaadin.starter.bakery.backend.data.entity.User;
import com.vaadin.starter.bakery.backend.service.UserService;
import com.vaadin.starter.bakery.ui.BakeryApp;
import com.vaadin.starter.bakery.ui.utils.BakeryConst;
import com.vaadin.starter.bakery.ui.view.admin.DefaultEntityPresenter;
import com.vaadin.starter.bakery.ui.view.admin.EntityEditor;
import com.vaadin.starter.bakery.ui.view.admin.ItemsView;
import com.vaadin.starter.bakery.ui.view.admin.PolymerEntityView;
import com.vaadin.ui.Tag;
import com.vaadin.ui.common.HtmlImport;
import com.vaadin.ui.grid.Grid;
import com.vaadin.ui.polymertemplate.Id;

@Tag("bakery-users")
@HtmlImport("src/users/bakery-users.html")
@Route(value = PAGE_USERS, layout = BakeryApp.class)
@PageTitle(BakeryConst.TITLE_USERS)
@Secured(Role.ADMIN)
public class UsersView extends PolymerEntityView<User, TemplateModel> {

	@Id("bakery-users-items-view")
	private ItemsView view;

	private UserEdit editor;

	@Id("users-grid")
	private Grid<User> grid;

	private DefaultEntityPresenter<User> presenter;

	@Autowired
	public UsersView(UserService userService, PasswordEncoder passwordEncoder) {
		editor = new UserEdit();
		editor.setPasswordEncoder(passwordEncoder);
		addToSlot(this, editor, "user-editor");
		presenter = new DefaultEntityPresenter<>(userService, this, "User");
		setupEventListeners();
		view.setActionText("New user");
		setupGrid();
	}

	private void setupGrid() {
		final Grid.Column<User> emailColumn = grid.addColumn(User::getEmail).setWidth("270px").setHeader("Email").setFlexGrow(5);
		final Grid.Column<User> userColumn = grid.addColumn(u -> u.getFirstName() + " " + u.getLastName()).setHeader("Name").setWidth("200px").setFlexGrow(5);
		grid.addColumn(User::getRole).setHeader("Role").setWidth("150px");

		grid.getElement().addEventListener("animationend", e -> {
			emailColumn.setWidth("270px").setFlexGrow(5);
			userColumn.setWidth("200px").setFlexGrow(5);
		});
	}

	@Override
	public Grid<User> getGrid() {
		return grid;
	}

	@Override
	public void closeDialog() {
		editor.clear();
		super.closeDialog();
	}

	@Override
	protected DefaultEntityPresenter<User> getPresenter() {
		return presenter;
	}

	@Override
	protected String getBasePage() {
		return PAGE_USERS;
	}

	@Override
	protected EntityEditor<User> getEditor() {
		return editor;
	}

	@Override
	protected ItemsView getItemsView() {
		return view;
	}

}
