package com.vaadin.starter.bakery.testbench;

import java.util.Random;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.vaadin.flow.component.tabs.testbench.TabElement;
import com.vaadin.flow.component.textfield.testbench.EmailFieldElement;
import com.vaadin.flow.component.textfield.testbench.PasswordFieldElement;
import com.vaadin.starter.bakery.testbench.elements.ui.StorefrontViewElement;
import com.vaadin.starter.bakery.testbench.elements.ui.UsersViewElement;
import com.vaadin.testbench.BrowserTest;
import com.vaadin.testbench.TestBenchElement;
import org.junit.jupiter.api.parallel.Isolated;

@Isolated
public class UsersViewIT extends AbstractIT<UsersViewElement> {

	private static Random r = new Random();

	@Override
	protected UsersViewElement openView() {
		StorefrontViewElement storefront = openLoginView().login("admin@vaadin.com", "admin");
		return storefront.getMenu().navigateToUsers();
	}

	@BrowserTest
	public void updatePassword() {
		UsersViewElement usersView = openView();

		Assertions.assertFalse(usersView.getCrud().isEditorOpen());

		String uniqueEmail = "e" + r.nextInt() + "@vaadin.com";

		createUser(usersView, uniqueEmail, "Paul", "Irwin", "Vaadin10", "baker");

		int rowNum = usersView.getCrud().getGrid().getCell(uniqueEmail).getRow();
		usersView.openRowForEditing(rowNum);

		Assertions.assertTrue(usersView.getCrud().isEditorOpen());

		Assertions.assertTrue(usersView.getCrud().isEditorOpen());

		// When opening form the password value must be always empty
		PasswordFieldElement password = usersView.getPasswordField();
		Assertions.assertEquals("", password.getValue());

		// Saving any field without changing password should save and close
		EmailFieldElement emailField = usersView.getEmailField();
		String newEmail = "foo" + r.nextInt() + "@bar.com";
		emailField.setValue(newEmail);

		usersView.getCrud().getEditorSaveButton().click();
		Assertions.assertFalse(usersView.getCrud().isEditorOpen());

		// Invalid password prevents closing form
		rowNum = usersView.getCrud().getGrid().getCell(newEmail).getRow();
		usersView.openRowForEditing(rowNum);

		emailField = usersView.getEmailField(); // Requery email field.
		password = usersView.getPasswordField(); // Requery password field.

		emailField.setValue(uniqueEmail);
		password.setValue("123");

		usersView.getCrud().getEditorSaveButton().click();

		Assertions.assertTrue(usersView.getCrud().isEditorOpen());

		password = usersView.getPasswordField(); // Requery password field.

		// Good password
		password.setValue("Abc123");
		usersView.getCrud().getEditorSaveButton().click();
		Assertions.assertFalse(usersView.getCrud().isEditorOpen());

		// When reopening the form password field must be empty.
		rowNum = usersView.getCrud().getGrid().getCell(uniqueEmail).getRow();
		usersView.openRowForEditing(rowNum);

		password = usersView.getPasswordField(); // Requery password field.
		Assertions.assertEquals("", password.getDomProperty("value"));
	}

	private void createUser(UsersViewElement usersView, String email, String firstName, String lastName,
			String password, String role) {
		usersView.getSearchBar().getCreateNewButton().click();
		Assertions.assertTrue(usersView.getCrud().isEditorOpen());

		usersView.getEmailField().setValue(email);
		usersView.getFirstName().setValue(firstName);
		usersView.getLastName().setValue(lastName);
		usersView.getPasswordField().setValue(password);

		usersView.getRole().selectByText(role);

		usersView.getCrud().getEditorSaveButton().click();
		Assertions.assertFalse(usersView.getCrud().isEditorOpen());
	}

	@BrowserTest
	public void tryToUpdateLockedEntity() {
		UsersViewElement page = openView();

		int rowNum = page.getCrud().getGrid().getCell("barista@vaadin.com").getRow();
		page.openRowForEditing(rowNum);

		PasswordFieldElement field = page.getPasswordField();
		field.setValue("Abc123");
		page.getEmailField().setValue("barista123@vaadin.com");
		page.getCrud().getEditorSaveButton().click();

		Assertions.assertEquals(rowNum, page.getCrud().getGrid().getCell("barista@vaadin.com").getRow());
	}

	@BrowserTest
	public void tryToDeleteLockedEntity() {
		UsersViewElement page = openView();

		int rowNum = page.getCrud().getGrid().getCell("barista@vaadin.com").getRow();
		page.openRowForEditing(rowNum);

		Assertions.assertTrue(page.getCrud().isEditorOpen());

		page.getCrud().getEditorDeleteButton().click();
		page.getDeleteConfirmDialog().getConfirmButton().click();

		Assertions.assertEquals(rowNum, page.getCrud().getGrid().getCell("barista@vaadin.com").getRow());
	}

	@BrowserTest
	public void testCancelConfirmationMessage() {
		UsersViewElement page = openView();
		page.getSearchBar().getCreateNewButton().click();
		page.getFirstName().setValue("Some name");
		page.getCrud().getEditorCancelButton().click();

		Assertions.assertEquals(page.getDiscardConfirmDialog().getHeaderText(), "Discard changes");
	}

	@BrowserTest
	public void accessDenied() {
		StorefrontViewElement storefront = openLoginView().login("barista@vaadin.com", "barista");
		Assertions.assertEquals(3, storefront.getMenu().$(TabElement.class).all().size());

		getDriver().get(APP_URL + "users");
		TestBenchElement link404 = $("vaadin-app-layout > div > a").waitForFirst();

		Assertions.assertEquals("Go to the front page.", link404.getText());
	}
}
