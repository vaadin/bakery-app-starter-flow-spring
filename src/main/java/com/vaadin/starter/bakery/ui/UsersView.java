package com.vaadin.starter.bakery.ui;

import com.vaadin.flow.model.Convert;
import com.vaadin.flow.model.Include;
import com.vaadin.flow.model.TemplateModel;
import com.vaadin.flow.router.LocationChangeEvent;
import com.vaadin.flow.router.View;
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
import com.vaadin.ui.Tag;
import com.vaadin.ui.button.Button;
import com.vaadin.ui.common.HasClickListeners;
import com.vaadin.ui.common.HtmlImport;
import com.vaadin.ui.polymertemplate.EventHandler;
import com.vaadin.ui.polymertemplate.Id;
import com.vaadin.ui.polymertemplate.PolymerTemplate;
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
		editor.addSaveListener(this::saveUser);
		editor.addDeleteListener(this::onBeforeDelete);
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

	private void createNewUser(HasClickListeners.ClickEvent<Button> newUserEvent) {
		editor.setUser(new User());
		view.openDialog(true);
	}

	private void onBeforeDelete(HasClickListeners.ClickEvent<Button> deleteEvent) {
		confirmationDialog.show(CONFIRM_CAPTION_DELETE_USER, CONFIRM_MESSAGE_DELETE, CONFIRM_OKBUTTON_DELETE,
				CONFIRM_CANCELBUTTON_DELETE, this::deleteUser, null);
	}

	private void deleteUser(HasClickListeners.ClickEvent<Button> confirmOkDeleteEvent) {
		try {
			userService.delete(editor.getUser().getId());
			navigateToUser(null);
		} catch (UserFriendlyDataException e) {
			// Commit failed because of application-level data constraints
			toast(e.getMessage(), true);
			getLogger().debug("User-friendly data exception while deleting entity of type "
					+ com.vaadin.starter.bakery.backend.data.entity.User.class.getName(), e);
		} catch (DataIntegrityViolationException e) {
			// Commit failed because of validation errors
			toast("The given entity cannot be deleted as there are references to it in the database", true);
			getLogger().error("Data integrity violation error while deleting entity of type "
					+ com.vaadin.starter.bakery.backend.data.entity.User.class.getName(), e);
		} catch (OptimisticLockingFailureException e) {
			// Somebody else probably edited the data at the same time
			toast("Somebody else might have updated the data. Please refresh and try again.", true);
			getLogger().debug("Optimistic locking error while deleting entity of type "
					+ com.vaadin.starter.bakery.backend.data.entity.User.class.getName(), e);
		} catch (Exception e) {
			// Something went wrong, no idea what
			toast("A problem occurred while deleting the data. Please refresh and try again.", true);
			getLogger().error("Unable to delete entity of type "
					+ com.vaadin.starter.bakery.backend.data.entity.User.class.getName(), e);
		} finally {
			filterUsers(view.getFilter());
		}
	}

	private void saveUser(HasClickListeners.ClickEvent<Button> saveEvent) {
		try {
			editor.writeEditsToUser();
			userService.save(editor.getUser());
			navigateToUser(null);
		} catch (DataIntegrityViolationException e) {
			// Commit failed because of validation errors
			toast(e.getMessage(), true);
			getLogger().debug("Data integrity violation error while updating entity of type "
					+ com.vaadin.starter.bakery.backend.data.entity.User.class.getName(), e);
		} catch (OptimisticLockingFailureException e) {
			// Somebody else probably edited the data at the same time
			toast("Somebody else might have updated the data. Please refresh and try again.", true);
			getLogger().debug("Optimistic locking error while saving entity of type "
					+ com.vaadin.starter.bakery.backend.data.entity.User.class.getName(), e);
		} catch (Exception e) {
			// Something went wrong, no idea what
			toast("A problem occurred while saving the data. Please check the fields.", true);
			getLogger().error("Unable to save entity of type "
					+ com.vaadin.starter.bakery.backend.data.entity.User.class.getName(), e);
		} finally {
			filterUsers(view.getFilter());
		}
	}

	@EventHandler
	private void onBeforeClose() {
		if (editor.isDirty()) {
			confirmationDialog.show(CONFIRM_CAPTION_CANCEL, CONFIRM_MESSAGE_CANCEL_USER, CONFIRM_OKBUTTON_CANCEL,
					CONFIRM_CANCELBUTTON_CANCEL, okButtonEvent -> navigateToUser(null), null);
		} else {
			navigateToUser(null);
		}
	}
}
