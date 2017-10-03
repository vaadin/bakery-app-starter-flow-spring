/**
 *
 */
package com.vaadin.starter.bakery.ui.form;

import com.vaadin.annotations.HtmlImport;
import com.vaadin.annotations.Id;
import com.vaadin.annotations.Tag;
import com.vaadin.data.Binder;
import com.vaadin.flow.html.H3;
import com.vaadin.flow.router.View;
import com.vaadin.flow.template.PolymerTemplate;
import com.vaadin.flow.template.model.TemplateModel;
import com.vaadin.starter.bakery.ui.HasToast;
import com.vaadin.starter.bakery.ui.event.CancelEvent;
import com.vaadin.starter.bakery.ui.event.DeleteEvent;
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

	private String entityName;

	public <E> void init(Binder<E> binder, String entityName) {
		this.entityName = entityName;
		binder.addValueChangeListener(e -> saveButton.setDisabled(!binder.hasChanges()));
		saveButton.addClickListener(e -> fireEvent(new SaveEvent(this, false)));
		cancelButton.addClickListener(e -> fireEvent(new CancelEvent(EditForm.this, false)));
		deleteButton.addClickListener(e -> fireEvent(new DeleteEvent(EditForm.this, false)));

	}

	public void showEditor(boolean isNewEntity) {
		title.setText((isNewEntity ? "New" : "Edit") + " " + entityName);
		saveButton.setDisabled(true);
		deleteButton.setDisabled(isNewEntity);
	}
}
