package com.vaadin.starter.bakery.ui.view.admin.users;

import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.vaadin.data.BeanValidationBinder;
import com.vaadin.data.ValidationException;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.flow.model.TemplateModel;
import com.vaadin.starter.bakery.backend.data.Role;
import com.vaadin.starter.bakery.backend.data.entity.User;
import com.vaadin.starter.bakery.ui.event.CancelEvent;
import com.vaadin.starter.bakery.ui.event.DeleteEvent;
import com.vaadin.starter.bakery.ui.event.SaveEvent;
import com.vaadin.starter.bakery.ui.view.admin.EditForm;
import com.vaadin.starter.bakery.ui.view.admin.EntityEditor;
import com.vaadin.starter.bakery.ui.view.wrapper.ComboboxBinderWrapper;
import com.vaadin.ui.Tag;
import com.vaadin.ui.combobox.ComboBox;
import com.vaadin.ui.common.HtmlImport;
import com.vaadin.ui.polymertemplate.Id;
import com.vaadin.ui.polymertemplate.PolymerTemplate;
import com.vaadin.ui.textfield.PasswordField;
import com.vaadin.ui.textfield.TextField;

@Tag("user-edit")
@HtmlImport("src/users/user-edit.html")
public class UserEdit extends PolymerTemplate<TemplateModel> implements EntityEditor<User> {

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

	public UserEdit() {
		editForm.init(binder, "User");
		ListDataProvider<String> roleProvider = DataProvider.ofItems(Role.getAllRoles());
		roleField.setItemLabelGenerator(s -> s != null  ? s : "");
		roleField.setDataProvider(roleProvider);
		binder.bind(firstnameField, "firstName");
		binder.bind(lastnameField, "lastName");
		binder.bind(emailField, "email");
		binder.bind(new ComboboxBinderWrapper<>(roleField), "role");
		binder.bind(passwordField, (user) -> passwordField.getEmptyValue(), (user, password) -> {
			if (!passwordField.getEmptyValue().equals(password)) {
				user.setPassword(passwordEncoder.encode(password));
			}
		});

		// Forward these events to the presenter
		editForm.addListener(SaveEvent.class, this::fireEvent);
		editForm.addListener(DeleteEvent.class, this::fireEvent);
		editForm.addListener(CancelEvent.class, this::fireEvent);
	}

	public void read(User user) {
		binder.readBean(user);
		editForm.showEditor(user.isNew());
	}

	public boolean isDirty() {
		return binder.hasChanges();
	}

	public void write(User entity) throws ValidationException {
		binder.writeBean(entity);
	}

	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

}
