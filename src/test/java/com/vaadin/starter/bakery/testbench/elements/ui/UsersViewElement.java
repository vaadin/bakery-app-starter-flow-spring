package com.vaadin.starter.bakery.testbench.elements.ui;

import com.vaadin.flow.component.grid.testbench.GridElement;
import com.vaadin.flow.component.textfield.testbench.PasswordFieldElement;
import com.vaadin.flow.component.textfield.testbench.TextFieldElement;
import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.elementsbase.Element;

@Element("users-view")
public class UsersViewElement extends TestBenchElement implements HasCrudView {

	@Element("user-form")
	public static class UserFormElement extends TestBenchElement {
	}

	@Override
	public GridElement getGrid() {
		return $(GridElement.class).waitForFirst();
	}

	public TextFieldElement getEmailField() {
		return getForm().$(TextFieldElement.class).id("email");
	}

	public PasswordFieldElement getPasswordField() {
		return getForm().$(PasswordFieldElement.class).id("password");
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class<UserFormElement> getFormClass() {
		return UserFormElement.class;
	}
}
