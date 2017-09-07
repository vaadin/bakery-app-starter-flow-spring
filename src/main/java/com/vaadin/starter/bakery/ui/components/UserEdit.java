package com.vaadin.starter.bakery.ui.components;

import com.vaadin.annotations.HtmlImport;
import com.vaadin.annotations.Id;
import com.vaadin.annotations.Tag;
import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.flow.event.ComponentEventListener;
import com.vaadin.flow.html.H3;
import com.vaadin.flow.router.View;
import com.vaadin.flow.template.PolymerTemplate;
import com.vaadin.flow.template.model.TemplateModel;
import com.vaadin.shared.Registration;
import com.vaadin.starter.bakery.backend.data.Role;
import com.vaadin.starter.bakery.backend.data.entity.User;
import com.vaadin.ui.AttachEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HasClickListeners.ClickEvent;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;

@Tag("user-edit")
@HtmlImport("frontend://src/users/user-edit.html")
public class UserEdit extends PolymerTemplate<UserEdit.Model> implements View {

	public interface Model extends TemplateModel {
		void setShowAvatar(boolean showAvatar);
	}

	// A workaround for a Flow issue (see BFF-244 for details).
	// Instead of adding a slot into the template, and then populating it with a <user-avatar>
	// element created from the server-side at run time, <user-avatar> would be placed
	// directly into the template and referenced with @Id from the server-side.
	private UserAvatar avatar;

	@Id("title")
	private H3 title;

	@Id("first")
	private TextField firstnameField;

	@Id("last")
	private TextField lastnameField;

	@Id("email")
	private TextField emailField;

	@Id("password")
	private PasswordField passwordField;

	@Id("role")
	private ComboBox<String> roleField;

	@Id("save")
	private Button saveButton;

	@Id("delete")
	private Button deleteButton;

	@Id("cancel")
	private Button cancelButton;

	private User user;
	private Binder<User> binder = new Binder<>();

	public UserEdit() {
		avatar = new UserAvatar();
		avatar.getElement().setAttribute("slot", "avatar");
		getElement().appendChild(avatar.getElement());

		roleField.setItems(Role.getAllRoles());

		binder.bind(firstnameField, User::getFirstName, User::setFirstName);
		binder.bind(lastnameField, User::getLastName, User::setLastName);
		binder.bind(emailField, User::getEmail, User::setEmail);
		binder.bind(passwordField,
				(user) -> passwordField.getEmptyValue(),
				(user, password) -> {
					if (!passwordField.getEmptyValue().equals(password)) {
						user.setPassword(password);
					}
				});
		binder.bind(roleField, User::getRole, User::setRole);

		binder.addValueChangeListener(event -> saveButton.setDisabled(!isDirty()));
	}

	public Registration addSaveListener(ComponentEventListener<ClickEvent<Button>> listener) {
		return saveButton.addClickListener(listener);
	}

	public Registration addDeleteListener(ComponentEventListener<ClickEvent<Button>> listener) {
		return deleteButton.addClickListener(listener);
	}

	public Registration addCancelListener(ComponentEventListener<ClickEvent<Button>> listener) {
		return cancelButton.addClickListener(listener);
	}

	public void setUser(User user) {
		this.user = user;
		binder.readBean(user);

		title.setText((user.isNew() ? "New" : "Edit") + " User");
		avatar.setSrc(user.getPhotoUrl());
		getModel().setShowAvatar(user.getPhotoUrl() != null);
		saveButton.setDisabled(true);
		deleteButton.setDisabled(user.isNew());
	}

	public User getUser() {
		return user;
	}

	public void writeEditsToUser() throws ValidationException {
		binder.writeBean(user);
	}

	public boolean isDirty() {
		return binder.hasChanges();
	}
}
