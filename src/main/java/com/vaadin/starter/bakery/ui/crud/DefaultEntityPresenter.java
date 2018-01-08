/**
 *
 */
package com.vaadin.starter.bakery.ui.crud;

import com.vaadin.data.provider.CallbackDataProvider;
import com.vaadin.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.starter.bakery.backend.data.entity.AbstractEntity;
import com.vaadin.starter.bakery.backend.data.entity.User;
import com.vaadin.starter.bakery.backend.service.FilterableCrudService;
import com.vaadin.starter.bakery.ui.view.EntityPresenter;
import com.vaadin.starter.bakery.ui.view.EntityView;

public class DefaultEntityPresenter<T extends AbstractEntity> {

	private final ConfigurableFilterDataProvider<T, Void, String> filteredDataProvider;

	private final EntityPresenter<T> entityPresenter;

	public DefaultEntityPresenter(FilterableCrudService<T> crudService, EntityView<T> view, String entityName,
			User currentUser) {
		this.entityPresenter = new EntityPresenter<>(crudService, entityName, currentUser);
		DataProvider<T, String> dataProvider = new CallbackDataProvider<>(
				query -> crudService.findAnyMatching(query.getFilter()).stream(),
				query -> crudService.findAnyMatching(query.getFilter()).size());

		filteredDataProvider = dataProvider.withConfigurableFilter();
		view.setDataProvider(filteredDataProvider);

		this.entityPresenter.onInsert(e -> {
			filteredDataProvider.refreshAll();
			entityPresenter.close();
		});
		this.entityPresenter.onUpdate(e -> {
			filteredDataProvider.refreshAll();
			entityPresenter.close();
		});
		this.entityPresenter.onDelete(e -> {
			filteredDataProvider.refreshItem(e);
			entityPresenter.close();
		});
	}

	public void filter(String filter) {
		filteredDataProvider.setFilter(filter);
	}

	public void save() {
		if (entityPresenter.writeEntity()) {
			entityPresenter.save();
		}
	}

	public void cancel() {
		entityPresenter.cancel();
	}

	public void createNew() {
		entityPresenter.createNew();
	}

	public void delete() {
		entityPresenter.delete();
	}

	public void init(EntityView<T> view) {
		entityPresenter.init(view);
	}

	public void loadEntity(Long id, boolean edit) {
		entityPresenter.loadEntity(id, edit);
	}
}
