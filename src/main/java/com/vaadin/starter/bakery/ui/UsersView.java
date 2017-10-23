package com.vaadin.starter.bakery.ui;

import static com.vaadin.starter.bakery.ui.utils.BakeryConst.PAGE_USERS;
import static com.vaadin.starter.bakery.ui.utils.TemplateUtil.addToSlot;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.vaadin.flow.model.Convert;
import com.vaadin.flow.model.Include;
import com.vaadin.flow.model.TemplateModel;
import com.vaadin.hummingbird.ext.spring.annotations.ParentView;
import com.vaadin.hummingbird.ext.spring.annotations.Route;
import com.vaadin.router.Title;
import com.vaadin.starter.bakery.backend.data.Role;
import com.vaadin.starter.bakery.backend.data.entity.User;
import com.vaadin.starter.bakery.backend.service.UserService;
import com.vaadin.starter.bakery.ui.components.ConfirmationDialog;
import com.vaadin.starter.bakery.ui.components.ItemsView;
import com.vaadin.starter.bakery.ui.components.UserEdit;
import com.vaadin.starter.bakery.ui.converters.LongToStringConverter;
import com.vaadin.starter.bakery.ui.presenter.DefaultEntityPresenter;
import com.vaadin.starter.bakery.ui.utils.BakeryConst;
import com.vaadin.starter.bakery.ui.view.DefaultEntityView;
import com.vaadin.ui.Tag;
import com.vaadin.ui.common.HtmlImport;
import com.vaadin.ui.polymertemplate.EventHandler;
import com.vaadin.ui.polymertemplate.Id;

@Tag("bakery-users")
@HtmlImport("context://src/users/bakery-users.html")
@Route(PAGE_USERS + "/{id}")
@ParentView(BakeryApp.class)
@Title(BakeryConst.TITLE_USERS)
@Secured(Role.ADMIN)
public class UsersView extends DefaultEntityView<User, UsersView.Model> {

	public interface Model extends TemplateModel {

		@Include({ "id", "firstName", "lastName", "email", "photoUrl", "role" })
		@Convert(value = LongToStringConverter.class, path = "id")
		void setUsers(List<User> users);
	}

	@Id("bakery-users-items-view")
	private ItemsView view;

	private UserEdit editor;

	@Id("user-confirmation-dialog")
	private ConfirmationDialog confirmationDialog;

	private DefaultEntityPresenter<User> presenter;

	@Autowired
	public UsersView(UserService userService, PasswordEncoder passwordEncoder) {
		this.editor = new UserEdit();
		editor.setPasswordEncoder(passwordEncoder);
		addToSlot(this, editor, "user-editor");
		this.presenter = new DefaultEntityPresenter<User>(userService, this, "User");
		setup(PAGE_USERS, presenter, editor, confirmationDialog, view, "New user");
		presenter.init();
	}

	@Override
	public void closeDialog(boolean updated) {
		editor.clear();
		super.closeDialog(updated);
	}

	@Override
	public void list(List<User> entities) {
		getModel().setUsers(entities);
	}

}
