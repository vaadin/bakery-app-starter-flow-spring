package com.vaadin.starter.bakery.ui.view;

import java.util.List;

import com.vaadin.starter.bakery.elements.GridElement;
import com.vaadin.starter.bakery.elements.VaadinGridCellContentElement;
import com.vaadin.testbench.HasElementQuery;

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
