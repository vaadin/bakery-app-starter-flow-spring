package com.vaadin.starter.bakery.ui;

import com.vaadin.starter.bakery.By;
import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.elementsbase.AbstractElement;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

class ConfirmDialogElement extends AbstractElement {
	void confirm() {
		TestBenchElement button = (TestBenchElement) findElement(By.shadowSelector("::shadow vaadin-button[theme~='danger']"));
		button.focus();
		button.click();
	}
}
