package com.vaadin.starter.bakery.testbench.elements.ui;

import com.vaadin.flow.component.button.testbench.ButtonElement;
import com.vaadin.flow.component.textfield.testbench.PasswordFieldElement;
import com.vaadin.flow.component.textfield.testbench.TextFieldElement;
import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.elementsbase.Element;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.function.Function;

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
		doUntilSuccessful(driver -> {
			getSignIn().click();
			return true;
		});
	}

	private PasswordFieldElement getPassword() {
		return $(PasswordFieldElement.class).waitForFirst();
	}

	public void setPassword(String password) {
		doUntilSuccessful(driver -> {
			getPassword().setValue(password);
			return true;
		});
	}

	private TextFieldElement getUsername() {
		return $(TextFieldElement.class).waitForFirst();
	}

	public void setUsername(String username) {
		doUntilSuccessful(driver -> {
			getUsername().setValue(username);
			return true;
		});
	}

	public String getUsernameLabel() {
		return getUsername().getLabel();
	}

	private void doUntilSuccessful(Function<WebDriver, Boolean> function) {
		new WebDriverWait(getDriver(), 10)
				.ignoring(StaleElementReferenceException.class)
				.until(function::apply);
	}
}