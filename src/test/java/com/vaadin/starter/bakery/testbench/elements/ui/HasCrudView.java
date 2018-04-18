package com.vaadin.starter.bakery.testbench.elements.ui;

import com.vaadin.flow.component.dialog.testbench.DialogElement;
import com.vaadin.flow.component.formlayout.testbench.FormLayoutElement;
import com.vaadin.flow.component.grid.testbench.GridElement;
import com.vaadin.starter.bakery.testbench.elements.components.ConfirmDialogElement;
import com.vaadin.starter.bakery.testbench.elements.components.FormButtonsBarElement;
import com.vaadin.starter.bakery.testbench.elements.core.FlowComponentRendererElement;
import com.vaadin.testbench.ElementQuery;
import com.vaadin.testbench.HasElementQuery;
import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.elementsbase.Element;
import org.openqa.selenium.WebDriver;

import java.util.Optional;

public interface HasCrudView extends HasElementQuery {

	WebDriver getRootContext();

	<T extends TestBenchElement> Class<T> getFormClass();

	default GridElement getGrid() {
		return $(GridElement.class).first();
	}

	default Optional<ConfirmDialogElement> getConfirmDialog() {
		ElementQuery<ConfirmDialogElement> query = new ElementQuery<>(ConfirmDialogElement.class,
				ConfirmDialogElement.class.getAnnotation(Element.class).value())
				.context(getRootContext());
		return query.exists() ? Optional.of(query.first()) : Optional.empty();
	}

	default Optional<DialogElement> getDialog() {
		ElementQuery<DialogElement> query = new ElementQuery<>(DialogElement.class,
				DialogElement.class.getAnnotation(Element.class).value())
				.context(getRootContext());
		return query.exists() ? Optional.of(query.first()) : Optional.empty();
	}

	default FormButtonsBarElement getButtonsBar() {
		return getDialog().get().$(FlowComponentRendererElement.class).first()
				.$(getFormClass()).first().$(FormButtonsBarElement.class).first();
	}

	default FormLayoutElement getForm() {
		return getDialog().get().$(FlowComponentRendererElement.class).first()
				.$(getFormClass()).first().$(FormLayoutElement.class).first();
	}

}
