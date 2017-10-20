package com.vaadin.starter.bakery.ui;

import static com.vaadin.starter.bakery.ui.utils.BakeryConst.PAGE_USERS;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.vaadin.data.ValidationException;
import com.vaadin.flow.model.Convert;
import com.vaadin.flow.model.Include;
import com.vaadin.flow.model.TemplateModel;
import com.vaadin.flow.router.LocationChangeEvent;
import com.vaadin.flow.router.View;
import com.vaadin.hummingbird.ext.spring.annotations.ParentView;
import com.vaadin.hummingbird.ext.spring.annotations.Route;
import com.vaadin.router.Title;
import com.vaadin.shared.Registration;
import com.vaadin.starter.bakery.app.HasLogger;
import com.vaadin.starter.bakery.backend.data.Role;
import com.vaadin.starter.bakery.backend.data.entity.User;
import com.vaadin.starter.bakery.backend.service.UserService;
import com.vaadin.starter.bakery.ui.components.ConfirmationDialog;
import com.vaadin.starter.bakery.ui.components.ItemsView;
import com.vaadin.starter.bakery.ui.components.UserEdit;
import com.vaadin.starter.bakery.ui.converters.LongToStringConverter;
import com.vaadin.starter.bakery.ui.event.CancelEvent;
import com.vaadin.starter.bakery.ui.event.DeleteEvent;
import com.vaadin.starter.bakery.ui.event.EditEvent;
import com.vaadin.starter.bakery.ui.event.SaveEvent;
import com.vaadin.starter.bakery.ui.presenter.Confirmer;
import com.vaadin.starter.bakery.ui.presenter.EntityView;
import com.vaadin.starter.bakery.ui.presenter.EntityViewPresenter;
import com.vaadin.starter.bakery.ui.utils.BakeryConst;
import com.vaadin.starter.bakery.ui.view.DefaultEntityView;
import com.vaadin.ui.Tag;
import com.vaadin.ui.common.HtmlImport;
import com.vaadin.ui.event.ComponentEventListener;
import com.vaadin.ui.polymertemplate.EventHandler;
import com.vaadin.ui.polymertemplate.Id;

@Tag("bakery-users")
@HtmlImport("context://src/users/bakery-users.html")
@Route(PAGE_USERS + "/{id}")
@ParentView(BakeryApp.class)
@Title(BakeryConst.TITLE_USERS)
@Secured(Role.ADMIN)
public class UsersView extends DefaultEntityView<User, UsersView.Model> implements View, HasLogger {

	public interface Model extends TemplateModel {

		@Include({ "id", "firstName", "lastName", "email", "photoUrl", "role" })
		@Convert(value = LongToStringConverter.class, path = "id")
		void setUsers(List<User> users);
	}

	private final UserService userService;

	private UserEdit editor;

	private EntityViewPresenter<User> presenter;

	@Autowired
	public UsersView(UserService userService, PasswordEncoder passwordEncoder) {
		this.userService = userService;

		editor = new UserEdit();
		editor.getElement().setAttribute("slot", "user-editor");
		getElement().appendChild(editor.getElement());

		editor.setPasswordEncoder(passwordEncoder);
		presenter = new EntityViewPresenter<User>(userService, this, "User");
		addListener(EditEvent.class, e -> navigateToEntity(getUI(), PAGE_USERS, e.getId()));

		filterUsers(view.getFilter());

		view.setActionText("New user");
		view.addActionClickListener(e -> presenter.createNew());
		view.addFilterChangeListener(this::filterUsers);
	}

	@Override
	public void onLocationChange(LocationChangeEvent locationChangeEvent) {
		presenter.onLocationChange(locationChangeEvent);
	}

	private void filterUsers(String filter) {
		getModel().setUsers(userService.findAnyMatching(Optional.ofNullable(filter), null).getContent());
	}

	@EventHandler
	private void onCloseDialog() {
		presenter.cancel();
	}

	@Override
	public void closeDialog(boolean updated) {
		view.openDialog(false);
		editor.clear();
		navigateToEntity(getUI(), PAGE_USERS, null);
		if (updated) {
			filterUsers(view.getFilter());
		}
	}

	@Override
	public void openDialog(User user, boolean edit) {
		editor.read(user);
		view.openDialog(true);
	}

	@Override
	public Confirmer getConfirmer() {
		return confirmationDialog;
	}

	@Override
	public boolean isDirty() {
		return editor.isDirty();
	}

	@Override
	public void write(User entity) throws ValidationException {
		editor.write(entity);
	}

	@Override
	public Registration addSaveListener(ComponentEventListener<SaveEvent> listener) {
		return editor.addListener(SaveEvent.class, listener);
	}

	@Override
	public Registration addCancelListener(ComponentEventListener<CancelEvent> listener) {
		return editor.addListener(CancelEvent.class, listener);
	}

	@Override
	public Registration addDeleteListener(ComponentEventListener<DeleteEvent> listener) {
		return editor.addListener(DeleteEvent.class, listener);
	}
}
