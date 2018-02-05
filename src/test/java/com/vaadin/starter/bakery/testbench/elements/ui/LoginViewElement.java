package com.vaadin.starter.bakery.testbench.elements.ui;

import com.vaadin.flow.component.button.testbench.ButtonElement;
import com.vaadin.flow.component.textfield.testbench.PasswordFieldElement;
import com.vaadin.flow.component.textfield.testbench.TextFieldElement;
import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.elementsbase.Element;

@Element("login-view")
public class LoginViewElement extends TestBenchElement {

	public StorefrontViewElement login(String username, String password) {
		getLogin().setValue(username);
		getPassword().setValue(password);
		getSignIn().click();

		return $(StorefrontViewElement.class).onPage().waitForFirst();
	}

	public ButtonElement getSignIn() {
		return $(ButtonElement.class).waitForFirst();
	}

	public PasswordFieldElement getPassword() {
		return $(PasswordFieldElement.class).waitForFirst();
	}

	public TextFieldElement getLogin() {
		return $(TextFieldElement.class).waitForFirst();
	}
}