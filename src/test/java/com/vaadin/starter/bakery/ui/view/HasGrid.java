package com.vaadin.starter.bakery.ui.view;

import com.vaadin.starter.bakery.elements.GridElement;
import com.vaadin.starter.bakery.elements.VaadinGridCellContentElement;
import com.vaadin.testbench.HasElementQuery;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public interface HasGrid extends HasElementQuery {

	default GridElement getGrid() {
		return $(GridElement.class).first();
	}

	default List<VaadinGridCellContentElement> getGridCells() {
		return getGrid().$(VaadinGridCellContentElement.class).all();
	}

	default VaadinGridCellContentElement getGridCell(String text) {
		return getGridCells().stream().filter(cell -> cell.getText().equals(text)).findFirst().orElse(null);
	}

}
