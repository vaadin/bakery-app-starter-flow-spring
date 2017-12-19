package com.vaadin.starter.bakery.ui.components;

import com.vaadin.shared.Registration;
import com.vaadin.ui.Component;
import com.vaadin.ui.Tag;
import com.vaadin.ui.common.HtmlImport;
import com.vaadin.ui.event.ComponentEvent;
import com.vaadin.ui.event.ComponentEventListener;
import com.vaadin.ui.event.DomEvent;

/**
 * Java wrapper of the polymer element `buttons-bar`
 */
@Tag("buttons-bar")
@HtmlImport("src/elements/buttons-bar.html")
public class ButtonsBar extends Component {

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
	public static class SaveEvent extends ComponentEvent<ButtonsBar> {
		public SaveEvent(ButtonsBar source, boolean fromClient) {
			super(source, fromClient);
		}
	}

	public Registration addSaveListener(ComponentEventListener<SaveEvent> listener) {
		return addListener(SaveEvent.class, listener);
	}

	@DomEvent("cancel")
	public static class CancelEvent extends ComponentEvent<ButtonsBar> {
		public CancelEvent(ButtonsBar source, boolean fromClient) {
			super(source, fromClient);
		}
	}

	public Registration addCancelListener(ComponentEventListener<CancelEvent> listener) {
		return addListener(CancelEvent.class, listener);
	}

	@DomEvent("delete")
	public static class DeleteEvent extends ComponentEvent<ButtonsBar> {
		public DeleteEvent(ButtonsBar source, boolean fromClient) {
			super(source, fromClient);
		}
	}

	public Registration addDeleteListener(ComponentEventListener<DeleteEvent> listener) {
		return addListener(DeleteEvent.class, listener);
	}
}