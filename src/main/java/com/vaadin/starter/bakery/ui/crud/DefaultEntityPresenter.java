/**
 *
 */
package com.vaadin.starter.bakery.ui.crud;

import com.vaadin.data.provider.CallbackDataProvider;
import com.vaadin.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.starter.bakery.backend.data.entity.AbstractEntity;
import com.vaadin.starter.bakery.backend.service.FilterableCrudService;
import com.vaadin.starter.bakery.ui.view.CrudOperationListener;
import com.vaadin.starter.bakery.ui.view.EntityPresenter;

public class DefaultEntityPresenter<T extends AbstractEntity> {

	private final ConfigurableFilterDataProvider<T, Void, String> filteredDataProvider;

	private final EntityPresenter<T> entityPresenter;

	private CrudView<T, ?> view;

	public DefaultEntityPresenter(EntityPresenter<T> entityPresenter, FilterableCrudService<T> crudService) {
		this.entityPresenter = entityPresenter;
		DataProvider<T, String> dataProvider = new CallbackDataProvider<>(
				query -> crudService.findAnyMatching(query.getFilter()).stream(),
				query -> crudService.findAnyMatching(query.getFilter()).size());

		filteredDataProvider = dataProvider.withConfigurableFilter();
	}

	public void init(CrudView<T, ?> view) {
		entityPresenter.setView(view);
		this.view = view;
		view.getGrid().setDataProvider(filteredDataProvider);
	}

	public void filter(String filter) {
		filteredDataProvider.setFilter(filter);
	}

	public void cancel() {
		entityPresenter.cancel();
	}

	public void createNew() {
		open(entityPresenter.createNew());
	}

	public void loadEntity(Long id, boolean edit) {
		entityPresenter.loadEntity(id, this::open);
	}

	private void open(T entity) {
		view.getBinder().readBean(entity);
		view.getButtons().setSaveDisabled(true);
		view.getButtons().setDeleteDisabled(entity.isNew());
		view.updateTitle(entity.isNew());
		view.getDialog().setOpened(true);
	}

	public void save() {
		if (entityPresenter.writeEntity()) {
			final boolean isNew = entityPresenter.getEntity().isNew();
			CrudOperationListener<T> onSaveSuccess = e -> {
				if (isNew) {
					filteredDataProvider.refreshAll();
				} else {
					filteredDataProvider.refreshItem(e);
				}
				entityPresenter.close();
			};
			entityPresenter.save(onSaveSuccess);
		}
	}

	public void delete() {
		entityPresenter.delete(e -> {
			filteredDataProvider.refreshAll();
			entityPresenter.close();
		});
	}

	public void onValueChange(boolean isDirty) {
		view.getButtons().setSaveDisabled(!isDirty);
	}
}
