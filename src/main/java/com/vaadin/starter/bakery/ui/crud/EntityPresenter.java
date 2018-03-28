package com.vaadin.starter.bakery.ui.crud;

import java.util.function.UnaryOperator;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;

import com.vaadin.flow.shared.Registration;
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

public class EntityPresenter<T extends AbstractEntity, V extends EntityView<T>> implements HasLogger {

	private CrudService<T> crudService;

	private String entityName;

	private User currentUser;

	private V view;

	private T entity;

	private Registration okRegistration;

	private Registration cancelRegistration;

	public EntityPresenter(CrudService<T> crudService, User currentUser) {
		this.crudService = crudService;
		this.currentUser = currentUser;
	}

	public void setView(V view) {
		this.view = view;
	}

	public V getView() {
		return view;
	}

	public void delete(CrudOperationListener<T> onSuccess) {
		Message CONFIRM_DELETE = Message.CONFIRM_DELETE.createMessage();
		confirmIfNecessaryAndExecute(true, CONFIRM_DELETE, () -> {
			 if (executeOperation(() -> crudService.delete(currentUser, entity))) {
				 onSuccess.execute(entity);
			 }
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
			consumeError(e, CrudErrorMessage.ENTITY_NOT_FOUND, false);
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
		} catch (NullPointerException e) {
			return false;
		}
	}

	public void close() {
		this.entity = null;
		view.clear();
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
			showConfirmationRequest(message, onConfirmed, onCancelled);
		} else {
			onConfirmed.run();
		}
	}

	private void showConfirmationRequest(Message message, Runnable onOk, Runnable onCancel) {
		view.getConfirmDialog().setMessage(message.getMessage());
		view.getConfirmDialog().setCaption(message.getCaption());
		view.getConfirmDialog().setCancelText(message.getCancelText());
		view.getConfirmDialog().setOkText(message.getOkText());

		if (okRegistration != null) {
			okRegistration.remove();
		}
		if (cancelRegistration != null) {
			cancelRegistration.remove();
		}

		okRegistration = view.getConfirmDialog()
				.addOkClickListener(e -> onOk.run());
		cancelRegistration = view.getConfirmDialog()
				.addCancelClickListener(e -> onCancel.run());

		view.getConfirmDialog().setOpened(true);
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
