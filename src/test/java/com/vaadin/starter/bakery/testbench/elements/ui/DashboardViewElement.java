package com.vaadin.starter.bakery.testbench.elements.ui;

import com.vaadin.starter.bakery.testbench.elements.core.VaadinBoardElement;
import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.elementsbase.Element;

@Element("dashboard-view")
public class DashboardViewElement extends TestBenchElement implements HasApp {

	public VaadinBoardElement getBoard() {
		return $(VaadinBoardElement.class).first();
	}

}
