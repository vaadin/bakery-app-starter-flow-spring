package com.vaadin.starter.bakery.ui.view.admin;

import static com.vaadin.starter.bakery.ui.utils.BakeryConst.PAGE_USERS;
import static com.vaadin.starter.bakery.ui.utils.TemplateUtil.addToSlot;

import com.vaadin.starter.bakery.ui.BakeryApp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.vaadin.flow.model.TemplateModel;
import com.vaadin.router.PageTitle;
import com.vaadin.router.Route;
import com.vaadin.starter.bakery.backend.data.Role;
import com.vaadin.starter.bakery.backend.data.entity.User;
import com.vaadin.starter.bakery.backend.service.UserService;
import com.vaadin.starter.bakery.ui.components.ConfirmationDialog;
import com.vaadin.starter.bakery.ui.components.ItemsView;
import com.vaadin.starter.bakery.ui.components.UserEdit;
import com.vaadin.starter.bakery.ui.utils.BakeryConst;
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

	private Grid<User> grid = new Grid<>();

	@Id("user-confirmation-dialog")
	private ConfirmationDialog confirmationDialog;

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
		grid.setId("grid");
		grid.getElement().setAttribute("theme", "borderless");

		grid.addColumn("Email", User::getEmail).setWidth("270px").setFlexGrow(5);
		grid.addColumn("Name", u -> u.getFirstName() + " " + u.getLastName()).setWidth("200px").setFlexGrow(5);
		grid.addColumn("Role", User::getRole);

		addToSlot(this, grid, "items-grid");
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
	public ConfirmationDialog getConfirmationDialog() {
		return confirmationDialog;
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
