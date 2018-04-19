package com.vaadin.starter.bakery.testbench.elements.ui;

import com.vaadin.flow.component.button.testbench.ButtonElement;
import com.vaadin.flow.component.textfield.testbench.PasswordFieldElement;
import com.vaadin.flow.component.textfield.testbench.TextFieldElement;
import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.elementsbase.Element;

@Element("login-view")
public class LoginViewElement extends TestBenchElement {

	public StorefrontViewElement login(String username, String password) {
		return login(username, password, StorefrontViewElement.class);
	}

	public <E extends TestBenchElement> E login(String username, String password, Class<E> target) {
		setUsername(username);
		setPassword(password);
		signIn();

		return $(target).onPage().waitForFirst();
	}

	private ButtonElement getSignIn() {
		return $(ButtonElement.class).waitForFirst();
	}

	public void signIn() {
		int retries = 0;
		while (retries++ < 5) {
			try {
				getSignIn().click();
				return;
			} catch (Exception ex) {
				System.out.println("Unable to sign in. Retrying due to: " + ex.getMessage());
			}
		}

		System.out.println("Unable to sign in. Gave up.");
	}

	private PasswordFieldElement getPassword() {
		return $(PasswordFieldElement.class).waitForFirst();
	}

	public void setPassword(String password) {
		int retries = 0;
		while (retries++ < 5) {
			try {
				getPassword().setValue(password);
				return;
			} catch (Exception ex) {
				System.out.println("Unable to type password. Retrying due to: " + ex.getMessage());
			}
		}

		System.out.println("Unable to type password. Gave up.");
	}

	private TextFieldElement getUsername() {
		return $(TextFieldElement.class).waitForFirst();
	}

	public void setUsername(String username) {
		int retries = 0;
		while (retries++ < 5) {
			try {
				getUsername().setValue(username);
				return;
			} catch (Exception ex) {
				System.out.println("Unable to type username. Retrying due to: " + ex.getMessage());
			}
		}

		throw new RuntimeException("Unable to type username. Gave up.");
	}

	public String getUsernameLabel() {
		int retries = 0;
		while (retries++ < 5) {
			try {
				return getUsername().getLabel();
			} catch (Exception ex) {
				System.out.println("Unable to get the username label. Retrying due to: " + ex.getMessage());
			}
		}

		throw new RuntimeException("Unable to get the username label. Gave up.");
	}
}