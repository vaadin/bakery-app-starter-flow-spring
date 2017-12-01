package com.vaadin.starter.bakery.ui.view;

import com.vaadin.starter.bakery.elements.ButtonElement;
import com.vaadin.starter.bakery.elements.PasswordFieldElement;
import com.vaadin.starter.bakery.elements.TextFieldElement;
import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.elementsbase.Element;

@Element("bakery-login")
public class LoginViewElement extends TestBenchElement {

	public StorefrontViewElement login(String username, String password) {
		getLogin().setValue(username);
		getPassword().setValue(password);
		getSignIn().click();

		return $(StorefrontViewElement.class).onPage().waitForFirst();
	}

	public ButtonElement getSignIn() {
		return $(ShadyButtonElement.class).id("button-submit");
	}

	public PasswordFieldElement getPassword() {
		return $(PasswordFieldElement.class).id("password");
	}

	public TextFieldElement getLogin() {
		return $(TextFieldElement.class).id("username");
	}
}