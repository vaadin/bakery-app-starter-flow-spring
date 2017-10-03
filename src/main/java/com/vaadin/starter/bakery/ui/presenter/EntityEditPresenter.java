package com.vaadin.starter.bakery.ui.presenter;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;

import com.vaadin.data.ValidationException;
import com.vaadin.starter.bakery.backend.data.entity.AbstractEntity;
import com.vaadin.starter.bakery.backend.service.CrudService;
import com.vaadin.starter.bakery.backend.service.UserFriendlyDataException;
import com.vaadin.starter.bakery.ui.components.EntityEditView;
import com.vaadin.starter.bakery.ui.components.EntityView;
import com.vaadin.starter.bakery.ui.messages.Message;

public class EntityEditPresenter<T extends AbstractEntity> {

	private CrudService<T> crudService;

	private EntityView view;

	private EntityEditView<T> editor;

	private String entityName;

	private T entity;

	public EntityEditPresenter(CrudService<T> crudService, EntityEditView<T> editor, EntityView view,
			String entityName) {
		this.crudService = crudService;
		this.editor = editor;
		this.view = view;
		this.entityName = entityName;
		editor.addSaveListener(e -> save());
		editor.addDeleteListener(e -> delete());
		editor.addCancelListener(cancelClickEvent -> cancel());
		editor.addValidationFailedEvent(e -> view.toast("Please fill out all required fields before proceeding."));
	}

	private void delete() {
		Message CONFIRM_DELETE = Message.CONFIRM_DELETE.createMessage();
		view.confirm(CONFIRM_DELETE, () -> executeJPAOperation(() -> {
			crudService.delete(entity.getId());
			close(true);
		}));

	}

	private void save() {
		executeJPAOperation(() -> {
			editor.write(entity);
			crudService.save(entity);
			close(true);
		});
	}

	private boolean executeJPAOperation(JPAOperation operation) {
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

	private void close(boolean updated) {
		view.closeDialog(updated);
		this.entity = null;
	}

	public void cancel() {
		if (editor.isDirty()) {
			Message CONFIRM_CANCEL = Message.UNSAVED_CHANGES.createMessage(entityName);
			view.confirm(CONFIRM_CANCEL, () -> view.closeDialog(false));
		} else {
			close(false);
		}
	}

	public void edit(Long id) {
		this.entity = crudService.getRepository().findOne(id);
		if (this.entity == null) {
			String errorMessage = "Cannot find a user with the id '" + id + "'. Please refresh the page and try again.";
			view.showError(errorMessage);
			view.closeDialog(true);
		}
		editor.read(entity);
		view.openDialog();
	}

	public void createNew(T entity) {
		this.entity = entity;
		editor.read(entity);
		view.openDialog();
	}
}
