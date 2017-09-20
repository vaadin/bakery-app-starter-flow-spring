package com.vaadin.starter.bakery.ui;

import static com.vaadin.starter.bakery.backend.service.UserService.MODIFY_LOCKED_USER_NOT_PERMITTED;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.WebElement;

import com.vaadin.starter.bakery.AbstractIT;
import com.vaadin.starter.bakery.By;
import com.vaadin.testbench.TestBenchElement;

public class UsersViewIT extends AbstractIT {

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
		Assert.assertEquals("", password.getAttribute("value"));

		password.sendKeys("foobar");
		page.getUpdateButton().click();
		Assert.assertFalse(editor.isDisplayed());

		bakerCell.click();
		Assert.assertEquals("", password.getAttribute("value"));
	}

	@Test
	public void tryToUpdateLockedEntity() {
		UsersViewElement page = openTestPage();

		page.getGridCell("barista@vaadin.com").click();

		WebElement field = page.getFirstTextField();
		field.sendKeys("-updated");
		page.getUpdateButton().click();

		WebElement toast = findElement(By.id("_persistentToast"));
		Assert.assertEquals(MODIFY_LOCKED_USER_NOT_PERMITTED, toast.getAttribute("text"));
		Assert.assertEquals("paper-toast-open", toast.getAttribute("class"));
	}

	@Test
	public void tryToDeleteLockedEntity() {
		UsersViewElement page = openTestPage();

		page.getGridCell("barista@vaadin.com").click();

		page.getDeleteButton().click();

		WebElement dialogElement = findElement(By.id("user-confirmation-dialog"));
		Assert.assertNotNull(dialogElement);
		ConfirmDialogElement dialog = ((TestBenchElement) dialogElement).wrap(ConfirmDialogElement.class);
		dialog.confirm();

		WebElement toast = findElement(By.id("_persistentToast"));
		Assert.assertEquals(MODIFY_LOCKED_USER_NOT_PERMITTED, toast.getAttribute("text"));
		Assert.assertEquals("paper-toast-open", toast.getAttribute("class"));
	}
}
