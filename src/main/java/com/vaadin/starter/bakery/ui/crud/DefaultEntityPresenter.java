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

public class DefaultEntityPresenter<T extends AbstractEntity> extends EntityPresenter<T> {

	private final ConfigurableFilterDataProvider<T, Void, String> filteredDataProvider;

	public DefaultEntityPresenter(FilterableCrudService<T> crudService, EntityView<T> view, String entityName,
			User currentUser) {
		super(crudService, view, entityName, currentUser);

		DataProvider<T, String> dataProvider = new CallbackDataProvider<>(
				query -> crudService.findAnyMatching(query.getFilter()).stream(),
				query -> crudService.findAnyMatching(query.getFilter()).size());

		filteredDataProvider = dataProvider.withConfigurableFilter();
		view.setDataProvider(filteredDataProvider);
	}

	public void filter(String filter) {
		filteredDataProvider.setFilter(filter);
	}

	@Override
	protected void onSaveSuccess(boolean isNew) {
		if (isNew) {
			filteredDataProvider.refreshAll();
		} else {
			filteredDataProvider.refreshItem(getEntity());
		}
		super.onSaveSuccess(isNew);
	}

	@Override
	protected void onDeleteSuccess() {
		filteredDataProvider.refreshAll();
		super.onDeleteSuccess();
	}

}
