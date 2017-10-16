package com.vaadin.starter.bakery.ui;

import java.util.List;

import org.openqa.selenium.WebElement;

import com.vaadin.starter.bakery.By;
import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.elementsbase.Element;

@Element("bakery-users")
public class UsersViewElement extends TestBenchElement {
	List<WebElement> getGridCells() {
		return findElements(By.shadowSelector("::shadow #grid > vaadin-grid-cell-content"));
	}

	WebElement getEditorDialog() {
		return findElement(By.shadowSelector("::shadow items-view::shadow item-detail-dialog"));
	}

	WebElement getFirstTextField() {
		return findElement(By.shadowSelector("user-edit[slot='user-editor']::shadow vaadin-form-layout > vaadin-text-field"));
	}

	WebElement getPasswordField() {
		return findElement(
				By.shadowSelector("user-edit[slot='user-editor']::shadow vaadin-form-layout > vaadin-password-field"));
	}

	WebElement getUpdateButton() {
		return findElement(By.shadowSelector(
				"user-edit[slot='user-editor']::shadow #user-edit-form::shadow vaadin-button[theme='primary']"));
	}

	WebElement getDeleteButton() {
		return findElement(By.shadowSelector(
				"user-edit[slot='user-editor']::shadow #user-edit-form::shadow vaadin-button[theme~='danger']"));
	}

	WebElement getGridCell(String text) {
		return getGridCells().stream().filter(cell -> cell.getText().equals(text)).findFirst().orElse(null);
	}
}
