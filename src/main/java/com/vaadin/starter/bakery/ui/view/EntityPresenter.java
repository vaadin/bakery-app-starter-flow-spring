package com.vaadin.starter.bakery.ui.view;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;

import com.vaadin.data.ValidationException;
import com.vaadin.starter.bakery.app.HasLogger;
import com.vaadin.starter.bakery.backend.data.entity.AbstractEntity;
import com.vaadin.starter.bakery.backend.data.entity.User;
import com.vaadin.starter.bakery.backend.service.CrudService;
import com.vaadin.starter.bakery.backend.service.UserFriendlyDataException;
import com.vaadin.starter.bakery.ui.utils.messages.Message;

public class EntityPresenter<T extends AbstractEntity> implements HasLogger {

	public static final String DB_NOT_FOUND_MESSAGE = "The selected %s was not found.";

	public static final String DB_CHANGES_MESSAGE = "Somebody else might have updated the data. Please refresh and try again.";

	public static final String DB_REFERENCES_MESSAGE = "The operation can not be executed as there are references to entity in the database";

	public static final String REQUIRED_MESSAGE = "Please fill out all required fields before proceeding.";

	private CrudService<T> crudService;

	protected EntityView<T> view;

	private String entityName;

	private User currentUser;
	
	private T entity;

	public EntityPresenter(CrudService<T> crudService, EntityView<T> view, String entityName, User currentUser) {
		this.crudService = crudService;
		this.view = view;
		this.entityName = entityName;
		this.currentUser = currentUser;
	}

	public EntityPresenter(CrudService<T> crudService, String entityName, User currentUser) {
		this(crudService, null, entityName, currentUser);
	}

	public void init(EntityView<T> view) {
		this.view = view;
	}

	public void delete() {
		Message CONFIRM_DELETE = Message.CONFIRM_DELETE.createMessage();
		confirmIfNecessaryAndExecute(true, CONFIRM_DELETE, () -> executeJPAOperation(() -> {
			crudService.delete(currentUser, entity);
			onDeleteSuccess();
		}));
	}

	public void save() {

		try {
			beforeSave();
			boolean isNew = getEntity().isNew();
			if (executeJPAOperation(() -> saveEntity())) {
				onSaveSuccess(isNew);
			}
		} catch (ValidationException e) {
			showValidationError();
		}
	}

	protected void showValidationError() {
		view.showError(REQUIRED_MESSAGE, false);
	}

	protected void saveEntity() {
		crudService.save(currentUser, entity);
	}

	protected void beforeSave() throws ValidationException {
		writeEntity();
	}

	protected void onSaveSuccess(boolean isNew) {
		close();
	}

	protected void onDeleteSuccess() {
		close();
	}

	protected void writeEntity() throws ValidationException {
		view.write(entity);
	}

	protected boolean executeJPAOperation(Runnable operation) {
		try {
			operation.run();
			return true;
		} catch (UserFriendlyDataException e) {
			// Commit failed because of application-level data constraints
			showError(e, e.getMessage(), true);
		} catch (DataIntegrityViolationException e) {
			// Commit failed because of validation errors
			showError(e, DB_REFERENCES_MESSAGE, true);
		} catch (OptimisticLockingFailureException e) {
			showError(e, DB_CHANGES_MESSAGE, true);
		} catch (EntityNotFoundException e) {
			showError(e, String.format(DB_NOT_FOUND_MESSAGE, entityName), false);
		} catch (ConstraintViolationException e) {
			showValidationError();
		}
		return false;
	}

	private void close() {
		view.closeDialog();
		this.entity = null;
	}

	public void cancel() {
		confirmIfNecessaryAndExecute(view.isDirty(), Message.UNSAVED_CHANGES.createMessage(entityName), this::close);
	}

	protected void confirmIfNecessaryAndExecute(boolean needsConfirmation, Message message, Runnable operation) {
		if (needsConfirmation) {
			view.showConfirmationRequest(message, operation);
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
			view.closeDialog();
		}
	}

	protected void openDialog(T entity, boolean edit) {
		view.openDialog(entity, edit);
	}

	public void createNew(User currentUser) {
		this.entity = crudService.createNew(currentUser);
		openDialog(entity, true);
	}

	private void showError(Exception e, String message, boolean isPersistent) {
		view.showError(message, isPersistent);
		getLogger().debug(message, e);
	}

	protected T getEntity() {
		return entity;
	}

	protected void setEntity(T entity) {
		this.entity = entity;
	}

}
