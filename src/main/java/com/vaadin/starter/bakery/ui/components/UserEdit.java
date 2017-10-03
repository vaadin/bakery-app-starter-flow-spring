package com.vaadin.starter.bakery.ui.components;

import com.vaadin.annotations.HtmlImport;
import com.vaadin.annotations.Id;
import com.vaadin.annotations.Tag;
import com.vaadin.data.BeanValidationBinder;
import com.vaadin.flow.event.ComponentEventListener;
import com.vaadin.flow.router.View;
import com.vaadin.flow.template.PolymerTemplate;
import com.vaadin.flow.template.model.TemplateModel;
import com.vaadin.shared.Registration;
import com.vaadin.starter.bakery.backend.data.Role;
import com.vaadin.starter.bakery.backend.data.entity.User;
import com.vaadin.starter.bakery.ui.event.CancelEvent;
import com.vaadin.starter.bakery.ui.event.DeleteEvent;
import com.vaadin.starter.bakery.ui.event.SaveEvent;
import com.vaadin.starter.bakery.ui.form.EditForm;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;

@Tag("user-edit")
@HtmlImport("context://src/users/user-edit.html")
public class UserEdit extends PolymerTemplate<UserEdit.Model> implements View {

	public interface Model extends TemplateModel {
		void setAvatar(String avatar);
	}

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

	@Id("user-edit-form")
	private EditForm editForm;

	private User user;
	private BeanValidationBinder<User> binder = new BeanValidationBinder<>(User.class);

	public UserEdit() {
		editForm.init(binder, () -> user, "User");
		roleField.setItems(Role.getAllRoles());
		binder.bind(firstnameField, "firstName");
		binder.bind(lastnameField, "lastName");
		binder.bind(emailField, "email");
		binder.bind(passwordField, (user) -> passwordField.getEmptyValue(), (user, password) -> {
			if (!passwordField.getEmptyValue().equals(password)) {
				user.setPassword(passwordEncoder.encode(password));
			}
		});
		binder.bind(roleField, "role");
	}

	public Registration addSaveListener(ComponentEventListener<SaveEvent> listener) {
		return editForm.addListener(SaveEvent.class, listener);
	}

	public Registration addDeleteListener(ComponentEventListener<DeleteEvent> listener) {
		return editForm.addListener(DeleteEvent.class, listener);
	}

	public Registration addCancelListener(ComponentEventListener<CancelEvent> listener) {
		return editForm.addListener(CancelEvent.class, listener);
	}

	public void setUser(User user) {
		this.user = user;
		binder.readBean(user);
		getModel().setAvatar(user.getPhotoUrl());
		editForm.showEditor(user.isNew());
	}

	public User getUser() {
		return user;
	}

	public boolean isDirty() {
		return binder.hasChanges();
	}
}
