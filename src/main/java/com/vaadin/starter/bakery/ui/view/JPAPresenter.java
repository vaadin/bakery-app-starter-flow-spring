package com.vaadin.starter.bakery.ui.view;

import java.util.function.BiConsumer;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.starter.bakery.app.HasLogger;
import com.vaadin.starter.bakery.backend.service.UserFriendlyDataException;
import com.vaadin.starter.bakery.ui.utils.messages.ErrorMessage;

@SpringComponent
public class JPAPresenter implements HasLogger {

	public boolean executeJPAOperation(Runnable operation, BiConsumer<String, Boolean> onError) {
		try {
			operation.run();
			return true;
		} catch (UserFriendlyDataException e) {
			// Commit failed because of application-level data constraints
			showError(onError, e, e.getMessage(), true);
		} catch (DataIntegrityViolationException e) {
			// Commit failed because of validation errors
			showError(onError, e, ErrorMessage.DB_REFERENCES_MESSAGE, true);
		} catch (OptimisticLockingFailureException e) {
			showError(onError, e, ErrorMessage.DB_CHANGES_MESSAGE, true);
		} catch (EntityNotFoundException e) {
			showError(onError, e, ErrorMessage.DB_NOT_FOUND_MESSAGE, false);
		} catch (ConstraintViolationException e) {
			showError(onError, e, ErrorMessage.REQUIRED_MESSAGE, false);
		}
		return false;
	}

	private void showError(BiConsumer<String, Boolean> onError, Exception e, String message, boolean isPersistent) {
		getLogger().debug(message, e);
		onError.accept(message, isPersistent);
	}

}
