/**
 *
 */
package com.vaadin.starter.bakery.ui.form;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;

import com.vaadin.data.ValidationException;
import com.vaadin.starter.bakery.app.HasLogger;
import com.vaadin.starter.bakery.backend.service.UserFriendlyDataException;
import com.vaadin.starter.bakery.ui.HasToast;
import com.vaadin.starter.bakery.ui.components.ConfirmationDialog;
import com.vaadin.starter.bakery.ui.messages.Message;

public class EditFormUtil {

	public static void handeCancel(ConfirmationDialog confirmationDialog, String entityName, boolean isDirty,
			Runnable executeOnConfirm) {
		if (isDirty) {
			Message CONFIRM_CANCEL = Message.UNSAVED_CHANGES.createMessage(entityName);
			confirmationDialog.show(CONFIRM_CANCEL, ev -> executeOnConfirm.run());
		} else {
			executeOnConfirm.run();
		}
	}

	public static <V extends HasToast & HasLogger> boolean executeJPAOperation(V view, JPAOperation operation) {
		try {
			operation.execute();
			return true;
		} catch (UserFriendlyDataException e) {
			// Commit failed because of application-level data constraints
			view.toast(e.getMessage(), true);
			view.getLogger().debug("User-friendly data exception while deleting", e);
		} catch (DataIntegrityViolationException e) {
			// Commit failed because of validation errors
			view.toast("The operation can not be executed as there are references to entity in the database", true);
			view.getLogger().debug("Data integrity violation error while updating entity", e);
		} catch (OptimisticLockingFailureException e) {
			// Somebody else probably edited the data at the same time
			view.toast("Somebody else might have updated the data. Please refresh and try again.", true);
			view.getLogger().debug("Optimistic locking error while saving entity", e);
		} catch (Exception e) {
			// Something went wrong, no idea what
			view.toast("A problem occurred while saving the data. Please check the fields.", true);
			view.getLogger().error("Unable to save entity", e);
		}
		return false;
	}

	public interface JPAOperation {
		void execute() throws ValidationException;
	}
}
