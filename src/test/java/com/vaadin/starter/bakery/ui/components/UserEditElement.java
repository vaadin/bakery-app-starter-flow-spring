package com.vaadin.starter.bakery.ui.components;

import com.vaadin.starter.bakery.elements.PasswordFieldElement;
import com.vaadin.starter.bakery.elements.TextFieldElement;
import com.vaadin.starter.bakery.ui.form.EditFormElement;
import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.elementsbase.Element;

@Element("user-edit")
public class UserEditElement extends TestBenchElement {

	public TextFieldElement getFirstField() {
		return $(TextFieldElement.class).id("first");
	};

	public TextFieldElement getLastField() {
		return $(TextFieldElement.class).id("last");
	}

	public TextFieldElement getEmailField() {
		return $(TextFieldElement.class).id("email");
	}

	public PasswordFieldElement getPasswordField() {
		return $(PasswordFieldElement.class).id("user-edit-password");
	}

	public EditFormElement getEditForm() {
		return $(EditFormElement.class).id("user-edit-form");
	}
}
