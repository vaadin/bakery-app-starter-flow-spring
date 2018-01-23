package com.vaadin.starter.bakery.ui.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.DomEvent;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.shared.Registration;

/**
 * Java wrapper of the polymer element `form-buttons-bar`
 */
@Tag("form-buttons-bar")
@HtmlImport("src/components/form-buttons-bar.html")
public class FormButtonsBar extends Component {

	public void setSaveText(String saveText) {
		getElement().setProperty("saveText", saveText == null ? "" : saveText);
	}

	public void setCancelText(String cancelText) {
		getElement().setProperty("cancelText", cancelText == null ? "" : cancelText);
	}

	public void setDeleteText(String deleteText) {
		getElement().setProperty("deleteText", deleteText == null ? "" : deleteText);
	}

	public void setSaveDisabled(boolean saveDisabled) {
		getElement().setProperty("saveDisabled", saveDisabled);
	}

	public void setCancelDisabled(boolean cancelDisabled) {
		getElement().setProperty("cancelDisabled", cancelDisabled);
	}

	public void setDeleteDisabled(boolean deleteDisabled) {
		getElement().setProperty("deleteDisabled", deleteDisabled);
	}

	@DomEvent("save")
	public static class SaveEvent extends ComponentEvent<FormButtonsBar> {
		public SaveEvent(FormButtonsBar source, boolean fromClient) {
			super(source, fromClient);
		}
	}

	public Registration addSaveListener(ComponentEventListener<SaveEvent> listener) {
		return addListener(SaveEvent.class, listener);
	}

	@DomEvent("cancel")
	public static class CancelEvent extends ComponentEvent<FormButtonsBar> {
		public CancelEvent(FormButtonsBar source, boolean fromClient) {
			super(source, fromClient);
		}
	}

	public Registration addCancelListener(ComponentEventListener<CancelEvent> listener) {
		return addListener(CancelEvent.class, listener);
	}

	@DomEvent("delete")
	public static class DeleteEvent extends ComponentEvent<FormButtonsBar> {
		public DeleteEvent(FormButtonsBar source, boolean fromClient) {
			super(source, fromClient);
		}
	}

	public Registration addDeleteListener(ComponentEventListener<DeleteEvent> listener) {
		return addListener(DeleteEvent.class, listener);
	}
}