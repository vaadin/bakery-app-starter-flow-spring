package com.vaadin.starter.bakery.ui.view;

import com.vaadin.data.ValidationException;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.generated.starter.elements.GeneratedStarterConfirmDialog;
import com.vaadin.shared.Registration;
import com.vaadin.starter.bakery.ui.utils.messages.Message;
import com.vaadin.ui.UI;
import com.vaadin.ui.event.ComponentEventListener;

/**
 * A master / detail view for entities of the type <code>T</code>. The view
 * has a list of entities (the 'master' part) and a dialog to show a single
 * entity (the 'detail' part). The dialog has two modes: a view mode and an
 * edit mode.
 * <p>
 * The view can also show notifications, error messages, and confirmation
 * requests.
 *
 * @param <T> the entity type
 */
public interface EntityView<T> extends HasNotifications {
	/**
	 * Sets / refreshes the entities in grid.
	 *
	 * @param dataProvider provides entities for grid
	 */
	void setDataProvider(DataProvider<T, ?> dataProvider);

	/**
	 * Opens a dialog showing details of a single entity.
	 *
	 * @param entity the entity to show in the dialog
	 * @param edit if <code>true</code> the dialog opens in the 'edit' mode
	 */
	void openDialog(T entity, boolean edit);

	/**
	 * Closes the entity dialog.
	 */
	void closeDialog();

	/**
	 * Shows a confirmation request dialog with the given message.
	 *
	 * @param message
	 *            the message with a request to ask a confirmation for (see
	 *            {@link Message}
	 * @param onOk
	 *            command to execute if the user presses 'ok' in the dialog
	 */
	default void showConfirmationRequest(Message message, Runnable onOk) {
	    Message.confirm(message, onOk);
	}

	/**
	 * Shows an error notification with a given text.
	 *
	 * @param message a user-friendly error message
	 * @param isPersistent if <code>true</code> the message requires a user
	 *                     action to disappear (if <code>false</code> it
	 *                     disappears automatically after some time)
	 */
	default void showError(String message, boolean isPersistent) {
		showNotification(message, isPersistent);
	}

	/**
	 * Returns the current dirty state of the entity dialog.
	 *
	 * @return <code>true</code> if the entity dialog is open in the 'edit'
	 * mode and has unsaved changes
	 */
	boolean isDirty();

	/**
	 * Writes the changes from the entity dialog into the given entity instance
	 * (see {@link com.vaadin.data.Binder#writeBean(Object)})
	 *
	 * @param entity the entity instance to save the changes into
	 * @throws ValidationException if the values entered into the entity dialog
	 * cannot be converted into entity properties
	 */
	void write(T entity) throws ValidationException;
}
