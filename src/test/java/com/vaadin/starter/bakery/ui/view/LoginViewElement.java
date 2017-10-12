package com.vaadin.starter.bakery.ui.view;

import org.openqa.selenium.support.ui.ExpectedConditions;

import com.vaadin.starter.bakery.By;
import com.vaadin.starter.bakery.elements.ButtonElement;
import com.vaadin.starter.bakery.elements.PasswordFieldElement;
import com.vaadin.starter.bakery.elements.TextFieldElement;
import com.vaadin.testbench.TestBenchElement;

public class LoginViewElement extends TestBenchElement {

	public void login(String username, String password) {
		getLogin().setValue(username);
		getPassword().setValue(password);
		getSignIn().click();

		waitUntil(ExpectedConditions.presenceOfElementLocated(By.tagName("bakery-storefront")));
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