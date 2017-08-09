package com.vaadin.starter.bakery.ui;

import com.vaadin.starter.bakery.By;
import com.vaadin.testbench.elementsbase.AbstractElement;

class ConfirmDialogElement extends AbstractElement {
	void confirm() {
		findElement(By.shadowSelector("::shadow vaadin-button[theme~='danger']")).click();
	}
}
