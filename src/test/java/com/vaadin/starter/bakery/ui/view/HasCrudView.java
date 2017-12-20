package com.vaadin.starter.bakery.ui.view;

import java.util.List;

import org.openqa.selenium.By;

import com.vaadin.starter.bakery.elements.DialogOverlayElement;
import com.vaadin.starter.bakery.elements.FormDialogElement;
import com.vaadin.starter.bakery.elements.FormLayoutElement;
import com.vaadin.starter.bakery.elements.GridElement;
import com.vaadin.starter.bakery.elements.VaadinGridCellContentElement;
import com.vaadin.starter.bakery.ui.ConfirmDialogElement;
import com.vaadin.starter.bakery.ui.components.ButtonsBarElement;
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

	default ButtonsBarElement getButtonsBar() {
		return getVaadinOverlay().$(ButtonsBarElement.class).first();
	}

	default FormLayoutElement getForm() {
		return getVaadinOverlay().$(FormLayoutElement.class).first();
	}

	default <T extends TestBenchElement> T getField(String id, Class<T> elementType) {
		return getForm().findElement(By.id(id)).wrap(elementType);
	}
}
