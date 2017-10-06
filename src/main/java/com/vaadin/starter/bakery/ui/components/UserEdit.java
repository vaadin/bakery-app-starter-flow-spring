package com.vaadin.starter.bakery.ui.components;

import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.flow.model.TemplateModel;
import com.vaadin.shared.Registration;
import com.vaadin.starter.bakery.backend.data.Role;
import com.vaadin.starter.bakery.backend.data.entity.User;
import com.vaadin.ui.Tag;
import com.vaadin.ui.button.Button;
import com.vaadin.ui.combobox.ComboBox;
import com.vaadin.ui.common.HasClickListeners;
import com.vaadin.ui.common.HtmlImport;
import com.vaadin.ui.event.ComponentEventListener;
import com.vaadin.ui.html.H3;
import com.vaadin.ui.passwordfield.PasswordField;
import com.vaadin.ui.polymertemplate.Id;
import com.vaadin.ui.polymertemplate.PolymerTemplate;
import com.vaadin.ui.textfield.TextField;
import org.springframework.security.crypto.password.PasswordEncoder;

@Tag("user-edit")
@HtmlImport("context://src/users/user-edit.html")
public class UserEdit extends PolymerTemplate<UserEdit.Model> {

	public interface Model extends TemplateModel {
		void setAvatar(String avatar);
	}

	@Id("user-edit-title")
	private H3 title;

	@Id("first")
	private TextField firstnameField;

	@Id("last")
	private TextField lastnameField;

	@Id("email")
	private TextField emailField;

	@Id("user-edit-password")
	private PasswordField passwordField;

	@Id("role")
	private ComboBox<String> roleField;

	@Id("user-edit-save")
	private Button saveButton;

	@Id("user-edit-delete")
	private Button deleteButton;

	@Id("user-edit-cancel")
	private Button cancelButton;

	private User user;
	private Binder<User> binder = new Binder<>();

	public UserEdit() {
		roleField.setItems(Role.getAllRoles());
	}

	public void setupBinding(PasswordEncoder passwordEncoder) {
		binder.bind(firstnameField, User::getFirstName, User::setFirstName);
		binder.bind(lastnameField, User::getLastName, User::setLastName);
		binder.bind(emailField, User::getEmail, User::setEmail);
		binder.bind(passwordField,
				(user) -> passwordField.getEmptyValue(),
				(user, password) -> {
					if (!passwordField.getEmptyValue().equals(password)) {
						user.setPassword(passwordEncoder.encode(password));
					}
				});
		binder.bind(roleField, User::getRole, User::setRole);

		binder.addValueChangeListener(event -> saveButton.setDisabled(!isDirty()));
	}

	public Registration addSaveListener(ComponentEventListener<HasClickListeners.ClickEvent<Button>> listener) {
		return saveButton.addClickListener(listener);
	}

	public Registration addDeleteListener(ComponentEventListener<HasClickListeners.ClickEvent<Button>> listener) {
		return deleteButton.addClickListener(listener);
	}

	public Registration addCancelListener(ComponentEventListener<HasClickListeners.ClickEvent<Button>> listener) {
		return cancelButton.addClickListener(listener);
	}

	public void setUser(User user) {
		this.user = user;
		binder.readBean(user);

		title.setText((user.isNew() ? "New" : "Edit") + " User");
		getModel().setAvatar(user.getPhotoUrl());
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
