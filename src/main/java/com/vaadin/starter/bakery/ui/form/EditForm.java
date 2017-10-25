/**
 *
 */
package com.vaadin.starter.bakery.ui.form;

import com.vaadin.data.Binder;
import com.vaadin.flow.model.TemplateModel;
import com.vaadin.starter.bakery.ui.event.CancelEvent;
import com.vaadin.starter.bakery.ui.event.DeleteEvent;
import com.vaadin.starter.bakery.ui.event.SaveEvent;
import com.vaadin.ui.Tag;
import com.vaadin.ui.button.Button;
import com.vaadin.ui.common.HtmlImport;
import com.vaadin.ui.html.H3;
import com.vaadin.ui.polymertemplate.Id;
import com.vaadin.ui.polymertemplate.PolymerTemplate;

@Tag("edit-form")
@HtmlImport("context://src/elements/edit-form.html")
public class EditForm extends PolymerTemplate<TemplateModel> {

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
		setIsDirty(false);
		deleteButton.setDisabled(isNewEntity);
	}

	public void setIsDirty(boolean isDirty) {
		saveButton.setDisabled(!isDirty);
	}
}
