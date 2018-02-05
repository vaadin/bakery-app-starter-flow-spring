package com.vaadin.starter.bakery.ui.crud;

import java.util.function.UnaryOperator;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;

import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.starter.bakery.app.HasLogger;
import com.vaadin.starter.bakery.backend.data.entity.AbstractEntity;
import com.vaadin.starter.bakery.backend.data.entity.User;
import com.vaadin.starter.bakery.backend.data.entity.util.EntityUtil;
import com.vaadin.starter.bakery.backend.service.CrudService;
import com.vaadin.starter.bakery.backend.service.UserFriendlyDataException;
import com.vaadin.starter.bakery.ui.utils.messages.CrudErrorMessage;
import com.vaadin.starter.bakery.ui.utils.messages.Message;
import com.vaadin.starter.bakery.ui.views.EntityView;

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
			executeOperation(() -> crudService.delete(currentUser, entity));
			onSuccess.execute(entity);
			entity = null;
		}, () -> {
		});
	}

	public void save(CrudOperationListener<T> onSuccess) {
		if (executeOperation(() -> saveEntity())) {
			onSuccess.execute(entity);
		}
	}

	public boolean executeUpdate(UnaryOperator<T> updater) {
		return executeOperation(() -> {
			this.entity = updater.apply(getEntity());
		});
	}

	private boolean executeOperation(Runnable operation) {
		try {
			operation.run();
			return true;
		} catch (UserFriendlyDataException e) {
			// Commit failed because of application-level data constraints
			consumeError(e, e.getMessage(), true);
		} catch (DataIntegrityViolationException e) {
			// Commit failed because of validation errors
			consumeError(e, CrudErrorMessage.OPERATION_PREVENTED_BY_REFERENCES, true);
		} catch (OptimisticLockingFailureException e) {
			consumeError(e, CrudErrorMessage.CONCURRENT_UPDATE, true);
		} catch (EntityNotFoundException e) {
			consumeError(e, String.format(CrudErrorMessage.ENTITY_NOT_FOUND, entityName), false);
		} catch (ConstraintViolationException e) {
			consumeError(e, CrudErrorMessage.REQUIRED_FIELDS_MISSING, false);
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
			view.showError(CrudErrorMessage.REQUIRED_FIELDS_MISSING, false);
			return false;
		}
	}

	public void close() {
		this.entity = null;
	}

	public void cancel(Runnable onConfirmed, Runnable onCancelled) {
		confirmIfNecessaryAndExecute(view.isDirty(), Message.UNSAVED_CHANGES.createMessage(entityName), () -> {
			view.clear();
			onConfirmed.run();
		}, onCancelled);
	}

	private void confirmIfNecessaryAndExecute(boolean needsConfirmation, Message message, Runnable onConfirmed,
			Runnable onCancelled) {
		if (needsConfirmation) {
			view.showConfirmationRequest(message, onConfirmed, onCancelled);
		} else {
			onConfirmed.run();
		}
	}

	public boolean loadEntity(Long id, CrudOperationListener<T> onSuccess) {
		return executeOperation(() -> {
			this.entity = crudService.load(id);
			this.entityName = EntityUtil.getName(this.entity.getClass());
			onSuccess.execute(entity);
		});
	}

	public T createNew() {
		return this.entity = crudService.createNew(currentUser);
	}

	public T getEntity() {
		return entity;
	}

	@FunctionalInterface
	public interface CrudOperationListener<T> {
		void execute(T entity);
	}
}
