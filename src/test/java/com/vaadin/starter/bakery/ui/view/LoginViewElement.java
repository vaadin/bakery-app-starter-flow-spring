package com.vaadin.starter.bakery.ui.view;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.vaadin.starter.bakery.By;
import com.vaadin.testbench.elementsbase.AbstractElement;


public class LoginViewElement extends AbstractElement {

	public void login(String username, String password) {
		WebElement loginElement = getLoginInput();
		WebElement passwordElement = getPasswordInput();
		loginElement.clear();
		loginElement.sendKeys(username);
		passwordElement.clear();
		passwordElement.sendKeys(password);

		getSubmit().click();

		waitUntilElementPresent(By.tagName("bakery-storefront"));
	}

	protected void waitUntilElementPresent(org.openqa.selenium.By by) {
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

	private WebElement getPasswordInput() {
		return getPassword().findElement(By.shadowSelector("::shadow #input"));
	}

	private WebElement getLogin() {
		return findElement(By.shadowSelector("::shadow #username"));
	}

	private WebElement getLoginInput() {
		return getLogin().findElement(By.shadowSelector("::shadow #input"));
	}

	String getLoginLabel() {
		return getLogin().getAttribute("label");
	}

}