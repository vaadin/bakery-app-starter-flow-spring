package com.vaadin.starter.bakery.testbench.elements.ui;

import com.vaadin.flow.component.login.testbench.LoginElement;
import com.vaadin.flow.component.login.testbench.LoginOverlayElement;
import com.vaadin.flow.component.orderedlayout.testbench.VerticalLayoutElement;
import com.vaadin.flow.component.textfield.testbench.TextFieldElement;
import com.vaadin.testbench.TestBenchElement;

public class LoginViewElement extends VerticalLayoutElement {

	public StorefrontViewElement login(String username, String password) {
		return login(username, password, StorefrontViewElement.class);
	}

	public <E extends TestBenchElement> E login(
		String username, String password, Class<E> target) {

		final LoginElement loginElement = getLoginElement();
		loginElement.getUsernameField().setValue(username);
		loginElement.getPasswordField().setValue(password);
		loginElement.getSubmitButton().click();

		return $(target).onPage().waitForFirst();
	}

	public String getUsernameLabel() {
		return getLoginElement().getUsernameField().getLabel();
	}

	private LoginElement getLoginElement() {
		return $(LoginOverlayElement.class).waitForFirst().getLogin();
	}

}
