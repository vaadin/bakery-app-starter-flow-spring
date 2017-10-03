/**
 *
 */
package com.vaadin.starter.bakery.ui.form;

import java.util.function.Supplier;

import com.vaadin.annotations.HtmlImport;
import com.vaadin.annotations.Id;
import com.vaadin.annotations.Tag;
import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.flow.html.H3;
import com.vaadin.flow.router.View;
import com.vaadin.flow.template.PolymerTemplate;
import com.vaadin.flow.template.model.TemplateModel;
import com.vaadin.starter.bakery.ui.HasToast;
import com.vaadin.starter.bakery.ui.components.ConfirmationDialog;
import com.vaadin.starter.bakery.ui.event.CancelEvent;
import com.vaadin.starter.bakery.ui.event.SaveEvent;
import com.vaadin.ui.Button;

@Tag("edit-form")
@HtmlImport("context://src/elements/edit-form.html")
public class EditForm extends PolymerTemplate<TemplateModel> implements View, HasToast {

	@Id("title")
	private H3 title;

	@Id("save")
	private Button saveButton;

	@Id("delete")
	private Button deleteButton;

	@Id("cancel")
	private Button cancelButton;

	@Id("confirmation-dialog")
	private ConfirmationDialog confirmationDialog;

	private String entityName;

	public <E> void init(Binder<E> binder, Supplier<E> entitySupplier, String entityName) {
		this.entityName = entityName;
		binder.addValueChangeListener(e -> saveButton.setDisabled(binder.hasChanges()));
		saveButton.addClickListener(e -> {
			try {
				binder.writeBean(entitySupplier.get());
				fireEvent(new SaveEvent(this, false));
			} catch (ValidationException ex) {
				toast("Please fill out all required fields before proceeding.");
			}
		});
		cancelButton.addClickListener(e -> fireEvent(new CancelEvent(EditForm.this, false)));

	}

	public void showEditor(boolean isNewEntity) {
		title.setText((isNewEntity ? "New" : "Edit") + " " + entityName);
		saveButton.setDisabled(true);
		deleteButton.setDisabled(isNewEntity);

	}
}
