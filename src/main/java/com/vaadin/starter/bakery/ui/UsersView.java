package com.vaadin.starter.bakery.ui;

import static com.vaadin.starter.bakery.ui.utils.BakeryConst.PAGE_USERS;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.vaadin.annotations.Convert;
import com.vaadin.annotations.EventHandler;
import com.vaadin.annotations.HtmlImport;
import com.vaadin.annotations.Id;
import com.vaadin.annotations.Include;
import com.vaadin.annotations.Tag;
import com.vaadin.annotations.Title;
import com.vaadin.flow.router.LocationChangeEvent;
import com.vaadin.flow.router.View;
import com.vaadin.flow.template.PolymerTemplate;
import com.vaadin.flow.template.model.TemplateModel;
import com.vaadin.hummingbird.ext.spring.annotations.ParentView;
import com.vaadin.hummingbird.ext.spring.annotations.Route;
import com.vaadin.starter.bakery.app.HasLogger;
import com.vaadin.starter.bakery.backend.data.Role;
import com.vaadin.starter.bakery.backend.data.entity.User;
import com.vaadin.starter.bakery.backend.service.UserFriendlyDataException;
import com.vaadin.starter.bakery.backend.service.UserService;
import com.vaadin.starter.bakery.ui.components.ConfirmationDialog;
import com.vaadin.starter.bakery.ui.components.EntityView;
import com.vaadin.starter.bakery.ui.components.ItemsView;
import com.vaadin.starter.bakery.ui.components.UserEdit;
import com.vaadin.starter.bakery.ui.converters.LongToStringConverter;
import com.vaadin.starter.bakery.ui.messages.Message;
import com.vaadin.starter.bakery.ui.presenter.EntityEditPresenter;
import com.vaadin.starter.bakery.ui.utils.BakeryConst;

@Tag("bakery-users")
@HtmlImport("context://src/users/bakery-users.html")
@Route(PAGE_USERS + "/{id}")
@ParentView(BakeryApp.class)
@Title(BakeryConst.TITLE_USERS)
@Secured(Role.ADMIN)
public class UsersView extends PolymerTemplate<UsersView.Model> implements View, HasToast, HasLogger {

	public interface Model extends TemplateModel {

		@Include({ "id", "firstName", "lastName", "email", "photoUrl", "role" })
		@Convert(value = LongToStringConverter.class, path = "id")
		void setUsers(List<User> users);
	}

	private final UserService userService;

	@Id("view")
	private ItemsView view;

	@Id("user-editor")
	private UserEdit editor;

	@Id("user-confirmation-dialog")
	private ConfirmationDialog confirmationDialog;

	private EntityEditPresenter<User> presenter;

	@Autowired
	public UsersView(UserService userService) {
		this.userService = userService;
		editor.setPasswordEncoder(passwordEncoder);
		presenter = new EntityEditPresenter<User>(userService, editor, this, "User");
		getElement().addEventListener("edit", e -> navigateToUser(e.getEventData().getString("event.detail")),
				"event.detail");

		filterUsers(view.getFilter());

		view.setActionText("New user");
		view.addActionClickListener(e -> presenter.createNew(new User()));
		view.addFilterChangeListener(this::filterUsers);
	}

	@Override
	public void onLocationChange(LocationChangeEvent locationChangeEvent) {
		setEditableUser(locationChangeEvent.getPathParameter("id"));
	}

	private void setEditableUser(String userId) {
		if (userId == null || userId.isEmpty()) {
			view.openDialog(false);
			return;
		}

		try {
			presenter.edit(Long.parseLong(userId));
		} catch (NumberFormatException e) {
			toast("Invalid id", false);
			getLogger().error("Expected to get a numeric user id, but got: " + userId, e);
			view.openDialog(false);
		}
	}

	private void navigateToUser(String id) {
		final String location = PAGE_USERS + (id == null || id.isEmpty() ? "" : "/" + id);
		getUI().ifPresent(ui -> ui.navigateTo(location));
	}

	private void filterUsers(String filter) {
		getModel().setUsers(userService.findAnyMatching(Optional.ofNullable(filter), null).getContent());
	}

	@EventHandler
	private void onBeforeClose() {
		presenter.cancel();
	}

	@Override
	public void closeDialog(boolean updated) {
		view.openDialog(false);
		if (updated) {
			filterUsers(view.getFilter());
		}
	}

	@Override
	public void confirm(Message message, Runnable operation) {
		confirmationDialog.show(message, ev -> operation.run());
	}

	@Override
	public void openDialog() {
		view.openDialog(true);
	}
}
