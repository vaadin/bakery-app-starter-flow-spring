package com.vaadin.starter.bakery.ui.views.admin.users;

import static com.vaadin.starter.bakery.ui.utils.BakeryConst.PAGE_USERS;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.templatemodel.TemplateModel;
import com.vaadin.starter.bakery.backend.data.Role;
import com.vaadin.starter.bakery.backend.data.entity.User;
import com.vaadin.starter.bakery.backend.data.entity.util.EntityUtil;
import com.vaadin.starter.bakery.ui.MainView;
import com.vaadin.starter.bakery.ui.components.SearchBar;
import com.vaadin.starter.bakery.ui.crud.CrudView;
import com.vaadin.starter.bakery.ui.crud.DefaultEntityPresenter;
import com.vaadin.starter.bakery.ui.utils.BakeryConst;

@Tag("users-view")
@HtmlImport("src/views/admin/users/users-view.html")
@Route(value = PAGE_USERS, layout = MainView.class)
@PageTitle(BakeryConst.TITLE_USERS)
@Secured(Role.ADMIN)
public class UsersView extends CrudView<User, TemplateModel> {

	@Id("searchBar")
	private SearchBar searchbar;

	@Id("grid")
	private Grid<User> grid;

	private Dialog dialog = new Dialog();

	private UserForm form = new UserForm();

	private DefaultEntityPresenter<User> presenter;

	private final BeanValidationBinder<User> binder = new BeanValidationBinder<>(User.class);

	@Autowired
	public UsersView(DefaultEntityPresenter<User> presenter, PasswordEncoder passwordEncoder, User currentUser) {
		super(EntityUtil.getName(User.class));
		this.presenter = presenter;

		form.setPasswordEncoder(passwordEncoder);
		form.setBinder(binder);
		dialog.add(form);

		setupEventListeners();
		setupGrid();
		presenter.init(this);
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
	protected DefaultEntityPresenter<User> getPresenter() {
		return presenter;
	}

	@Override
	protected String getBasePage() {
		return PAGE_USERS;
	}

	@Override
	public SearchBar getSearchBar() {
		return searchbar;
	}

	@Override
	protected Dialog getDialog() {
		return dialog;
	}

	@Override
	protected BeanValidationBinder<User> getBinder() {
		return binder;
	}

	@Override
	protected CrudForm<User> getForm() {
		return form;
	}
}
