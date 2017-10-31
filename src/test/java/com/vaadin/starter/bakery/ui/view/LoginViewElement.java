package com.vaadin.starter.bakery.ui.view;

import com.vaadin.starter.bakery.elements.ButtonElement;
import com.vaadin.starter.bakery.elements.PasswordFieldElement;
import com.vaadin.starter.bakery.elements.TextFieldElement;
import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.elementsbase.Element;

@Element("bakery-login")
public class LoginViewElement extends TestBenchElement {

	public StoreFrontViewElement login(String username, String password) {
		getLogin().setValue(username);
		getPassword().setValue(password);

		// Workaround for https://github.com/webcomponents/shadydom/issues/141
		executeScript("arguments[0].click()", getSignIn());

		return $(StoreFrontViewElement.class).onPage().waitForFirst();
	}

	public ButtonElement getSignIn() {
		return $(ButtonElement.class).id("button-submit");
	}

	public PasswordFieldElement getPassword() {
		return $(PasswordFieldElement.class).id("password");
	}

	public TextFieldElement getLogin() {
		return $(TextFieldElement.class).id("username");
	}
}