package com.vaadin.starter.bakery.ui;

import com.vaadin.starter.bakery.AbstractIT;
import com.vaadin.starter.bakery.By;
import com.vaadin.testbench.TestBenchElement;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class UsersViewIT extends AbstractIT {

	private static final String MODIFY_LOCKED_USER_NOT_PERMITTED = "User has been locked and cannot be modified or deleted";

	private UsersViewElement openTestPage() {
		openLoginView().login("admin@vaadin.com", "admin");
		WebElement usersNavLink = findElement(By.shadowSelector("bakery-app::shadow bakery-navigation::shadow a[href='users']"));
		usersNavLink.click();
		return ((TestBenchElement) findElement(By.tagName("bakery-users"))).wrap(UsersViewElement.class);
	}

	@Test
	public void updatePassword() {
		UsersViewElement page = openTestPage();

		WebElement editor = page.getEditorDialog();
		Assert.assertFalse(editor.isDisplayed());

		WebElement bakerCell = page.getGridCell("baker@vaadin.com");
		Assert.assertNotNull(bakerCell);
		
		bakerCell.click();
		Assert.assertTrue(editor.isDisplayed());

		WebElement password = page.getPasswordField();
		Assert.assertNull(password.getAttribute("value"));

		password.sendKeys("foobar");
		page.getUpdateButton().click();
		Assert.assertFalse(editor.isDisplayed());

		bakerCell.click();
		Assert.assertNull(password.getAttribute("value"));
	}

	@Test
	public void tryToUpdateLockedEntity() {
		UsersViewElement page = openTestPage();

		page.getGridCell("barista@vaadin.com").click();

		WebElement field = page.getFirstTextField();
		field.sendKeys("-updated");
		page.getUpdateButton().click();

		WebDriverWait wait = new WebDriverWait(getDriver(), 10);
		Alert alert = wait.until(ExpectedConditions.alertIsPresent());

		Assert.assertNotNull(alert);
		Assert.assertEquals(MODIFY_LOCKED_USER_NOT_PERMITTED, alert.getText());
	}

	@Test
	public void tryToDeleteLockedEntity() {
		UsersViewElement page = openTestPage();

		page.getGridCell("barista@vaadin.com").click();

		page.getDeleteButton().click();

		WebElement dialogElement = findElements(By.tagName("confirm-dialog"))
				.stream().filter(WebElement::isDisplayed).findFirst().get();
		Assert.assertNotNull(dialogElement);
		ConfirmDialogElement dialog = ((TestBenchElement) dialogElement).wrap(ConfirmDialogElement.class);
		dialog.confirm();

		WebDriverWait wait = new WebDriverWait(getDriver(), 10);
		Alert alert = wait.until(ExpectedConditions.alertIsPresent());

		Assert.assertNotNull(alert);
		Assert.assertEquals(MODIFY_LOCKED_USER_NOT_PERMITTED, alert.getText());
	}
}
