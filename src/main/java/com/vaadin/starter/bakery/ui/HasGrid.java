package com.vaadin.starter.bakery.ui;

import com.vaadin.data.provider.DataProvider;
import com.vaadin.ui.grid.Grid;

public interface HasGrid<T> {

	Grid<T> getGrid();

	/**
	 * Sets / refreshes the entities in grid.
	 *
	 * @param dataProvider provides entities for grid
	 */
	default void setDataProvider(DataProvider<T, String> dataProvider) {
		getGrid().setDataProvider(dataProvider);
	};

}
