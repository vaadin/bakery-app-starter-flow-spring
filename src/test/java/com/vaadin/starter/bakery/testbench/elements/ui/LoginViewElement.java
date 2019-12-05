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

		ElementQuery<E> eq = $(target).onPage();
		return waitForFirst(eq, target.getSimpleName());
	}

	// a workaround for slow first page load
	// the default waitForFirst() method does not allow setting custom timeout
	private <E extends TestBenchElement> E waitForFirst(ElementQuery<E> eq, String tagname) {
		Object result = new WebDriverWait(getDriver(), 30).until(driver -> {
			try {
				return eq.first();
			} catch (NoSuchElementException e) {
				return null;
			}
		});
		if (result == null) {
			throw new NoSuchElementException("No element for class " + tagname + " found");
		} else {
			return (E) result;
		}
	}

	public String getUsernameLabel() {
		return getUsernameField().getLabel();
	}

}