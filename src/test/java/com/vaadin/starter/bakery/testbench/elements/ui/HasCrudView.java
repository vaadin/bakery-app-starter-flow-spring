package com.vaadin.starter.bakery.testbench.elements.ui;

import com.vaadin.flow.component.dialog.testbench.DialogElement;
import com.vaadin.flow.component.formlayout.testbench.FormLayoutElement;
import com.vaadin.flow.component.grid.testbench.GridElement;
import com.vaadin.starter.bakery.testbench.elements.components.ConfirmDialogElement;
import com.vaadin.starter.bakery.testbench.elements.components.FormButtonsBarElement;
import com.vaadin.starter.bakery.testbench.elements.core.FlowComponentRendererElement;
import com.vaadin.testbench.HasElementQuery;
import com.vaadin.testbench.TestBenchElement;

public interface HasCrudView extends HasElementQuery {

	<T extends TestBenchElement> Class<T> getFormClass();

	default GridElement getGrid() {
		return $(GridElement.class).first();
	}

	default ConfirmDialogElement getConfirmDialog() {
		return $(ConfirmDialogElement.class).first();
	}

	default DialogElement getDialog() {
		return $(DialogElement.class).first();
	}

	default FormButtonsBarElement getButtonsBar() {
		return getDialog().$(FlowComponentRendererElement.class).first()
				.$(getFormClass()).first().$(FormButtonsBarElement.class).first();
	}

	default FormLayoutElement getForm() {
		return getDialog().$(FlowComponentRendererElement.class).first()
				.$(getFormClass()).first().$(FormLayoutElement.class).first();
	}

}
