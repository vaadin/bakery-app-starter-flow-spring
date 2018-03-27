/**
 *
 */
package com.vaadin.starter.bakery.ui.crud;

import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.starter.bakery.backend.data.entity.AbstractEntity;
import com.vaadin.starter.bakery.backend.data.entity.User;
import com.vaadin.starter.bakery.backend.service.FilterableCrudService;

public class CrudEntityPresenter<T extends AbstractEntity> extends EntityPresenter<T, CrudView<T, ?>> {

	private ConfigurableFilterDataProvider<T, Void, String> filteredDataProvider;


	public CrudEntityPresenter(FilterableCrudService<T> crudService, User currentUser) {
		super(crudService, currentUser);

		DataProvider<T, String> dataProvider = new CallbackDataProvider<>(
				query -> crudService.findAnyMatching(query.getFilter()).stream(),
				query -> crudService.findAnyMatching(query.getFilter()).size());
		filteredDataProvider = dataProvider.withConfigurableFilter();
	}

	@Override
	public void setView(CrudView<T, ?> view) {
		super.setView(view);
		view.getGrid().setDataProvider(filteredDataProvider);
	}

	public void filter(String filter) {
		filteredDataProvider.setFilter(filter);
	}

	public void cancel() {
		cancel(getView()::closeDialog, getView()::openDialog);
	}

	public void closeSilently() {
		getView().clear();
		getView().closeDialog();
	}

	@Override
	public T createNew() {
		return open(super.createNew());
	}

	public void loadEntity(Long id) {
		loadEntity(id, this::open);
	}

	private T open(T entity) {
		getView().getBinder().readBean(entity);
		getView().getForm().getButtons().setSaveDisabled(true);
		getView().getForm().getButtons().setDeleteDisabled(entity.isNew());
		getView().updateTitle(entity.isNew());
		getView().openDialog();
		return entity;
	}

	public void save() {
		if (writeEntity()) {
			final boolean isNew = getEntity().isNew();
			super.save(e -> {
				if (isNew) {
					getView().showNotification(getView().getEntityName() + " was created");
					filteredDataProvider.refreshAll();
				} else {
					filteredDataProvider.refreshItem(e);
				}
				close();
				getView().closeDialog();
			});
		}
	}

	public void delete() {
		super.delete(e -> {
			filteredDataProvider.refreshAll();
			close();
			getView().closeDialog();
		});
	}

	public void onValueChange(boolean isDirty) {
		getView().getForm().getButtons().setSaveDisabled(!isDirty);
	}
}
