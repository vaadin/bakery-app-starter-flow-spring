package com.vaadin.starter.bakery.tb.elements.ui;

import com.vaadin.starter.bakery.tb.elements.core.VaadinBoardElement;
import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.elementsbase.Element;

@Element("bakery-dashboard")
public class DashboardViewElement extends TestBenchElement implements HasApp {

	public VaadinBoardElement getBoard() {
		return $(VaadinBoardElement.class).first();
	}

}
