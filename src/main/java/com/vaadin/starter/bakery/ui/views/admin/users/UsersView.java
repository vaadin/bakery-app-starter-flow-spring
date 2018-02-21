package com.vaadin.starter.bakery.ui.views.admin.users;

import static com.vaadin.starter.bakery.ui.utils.BakeryConst.PAGE_USERS;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

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

	@Id("search")
	private SearchBar search;

	@Id("grid")
	private Grid<User> grid;

	@Id("dialog")
	private Dialog dialog;

	private final UserForm form;

	private final DefaultEntityPresenter<User> presenter;

	private final BeanValidationBinder<User> binder = new BeanValidationBinder<>(User.class);

	@Autowired
	public UsersView(DefaultEntityPresenter<User> presenter, UserForm form) {
		super(EntityUtil.getName(User.class));
		this.presenter = presenter;
		this.form = form;

		form.setBinder(binder);
		dialog.add(form);

		setupEventListeners();
		setupGrid();
		presenter.init(this);
	}

	private void setupGrid() {
		grid.addColumn(User::getEmail).setWidth("270px").setHeader("Email").setFlexGrow(5);
		grid.addColumn(u -> u.getFirstName() + " " + u.getLastName()).setHeader("Name").setWidth("200px").setFlexGrow(5);
		grid.addColumn(User::getRole).setHeader("Role").setWidth("150px");
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
		return search;
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
