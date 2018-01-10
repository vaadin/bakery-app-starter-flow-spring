package com.vaadin.starter.bakery.ui.view;

import com.vaadin.data.ValidationException;
import com.vaadin.starter.bakery.app.HasLogger;
import com.vaadin.starter.bakery.backend.data.entity.AbstractEntity;
import com.vaadin.starter.bakery.backend.data.entity.User;
import com.vaadin.starter.bakery.backend.service.CrudService;
import com.vaadin.starter.bakery.ui.utils.messages.ErrorMessage;
import com.vaadin.starter.bakery.ui.utils.messages.Message;

public class EntityPresenter<T extends AbstractEntity> implements HasLogger {

	private CrudService<T> crudService;

	private String entityName;

	private User currentUser;

	private JPAPresenter jpaPresenter;

	private EntityView<T> view;

	private T entity;

	public EntityPresenter(CrudService<T> crudService, User currentUser, JPAPresenter jpaPresenter) {
		this.crudService = crudService;
		this.currentUser = currentUser;
		this.jpaPresenter = jpaPresenter;
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
		return jpaPresenter.executeJPAOperation(operation, view::showError);
	}

	protected void saveEntity() {
		this.entity = crudService.save(currentUser, entity);
	}

	public boolean writeEntity() {
		try {
			view.write(entity);
			return true;
		} catch (ValidationException e) {
			view.showError(ErrorMessage.REQUIRED_MESSAGE, false);
			return false;
		}
	}

	public void close() {
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

	public void loadEntity(Long id, CrudOperationListener<T> onSuccess) {
		boolean loaded = executeJPAOperation(() -> {
			this.entity = crudService.load(id);
			onSuccess.execute(entity);
		});
		if (!loaded) {
			view.closeDialog();
		}
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

}
