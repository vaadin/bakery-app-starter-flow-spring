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

public class CrudEntityPresenter<T extends AbstractEntity> extends EntityPresenter<T> {

	private ConfigurableFilterDataProvider<T, Void, String> filteredDataProvider;

	private CrudView<T, ?> view;

	public CrudEntityPresenter(FilterableCrudService<T> crudService, User currentUser) {
		super(crudService, currentUser);

		DataProvider<T, String> dataProvider = new CallbackDataProvider<>(
				query -> crudService.findAnyMatching(query.getFilter()).stream(),
				query -> crudService.findAnyMatching(query.getFilter()).size());
		filteredDataProvider = dataProvider.withConfigurableFilter();
	}

	public void setView(CrudView<T, ?> view) {
		super.setView(view);
		this.view = view;
		view.getGrid().setDataProvider(filteredDataProvider);
	}

	public void filter(String filter) {
		filteredDataProvider.setFilter(filter);
	}

	public void cancel() {
		this.cancel(view::closeDialog, view::openDialog);
	}

	public void closeSilently() {
		view.clear();
		view.closeDialog();
	}

	@Override
	public T createNew() {
		return open(super.createNew());
	}

	public void loadEntity(Long id) {
		loadEntity(id, this::open);
	}

	private T open(T entity) {
		view.getBinder().readBean(entity);
		view.getForm().getButtons().setSaveDisabled(true);
		view.getForm().getButtons().setDeleteDisabled(entity.isNew());
		view.updateTitle(entity.isNew());
		view.openDialog();
		return entity;
	}

	public void save() {
		if (writeEntity()) {
			final boolean isNew = getEntity().isNew();
			super.save(e -> {
				if (isNew) {
					filteredDataProvider.refreshAll();
				} else {
					filteredDataProvider.refreshItem(e);
				}
				close();
				view.closeDialog();
			});
		}
	}

	public void delete() {
		super.delete(e -> {
			filteredDataProvider.refreshAll();
			close();
			view.closeDialog();
		});
	}

	public void onValueChange(boolean isDirty) {
		view.getForm().getButtons().setSaveDisabled(!isDirty);
	}
}
