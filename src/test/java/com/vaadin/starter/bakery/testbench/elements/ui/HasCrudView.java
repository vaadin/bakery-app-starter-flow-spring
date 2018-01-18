package com.vaadin.starter.bakery.testbench.elements.ui;

import java.util.List;

import org.openqa.selenium.By;

import com.vaadin.starter.bakery.testbench.elements.components.ConfirmDialogElement;
import com.vaadin.starter.bakery.testbench.elements.components.FormButtonsBarElement;
import com.vaadin.starter.bakery.testbench.elements.core.DialogOverlayElement;
import com.vaadin.starter.bakery.testbench.elements.core.FormDialogElement;
import com.vaadin.starter.bakery.testbench.elements.core.FormLayoutElement;
import com.vaadin.starter.bakery.testbench.elements.core.GridElement;
import com.vaadin.starter.bakery.testbench.elements.core.VaadinGridCellContentElement;
import com.vaadin.testbench.HasElementQuery;
import com.vaadin.testbench.TestBenchElement;

public interface HasCrudView extends HasElementQuery {

	default GridElement getGrid() {
		return $(GridElement.class).first();
	}

	default List<VaadinGridCellContentElement> getGridCells() {
		return getGrid().$(VaadinGridCellContentElement.class).all();
	}

	default VaadinGridCellContentElement getGridCell(String text) {
		return getGridCells().stream().filter(cell -> cell.getText().equals(text)).findFirst().orElse(null);
	}

	default ConfirmDialogElement getConfirmDialog() {
		return $(ConfirmDialogElement.class).onPage().first();
	}

	default FormDialogElement getDialog() {
		return $(FormDialogElement.class).first();
	}

	default DialogOverlayElement getVaadinOverlay() {
		return $(DialogOverlayElement.class).onPage().first();
	}

	default FormButtonsBarElement getButtonsBar() {
		return getVaadinOverlay().$(FormButtonsBarElement.class).first();
	}

	default FormLayoutElement getForm() {
		return getVaadinOverlay().$(FormLayoutElement.class).first();
	}

	default <T extends TestBenchElement> T getField(String id, Class<T> elementType) {
		return getForm().findElement(By.id(id)).wrap(elementType);
	}
}
