package com.vaadin.starter.bakery.ui.components;

import com.vaadin.annotations.HtmlImport;
import com.vaadin.annotations.Id;
import com.vaadin.annotations.Tag;
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

import java.util.stream.Stream;

@Tag("user-edit")
@HtmlImport("frontend://src/users/user-edit.html")
public class UserEdit extends PolymerTemplate<UserEdit.Model> implements View {

	public interface Model extends TemplateModel {
		void setShowAvatar(boolean showAvatar);
	}

	// TODO(vlukashov): refactor when https://github.com/vaadin/flow/issues/2233 is fixed
	// Instead of adding a slot into the template, and then populating it with a <user-avatar>
	// element created from the server-side at run time, <user-avatar> would be placed
	// directly into the template and referenced with @Id from the server-side.
	private UserAvatar avatar;

	@Id("title")
	private H3 title;

	@Id("first")
	private TextField nameField;

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

	public UserEdit() {
		nameField.addValueChangeListener(valueChangeEvent -> saveButton.setDisabled(!isDirty()));
		setUser(new User());
	}

	@Override
	protected void onAttach(AttachEvent attachEvent) {
		super.onAttach(attachEvent);
		if (attachEvent.isInitialAttach()) {
			roleField.setItems(Role.getAllRoles());

			Stream.of(nameField, lastnameField, emailField, passwordField, roleField)
					.forEach(field -> field.addValueChangeListener(
							event -> saveButton.setDisabled(!isDirty())));
		}
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

		if (avatar == null) {
			avatar = new UserAvatar();
			avatar.getElement().setAttribute("slot", "avatar");
			getElement().appendChild(avatar.getElement());
		}
		avatar.setSrc(user.getPhotoUrl());
		getModel().setShowAvatar(user.getPhotoUrl() != null && !user.getPhotoUrl().isEmpty());

		nameField.setValue(user.getFirstName());
		lastnameField.setValue(user.getLastName());
		emailField.setValue(user.getEmail());
		passwordField.clear();
		roleField.setValue(user.getRole());
		deleteButton.setDisabled(user.getId() == null);
		title.setText((user.getId() == null ? "New" : "Edit") + " User");
	}

	public User getUser() {
		if (user != null) {
			user.setFirstName(nameField.getValue());
			user.setLastName(lastnameField.getValue());
			user.setEmail(emailField.getValue());
			if (!passwordField.isEmpty()) {
				user.setPassword(passwordField.getValue());
			}
			user.setPassword(passwordField.getValue());
			user.setRole(roleField.getValue());
		}

		return user;
	}

	public boolean isDirty() {
		if (user != null && user.getId() != null) {
			return !user.getFirstName().equals(nameField.getValue())
					|| !user.getLastName().equals(lastnameField.getValue())
					|| !user.getEmail().equals(emailField.getValue())
					|| (!passwordField.isEmpty() && !user.getPassword().equals(passwordField.getValue()))
					|| !user.getRole().equals(roleField.getValue());
		}

		return Stream.of(nameField, lastnameField, emailField, passwordField, roleField)
				.anyMatch(field -> field.getValue() != null && !field.getValue().trim().isEmpty());
	}
}
