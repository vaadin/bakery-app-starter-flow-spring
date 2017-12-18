package com.vaadin.starter.bakery.ui.view;

import com.vaadin.starter.bakery.elements.VaadinBoardElement;
import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.elementsbase.Element;

@Element("bakery-dashboard")
public class DashboardViewElement extends TestBenchElement implements HasApp {

	public VaadinBoardElement getBoard() {
		return $(VaadinBoardElement.class).first();
	}

}
