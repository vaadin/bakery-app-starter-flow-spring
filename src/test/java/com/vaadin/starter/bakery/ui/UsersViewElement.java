package com.vaadin.starter.bakery.ui;

import com.vaadin.starter.bakery.elements.GridElement;
import com.vaadin.starter.bakery.elements.PasswordFieldElement;
import com.vaadin.starter.bakery.elements.TextFieldElement;
import com.vaadin.starter.bakery.ui.view.HasCrudView;
import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.elementsbase.Element;

@Element("bakery-users")
public class UsersViewElement extends TestBenchElement implements HasCrudView {

	@Override
	public GridElement getGrid() {
		return $(GridElement.class).id("users-grid");
	}

	public TextFieldElement getFirstField() {

		return getField("first", TextFieldElement.class);
	};

	public TextFieldElement getLastField() {
		return getField("last", TextFieldElement.class);
	}

	public TextFieldElement getEmailField() {
		return getField("email", TextFieldElement.class);
	}

	public PasswordFieldElement getPasswordField() {
		return getField("user-edit-password", PasswordFieldElement.class);
	}
}
