package com.vaadin.starter.bakery.ui.crud;

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
import com.vaadin.starter.bakery.ui.utils.messages.ErrorMessage;
import com.vaadin.starter.bakery.ui.utils.messages.Message;
import com.vaadin.starter.bakery.ui.view.EntityView;

public class EntityPresenter<T extends AbstractEntity> implements HasLogger {

	private CrudService<T> crudService;

	private String entityName;

	private User currentUser;

	private EntityView<T> view;

	private T entity;

	public EntityPresenter(CrudService<T> crudService, User currentUser) {
		this.crudService = crudService;
		this.currentUser = currentUser;
	}

	public void setView(EntityView<T> view) {
		this.view = view;
	}

	public void delete(CrudOperationListener<T> onSuccess) {
		Message CONFIRM_DELETE = Message.CONFIRM_DELETE.createMessage();
		confirmIfNecessaryAndExecute(true, CONFIRM_DELETE, () -> {
			executeJPAOperation(() -> crudService.delete(currentUser, entity));
			onSuccess.execute(entity);
			entity = null;
		});
	}

	public void save(CrudOperationListener<T> onSuccess) {
		if (executeJPAOperation(() -> saveEntity())) {
			onSuccess.execute(entity);
		}
	}

	public boolean executeJPAOperation(Runnable operation) {
		try {
			operation.run();
			return true;
		} catch (UserFriendlyDataException e) {
			// Commit failed because of application-level data constraints
			consumeError(e, e.getMessage(), true);
		} catch (DataIntegrityViolationException e) {
			// Commit failed because of validation errors
			consumeError(e, ErrorMessage.OPERATION_PREVENTED_BY_REFERENCES, true);
		} catch (OptimisticLockingFailureException e) {
			consumeError(e, ErrorMessage.CONCURRENT_UPDATE, true);
		} catch (EntityNotFoundException e) {
			consumeError(e, ErrorMessage.ENTITY_NOT_FOUND, false);
		} catch (ConstraintViolationException e) {
			consumeError(e, ErrorMessage.REQUIRED_FIELDS_MISSING, false);
		}
		return false;
	}

	private void consumeError(Exception e, String message, boolean isPersistent) {
		getLogger().debug(message, e);
		view.showError(message, isPersistent);
	}

	protected void saveEntity() {
		this.entity = crudService.save(currentUser, entity);
	}

	public boolean writeEntity() {
		try {
			view.write(entity);
			return true;
		} catch (ValidationException e) {
			view.showError(ErrorMessage.REQUIRED_FIELDS_MISSING, false);
			return false;
		}
	}

	public void close() {
		this.entity = null;
	}

	public void cancel(Runnable onConfirmed) {
		confirmIfNecessaryAndExecute(view.isDirty(), Message.UNSAVED_CHANGES.createMessage(entityName), onConfirmed);
	}

	protected void confirmIfNecessaryAndExecute(boolean needsConfirmation, Message message, Runnable operation) {
		if (needsConfirmation) {
			view.showConfirmationRequest(message, operation);
		} else {
			operation.run();
		}
	}

	public boolean loadEntity(Long id, CrudOperationListener<T> onSuccess) {
		return executeJPAOperation(() -> {
			this.entity = crudService.load(id);
			onSuccess.execute(entity);
		});
	}

	public T createNew() {
		return this.entity = crudService.createNew(currentUser);
	}

	public T getEntity() {
		return entity;
	}

	protected void setEntity(T entity) {
		this.entity = entity;
	}

	@FunctionalInterface
	public interface CrudOperationListener<T> {

		void execute(T entity);
	}
}
