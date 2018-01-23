package com.vaadin.starter.bakery.testbench.elements.ui;

import com.vaadin.starter.bakery.testbench.elements.core.GridElement;
import com.vaadin.starter.bakery.testbench.elements.core.PasswordFieldElement;
import com.vaadin.starter.bakery.testbench.elements.core.TextFieldElement;
import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.elementsbase.Element;

@Element("users-view")
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
