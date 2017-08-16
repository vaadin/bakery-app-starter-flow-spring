package com.vaadin.starter.bakery.ui.view;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.vaadin.testbench.TestBenchElement;

public class LoginViewElement extends TestBenchElement {

	public void login(String username, String password) {
		WebElement loginElement = getLogin();
		WebElement passwordElement = getPassword();
		loginElement.clear();
		loginElement.sendKeys(username);
		passwordElement.clear();
		passwordElement.sendKeys(password);

		getSubmit().click();

		waitUntilElementPresent("Login failed", By.tagName("bakery-app"));
	}

	protected void waitUntilElementPresent(String errorMessage, By by) {
		waitUntil(ExpectedConditions.presenceOfElementLocated(by), 30);
	}

	protected <T> void waitUntil(ExpectedCondition<T> condition, long timeoutInSeconds) {
		new WebDriverWait(getDriver(), timeoutInSeconds).until(condition);
	}

	private WebElement getSubmit() {
		return findElement(By.id("button-submit"));
	}

	private WebElement getPassword() {
		return findElement(By.id("password"));
	}

	private WebElement getLogin() {
		return findElement(By.id("login"));
	}

	WebElement getLoginLabel() {
		return findElement(By.id("login-label"));
	}

}