package com.vaadin.starter.bakery.ui.view;

import com.vaadin.starter.bakery.By;
import com.vaadin.testbench.elementsbase.AbstractElement;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


public class LoginViewElement extends AbstractElement {

	public void login(String username, String password) {
		WebElement loginElement = getLogin();
		WebElement passwordElement = getPassword();
		loginElement.clear();
		loginElement.sendKeys(username);
		passwordElement.clear();
		passwordElement.sendKeys(password);

		getSubmit().click();

		waitUntilElementPresent("Login failed", By.tagName("bakery-storefront"));
	}

	protected void waitUntilElementPresent(String errorMessage, org.openqa.selenium.By by) {
		waitUntil(ExpectedConditions.presenceOfElementLocated(by), 30);
	}

	protected <T> void waitUntil(ExpectedCondition<T> condition, long timeoutInSeconds) {
		new WebDriverWait(getDriver(), timeoutInSeconds).until(condition);
	}

	private WebElement getSubmit() {
		return findElement(By.shadowSelector("::shadow #button-submit"));
	}

	private WebElement getPassword() {
		return findElement(By.shadowSelector("::shadow #password"));
	}

	private WebElement getLogin() {
		return findElement(By.shadowSelector("::shadow #login"));
	}

	WebElement getLoginLabel() {
		return findElement(By.shadowSelector("::shadow #login-label"));
	}

}