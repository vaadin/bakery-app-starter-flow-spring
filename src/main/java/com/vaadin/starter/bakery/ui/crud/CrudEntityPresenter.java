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

	private CrudView<T, ?> crudView;

	public CrudEntityPresenter(FilterableCrudService<T> crudService, User currentUser) {
		super(crudService, currentUser);

		DataProvider<T, String> dataProvider = new CallbackDataProvider<>(
				query -> crudService.findAnyMatching(query.getFilter()).stream(),
				query -> crudService.findAnyMatching(query.getFilter()).size());
		filteredDataProvider = dataProvider.withConfigurableFilter();
	}

	public void setView(CrudView<T, ?> view) {
		super.setView(view);
		this.crudView = view;
		view.getGrid().setDataProvider(filteredDataProvider);
	}

	public void filter(String filter) {
		filteredDataProvider.setFilter(filter);
	}

	public void cancel() {
		this.cancel(crudView::closeDialog, crudView::openDialog);
	}

	public void closeSilently() {
		crudView.clear();
		crudView.closeDialog();
	}

	@Override
	public T createNew() {
		return open(super.createNew());
	}

	public void loadEntity(Long id) {
		loadEntity(id, this::open);
	}

	private T open(T entity) {
		crudView.getBinder().readBean(entity);
		crudView.getForm().getButtons().setSaveDisabled(true);
		crudView.getForm().getButtons().setDeleteDisabled(entity.isNew());
		crudView.updateTitle(entity.isNew());
		crudView.openDialog();
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
				crudView.closeDialog();
			});
		}
	}

	public void delete() {
		super.delete(e -> {
			filteredDataProvider.refreshAll();
			close();
			crudView.closeDialog();
		});
	}

	public void onValueChange(boolean isDirty) {
		crudView.getForm().getButtons().setSaveDisabled(!isDirty);
	}
}
