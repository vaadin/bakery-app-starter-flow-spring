package com.vaadin.starter.bakery.ui;

import com.vaadin.starter.bakery.By;
import com.vaadin.testbench.elementsbase.AbstractElement;
import org.openqa.selenium.WebElement;

import java.util.List;

class UsersViewElement extends AbstractElement {
	List<WebElement> getGridCells() {
		return findElements(By.shadowSelector("::shadow #grid > vaadin-grid-cell-content"));
	}

	WebElement getEditorDialog() {
		return findElement(By.shadowSelector("::shadow items-view::shadow item-detail-dialog"));
	}

	WebElement getFirstTextField() {
		return findElement(By.shadowSelector("::shadow #editor::shadow vaadin-form-layout > vaadin-text-field"));
	}

	WebElement getPasswordField() {
		return findElement(By.shadowSelector("::shadow #editor::shadow vaadin-form-layout > vaadin-password-field"));
	}

	WebElement getUpdateButton() {
		return findElement(By.shadowSelector("::shadow #editor::shadow vaadin-button[theme='primary']"));
	}

	WebElement getDeleteButton() {
		return findElement(By.shadowSelector("::shadow #editor::shadow vaadin-button[theme~='danger']"));
	}

	WebElement getGridCell(String text) {
		return getGridCells()
				.stream()
				.filter(cell -> cell.getText().equals(text))
				.findFirst()
				.orElse(null);
	}
}
