package com.vaadin.starter.bakery.ui.components;

import com.vaadin.starter.bakery.ui.event.ValidationFailedEvent;
import com.vaadin.starter.bakery.ui.form.EditForm;
import com.vaadin.ui.event.ComponentEventListener;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.vaadin.data.BeanValidationBinder;
import com.vaadin.data.ValidationException;
import com.vaadin.flow.model.TemplateModel;
import com.vaadin.shared.Registration;
import com.vaadin.starter.bakery.backend.data.Role;
import com.vaadin.starter.bakery.backend.data.entity.User;
import com.vaadin.starter.bakery.ui.event.CancelEvent;
import com.vaadin.starter.bakery.ui.event.DeleteEvent;
import com.vaadin.starter.bakery.ui.event.SaveEvent;
import com.vaadin.ui.Tag;
import com.vaadin.ui.combobox.ComboBox;
import com.vaadin.ui.common.HtmlImport;
import com.vaadin.ui.passwordfield.PasswordField;
import com.vaadin.ui.polymertemplate.Id;
import com.vaadin.ui.polymertemplate.PolymerTemplate;
import com.vaadin.ui.textfield.TextField;

@Tag("user-edit")
@HtmlImport("context://src/users/user-edit.html")
public class UserEdit extends PolymerTemplate<UserEdit.Model> implements EntityEditView<User> {

	public interface Model extends TemplateModel {
		void setAvatar(String avatar);

		void setUserRole(String userRole);
	}

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

	@Id("user-edit-form")
	private EditForm editForm;

	private final BeanValidationBinder<User> binder = new BeanValidationBinder<>(User.class);

	private PasswordEncoder passwordEncoder = NoOpPasswordEncoder.getInstance();

	private User user;

	private boolean isDirty = false;

	public UserEdit() {
		editForm.init(binder, "User");
		roleField.setItems(Role.getAllRoles());
		binder.bind(firstnameField, "firstName");
		binder.bind(lastnameField, "lastName");
		binder.bind(emailField, "email");
		binder.bind(passwordField, (user) -> passwordField.getEmptyValue(), (user, password) -> {
			if (!passwordField.getEmptyValue().equals(password)) {
				user.setPassword(passwordEncoder.encode(password));
			}
		});
		roleField.addValueChangeListener(e -> {
			setIsDirty(user == null || user.getRole() == null || !user.getRole().equals(e.getValue()));
		});
	}

	private void setIsDirty(boolean isDirty) {
		this.isDirty = isDirty;
		editForm.setIsDirty(isDirty());
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

	public void read(User user) {
		this.user = user;
		binder.readBean(user);
		getModel().setAvatar(user.getPhotoUrl());
		getModel().setUserRole(user.getRole());
		isDirty = false;
		editForm.showEditor(user.isNew());
	}

	public void clear() {
		getModel().setUserRole("");
	}

	public boolean isDirty() {
		return binder.hasChanges() || isDirty;
	}

	@Override
	public void write(User entity) throws ValidationException {
		binder.writeBean(entity);
		entity.setRole(roleField.getValue());
	}

	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

}
