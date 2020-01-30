package com.vaadin.starter.bakery.testbench.elements.ui;

import com.vaadin.flow.component.login.testbench.LoginOverlayElement;
import com.vaadin.testbench.ElementQuery;
import com.vaadin.testbench.TestBenchElement;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginViewElement extends LoginOverlayElement {

	public StorefrontViewElement login(String username, String password) {
		return login(username, password, StorefrontViewElement.class);
	}

	public <E extends TestBenchElement> E login(
		String username, String password, Class<E> target) {

		getUsernameField().setValue(username);
		getPasswordField().setValue(password);
		getSubmitButton().click();

		return $(target).onPage().waitForFirst();
	}

	public String getUsernameLabel() {
		return getUsernameField().getLabel();
	}

}