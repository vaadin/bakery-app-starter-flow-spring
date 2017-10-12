package com.vaadin.starter.bakery.ui;

import com.vaadin.starter.bakery.By;
import com.vaadin.testbench.TestBenchElement;

class ConfirmDialogElement extends TestBenchElement {
	void confirm() {
		findElement(By.shadowSelector("::shadow vaadin-button[theme~='danger']")).click();
	}
}
