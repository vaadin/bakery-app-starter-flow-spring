package com.vaadin.starter.bakery.ui.presenter;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;

import com.vaadin.data.ValidationException;
import com.vaadin.starter.bakery.app.HasLogger;
import com.vaadin.starter.bakery.backend.data.entity.AbstractEntity;
import com.vaadin.starter.bakery.backend.service.CrudService;
import com.vaadin.starter.bakery.backend.service.UserFriendlyDataException;
import com.vaadin.starter.bakery.ui.components.EntityEditView;
import com.vaadin.starter.bakery.ui.components.EntityView;
import com.vaadin.starter.bakery.ui.event.DecisionEvent;
import com.vaadin.starter.bakery.ui.messages.Message;

public class EntityEditPresenter<T extends AbstractEntity> implements HasLogger {

	private CrudService<T> crudService;

	private EntityView<T> view;

	private EntityEditView<T> editor;

	private String entityName;

	private T entity;

	private Runnable operationWaitingConfirmation;

	public EntityEditPresenter(CrudService<T> crudService, EntityEditView<T> editor, EntityView<T> view,
			String entityName) {
		this.crudService = crudService;
		this.editor = editor;
		this.view = view;
		this.entityName = entityName;
		editor.addSaveListener(e -> save());
		editor.addDeleteListener(e -> delete());
		editor.addCancelListener(e -> cancel());
		view.getConfirmer().addDecisionListener(this::confirmationDecisionReceived);
	}

	private void delete() {
		Message CONFIRM_DELETE = Message.CONFIRM_DELETE.createMessage();
		confirmIfNecessary(true, CONFIRM_DELETE, () -> executeJPAOperation(() -> {
			crudService.delete(entity);
			close(true);
		}));
	}

	private void save() {

		try {
			beforeSave();
			if (executeJPAOperation(() -> saveEntity())) {
				view.update(entity);
				close(true);
			}
		} catch (ValidationException e) {
			showValidationError();
		}
	}

	protected void showValidationError() {
		view.showError("Please fill out all required fields before proceeding.", false);
	}

	protected void saveEntity() {
		crudService.save(entity);
	}

	protected void beforeSave() throws ValidationException {
		writeEntity();
	}

	protected void writeEntity() throws ValidationException {
		editor.write(entity);
	}

	private boolean executeJPAOperation(Runnable operation) {
		try {
			operation.run();
			return true;
		} catch (UserFriendlyDataException e) {
			// Commit failed because of application-level data constraints
			showError(e, e.getMessage(), true);
		} catch (DataIntegrityViolationException e) {
			// Commit failed because of validation errors
			showError(e, "The operation can not be executed as there are references to entity in the database", true);
		} catch (OptimisticLockingFailureException e) {
			// Somebody else probably edited the data at the same time
			showError(e, "Somebody else might have updated the data. Please refresh and try again.", true);
		} catch (EntityNotFoundException e) {
			showError(e, String.format("The selected %s was not found.", entityName), false);
		} catch (ConstraintViolationException e) {
			showValidationError();
		}
		return false;
	}

	private void close(boolean updated) {
		view.closeDialog(updated);
		this.entity = null;
	}

	public void cancel() {
		confirmIfNecessary(editor.isDirty(), Message.UNSAVED_CHANGES.createMessage(entityName), () -> close(false));
	}

	public void confirmationDecisionReceived(DecisionEvent event) {
		event.ifConfirmed(this.operationWaitingConfirmation);
		this.operationWaitingConfirmation = null;
	}

	protected void confirmIfNecessary(boolean isNecessary, Message message, Runnable operation) {
		if (isNecessary) {
			this.operationWaitingConfirmation = operation;
			view.getConfirmer().show(message);
		} else {
			operation.run();
		}
	}

	public void loadEntity(Long id, boolean edit) {
		boolean loaded = executeJPAOperation(() -> {
			this.entity = crudService.load(id);
			openDialog(entity, edit);
		});
		if (!loaded) {
			view.closeDialog(true);
		}
	}

	protected void openDialog(T entity, boolean edit) {
		view.openDialog(entity, edit);
	}

	public void createNew(T entity) {
		this.entity = entity;
		openDialog(entity, true);
	}

	private void showError(Exception e, String message, boolean isPersistent) {
		view.showError(message, isPersistent);
		getLogger().debug(message, e);
	}

	protected T getEntity() {
		return entity;
	}

}
