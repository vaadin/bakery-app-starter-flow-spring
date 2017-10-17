package com.vaadin.starter.bakery.ui.view;

import com.vaadin.starter.bakery.By;
import com.vaadin.starter.bakery.elements.GridElement;
import com.vaadin.testbench.HasElementQuery;
import org.openqa.selenium.WebElement;

import java.util.List;

public interface HasGrid extends HasElementQuery {

	default GridElement getGrid() {
		return $(GridElement.class).onPage().first();
	}

	default List<WebElement> getGridCells() {
		return getGrid().findElements(By.cssSelector("vaadin-grid-cell-content"));
	}

	default WebElement getGridCell(String text) {
		return getGridCells().stream().filter(cell -> cell.getText().equals(text)).findFirst().orElse(null);
	}

}
