package com.vaadin.starter.bakery.ui.views.admin.users;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.vaadin.flow.component.HasText;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.templatemodel.TemplateModel;
import com.vaadin.starter.bakery.backend.data.Role;
import com.vaadin.starter.bakery.backend.data.entity.User;
import com.vaadin.starter.bakery.ui.components.ComboBoxForBinder;
import com.vaadin.starter.bakery.ui.components.FormButtonsBar;
import com.vaadin.starter.bakery.ui.crud.CrudView.CrudForm;

@Tag("user-form")
@HtmlImport("src/views/admin/users/user-form.html")
public class UserForm extends PolymerTemplate<TemplateModel> implements CrudForm<User> {
	
	@Id("title")
	private H3 title;	

	@Id("buttons")
	private FormButtonsBar buttons;

	@Id("first")
	private TextField firstnameField;

	@Id("last")
	private TextField lastnameField;

	@Id("email")
	private TextField emailField;

	@Id("user-edit-password")
	private PasswordField passwordField;

	@Id("role")
	private ComboBoxForBinder<String> roleField;

	private PasswordEncoder passwordEncoder;

	void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public void setBinder(BeanValidationBinder<User> binder) {
		ListDataProvider<String> roleProvider = DataProvider.ofItems(Role.getAllRoles());
		roleField.setItemLabelGenerator(s -> s != null ? s : "");
		roleField.setDataProvider(roleProvider);
		
		binder.bind(firstnameField, "firstName");
		binder.bind(lastnameField, "lastName");
		binder.bind(emailField, "email");
		binder.bind(roleField, "role");

		binder.forField(passwordField).withValidator(password -> {
			return password.matches("^(|(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,})$");
		}, "need 6 or more chars, mixing digits, lowercase and uppercase letters")
				.bind(user -> passwordField.getEmptyValue(), (user, password) -> {
					if (!passwordField.getEmptyValue().equals(password)) {
						// user.
						// user.setPassword(""); //
						// passwordEncoder.encode(password));
					}
				});
	}

	@Override
	public FormButtonsBar getButtons() {
		return buttons;
	}

	@Override
	public HasText getTitle() {
		return title;
	}

}
