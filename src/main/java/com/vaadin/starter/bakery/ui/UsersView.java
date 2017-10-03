package com.vaadin.starter.bakery.ui;

import com.vaadin.annotations.Convert;
import com.vaadin.annotations.EventHandler;
import com.vaadin.annotations.HtmlImport;
import com.vaadin.annotations.Id;
import com.vaadin.annotations.Include;
import com.vaadin.annotations.Tag;
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
import com.vaadin.starter.bakery.ui.components.ItemsView;
import com.vaadin.starter.bakery.ui.components.UserEdit;
import com.vaadin.starter.bakery.ui.converters.LongToStringConverter;
import com.vaadin.starter.bakery.ui.form.EditFormUtil;
import com.vaadin.ui.Button;
import com.vaadin.ui.HasClickListeners.ClickEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.security.access.annotation.Secured;

import java.util.List;
import java.util.Optional;

import static com.vaadin.starter.bakery.ui.utils.BakeryConst.CONFIRM_CANCELBUTTON_CANCEL;
import static com.vaadin.starter.bakery.ui.utils.BakeryConst.CONFIRM_CANCELBUTTON_DELETE;
import static com.vaadin.starter.bakery.ui.utils.BakeryConst.CONFIRM_CAPTION_CANCEL;
import static com.vaadin.starter.bakery.ui.utils.BakeryConst.CONFIRM_CAPTION_DELETE_USER;
import static com.vaadin.starter.bakery.ui.utils.BakeryConst.CONFIRM_MESSAGE_CANCEL_USER;
import static com.vaadin.starter.bakery.ui.utils.BakeryConst.CONFIRM_MESSAGE_DELETE;
import static com.vaadin.starter.bakery.ui.utils.BakeryConst.CONFIRM_OKBUTTON_CANCEL;
import static com.vaadin.starter.bakery.ui.utils.BakeryConst.CONFIRM_OKBUTTON_DELETE;
import static com.vaadin.starter.bakery.ui.utils.BakeryConst.PAGE_USERS;

@Tag("bakery-users")
@HtmlImport("context://src/users/bakery-users.html")
@Route(PAGE_USERS + "/{id}")
@ParentView(BakeryApp.class)
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

	@Autowired
	public UsersView(UserService userService) {
		this.userService = userService;
		initUserEdit();
		getElement().addEventListener("edit", e -> navigateToUser(e.getEventData().getString("event.detail")),
				"event.detail");

		filterUsers(view.getFilter());

		view.setActionText("New user");
		view.addActionClickListener(this::createNewUser);
		view.addFilterChangeListener(this::filterUsers);
	}

	@Override
	public void onLocationChange(LocationChangeEvent locationChangeEvent) {
		setEditableUser(locationChangeEvent.getPathParameter("id"));
	}

	private void initUserEdit() {
		editor.addSaveListener(e -> saveUser());
		editor.addDeleteListener(e -> deleteUser());
		editor.addCancelListener(cancelClickEvent -> onBeforeClose());
	}

	private void setEditableUser(String userId) {
		if (userId == null || userId.isEmpty()) {
			view.openDialog(false);
			return;
		}

		String errorMessage = "Cannot find a user with the id '" + userId + "'. Please refresh the page and try again.";
		try {
			Long longId = Long.parseLong(userId);
			User user = userService.getRepository().findOne(longId);
			if (user == null) {
				toast(errorMessage, false);
				getLogger().error(errorMessage);
				return;
			}
			view.openDialog(true);
			editor.setUser(user);
		} catch (NumberFormatException e) {
			toast(errorMessage, false);
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

	private void createNewUser(ClickEvent<Button> newUserEvent) {
		editor.setUser(new User());
		view.openDialog(true);
	}

	private void deleteUser() {
		EditFormUtil.executeJPAOperation(this, () -> {
			userService.delete(editor.getUser().getId());
			navigateToUser(null);
		});
		filterUsers(view.getFilter());
	}

	private void saveUser() {
		EditFormUtil.executeJPAOperation(this, () -> {
			userService.save(editor.getUser());
			navigateToUser(null);
		});
		filterUsers(view.getFilter());
	}

	@EventHandler
	private void onBeforeClose() {
		EditFormUtil.handeCancel(confirmationDialog, "User", editor.isDirty(), () -> navigateToUser(null));
	}
}
