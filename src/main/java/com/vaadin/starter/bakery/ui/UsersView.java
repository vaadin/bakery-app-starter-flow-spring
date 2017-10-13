package com.vaadin.starter.bakery.ui;

import static com.vaadin.starter.bakery.ui.utils.BakeryConst.PAGE_USERS;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.vaadin.flow.model.Convert;
import com.vaadin.flow.model.Include;
import com.vaadin.flow.model.TemplateModel;
import com.vaadin.flow.router.LocationChangeEvent;
import com.vaadin.flow.router.View;
import com.vaadin.hummingbird.ext.spring.annotations.ParentView;
import com.vaadin.hummingbird.ext.spring.annotations.Route;
import com.vaadin.router.Title;
import com.vaadin.starter.bakery.app.HasLogger;
import com.vaadin.starter.bakery.backend.data.Role;
import com.vaadin.starter.bakery.backend.data.entity.User;
import com.vaadin.starter.bakery.backend.service.UserService;
import com.vaadin.starter.bakery.ui.components.ConfirmationDialog;
import com.vaadin.starter.bakery.ui.components.ItemsView;
import com.vaadin.starter.bakery.ui.components.UserEdit;
import com.vaadin.starter.bakery.ui.converters.LongToStringConverter;
import com.vaadin.starter.bakery.ui.presenter.Confirmer;
import com.vaadin.starter.bakery.ui.presenter.EntityEditPresenter;
import com.vaadin.starter.bakery.ui.presenter.EntityView;
import com.vaadin.starter.bakery.ui.utils.BakeryConst;
import com.vaadin.starter.bakery.ui.utils.TemplateUtil;
import com.vaadin.ui.Tag;
import com.vaadin.ui.common.HtmlImport;
import com.vaadin.ui.polymertemplate.EventHandler;
import com.vaadin.ui.polymertemplate.Id;
import com.vaadin.ui.polymertemplate.PolymerTemplate;

@Tag("bakery-users")
@HtmlImport("context://src/users/bakery-users.html")
@Route(PAGE_USERS + "/{id}")
@ParentView(BakeryApp.class)
@Title(BakeryConst.TITLE_USERS)
@Secured(Role.ADMIN)
public class UsersView extends PolymerTemplate<UsersView.Model> implements View, HasLogger, EntityView<User> {

	public interface Model extends TemplateModel {

		@Include({ "id", "firstName", "lastName", "email", "photoUrl", "role" })
		@Convert(value = LongToStringConverter.class, path = "id")
		void setUsers(List<User> users);
	}

	private final UserService userService;

	@Id("bakery-users-items-view")
	private ItemsView view;

	@Id("user-editor")
	private UserEdit editor;

	@Id("user-confirmation-dialog")
	private ConfirmationDialog confirmationDialog;

	private EntityEditPresenter<User> presenter;

	@Autowired
	public UsersView(UserService userService, PasswordEncoder passwordEncoder) {
		this.userService = userService;
		editor.setPasswordEncoder(passwordEncoder);
		presenter = new EntityEditPresenter<User>(userService, editor, this, "User");
		getElement().addEventListener("edit",
				e -> navigateToEntity(getUI(), PAGE_USERS, e.getEventData().getString("event.detail")), "event.detail");

		filterUsers(view.getFilter());

		view.setActionText("New user");
		view.addActionClickListener(e -> presenter.createNew(new User()));
		view.addFilterChangeListener(this::filterUsers);
	}

	@Override
	public void onLocationChange(LocationChangeEvent locationChangeEvent) {
		TemplateUtil.handleEntityNavigation(presenter, locationChangeEvent, true);
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
}
