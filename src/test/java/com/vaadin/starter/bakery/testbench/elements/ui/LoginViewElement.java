package com.vaadin.starter.bakery.testbench.elements.ui;

import com.vaadin.starter.bakery.testbench.elements.components.ShadyButtonElement;
import com.vaadin.starter.bakery.testbench.elements.core.ButtonElement;
import com.vaadin.starter.bakery.testbench.elements.core.PasswordFieldElement;
import com.vaadin.starter.bakery.testbench.elements.core.TextFieldElement;
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
		return $(ShadyButtonElement.class).waitForFirst();
	}

	public PasswordFieldElement getPassword() {
		return $(PasswordFieldElement.class).waitForFirst();
	}

	public TextFieldElement getLogin() {
		return $(TextFieldElement.class).waitForFirst();
	}
}