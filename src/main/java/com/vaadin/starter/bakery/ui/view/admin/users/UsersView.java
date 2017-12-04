package com.vaadin.starter.bakery.ui.view.admin.users;

import static com.vaadin.starter.bakery.ui.utils.BakeryConst.PAGE_USERS;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.vaadin.data.BeanValidationBinder;
import com.vaadin.data.ValidationException;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.flow.model.TemplateModel;
import com.vaadin.generated.starter.elements.GeneratedStarterButtonsBar;
import com.vaadin.generated.starter.elements.GeneratedStarterDialog;
import com.vaadin.generated.starter.elements.GeneratedStarterSearchBar;
import com.vaadin.router.PageTitle;
import com.vaadin.router.Route;
import com.vaadin.starter.bakery.backend.data.Role;
import com.vaadin.starter.bakery.backend.data.entity.User;
import com.vaadin.starter.bakery.backend.service.UserService;
import com.vaadin.starter.bakery.ui.BakeryApp;
import com.vaadin.starter.bakery.ui.utils.BakeryConst;
import com.vaadin.starter.bakery.ui.view.admin.DefaultEntityPresenter;
import com.vaadin.starter.bakery.ui.view.admin.EntityEditor;
import com.vaadin.starter.bakery.ui.view.crud.CrudView;
import com.vaadin.ui.Tag;
import com.vaadin.ui.combobox.ComboBox;
import com.vaadin.ui.common.HasText;
import com.vaadin.ui.common.HtmlImport;
import com.vaadin.ui.grid.Grid;
import com.vaadin.ui.html.H3;
import com.vaadin.ui.polymertemplate.Id;
import com.vaadin.ui.textfield.PasswordField;
import com.vaadin.ui.textfield.TextField;

@Tag("bakery-users")
@HtmlImport("src/users/bakery-users.html")
@Route(value = PAGE_USERS, layout = BakeryApp.class)
@PageTitle(BakeryConst.TITLE_USERS)
@Secured(Role.ADMIN)
public class UsersView extends CrudView<User, TemplateModel> {

    @Id("search-bar")
    private GeneratedStarterSearchBar searchbar;

	@Id("users-grid")
	private Grid<User> grid;
	
	@Id("dialog-editor")
	private GeneratedStarterDialog dialog;
	
    @Id("buttons")
    private GeneratedStarterButtonsBar buttons;	
	
    @Id("title")
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
    

    
    private DefaultEntityPresenter<User> presenter;

    private final BeanValidationBinder<User> binder = new BeanValidationBinder<>(User.class);

    private PasswordEncoder passwordEncoder = NoOpPasswordEncoder.getInstance();

	@Autowired
	public UsersView(UserService userService) {
		presenter = new DefaultEntityPresenter<>(userService, this, getEntityName());
		setupEventListeners();
		
		setupGrid();
		
        ListDataProvider<String> roleProvider = DataProvider.ofItems(Role.getAllRoles());
        roleField.setItemLabelGenerator(s -> s != null  ? s : "");
        roleField.setDataProvider(roleProvider);
        binder.bind(firstnameField, "firstName");
        binder.bind(lastnameField, "lastName");
        binder.bind(emailField, "email");
        binder.bind(roleField, "role");
        binder.bind(passwordField, (user) -> passwordField.getEmptyValue(), (user, password) -> {
            if (!passwordField.getEmptyValue().equals(password)) {
                user.setPassword(passwordEncoder.encode(password));
            }
        });		
	}

	private void setupGrid() {
		final Grid.Column<User> emailColumn = grid.addColumn(User::getEmail).setWidth("270px").setHeader("Email").setFlexGrow(5);
		final Grid.Column<User> userColumn = grid.addColumn(u -> u.getFirstName() + " " + u.getLastName()).setHeader("Name").setWidth("200px").setFlexGrow(5);
		grid.addColumn(User::getRole).setHeader("Role").setWidth("150px");

		grid.getElement().addEventListener("animationend", e -> {
			emailColumn.setWidth("270px").setFlexGrow(5);
			userColumn.setWidth("200px").setFlexGrow(5);
		});
	}

	@Override
	public Grid<User> getGrid() {
		return grid;
	}

	@Override
	protected DefaultEntityPresenter<User> getPresenter() {
		return presenter;
	}

	@Override
	protected String getBasePage() {
		return PAGE_USERS;
	}

	@Override
	protected EntityEditor<User> getEditor() {
		return this;
	}

    @Override
    public boolean isDirty() {
        return binder.hasChanges();
    }

    @Override
    public void write(User entity) throws ValidationException {
        binder.writeBean(entity);
    }

    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public GeneratedStarterSearchBar getSearchBar() {
        return searchbar;
    }

    @Override
    protected GeneratedStarterButtonsBar getButtonsBar() {
        return buttons;
    }

    @Override
    protected GeneratedStarterDialog getDialog() {
        return dialog;
    }

    @Override
    protected BeanValidationBinder<User> getBinder() {
        return binder;
    }
    
    @Override
    protected HasText getTitle() {
        return title;
    }

    @Override
    protected String getEntityName() {
        return "User";
    }
}
