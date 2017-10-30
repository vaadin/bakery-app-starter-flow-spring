/**
 *
 */
package com.vaadin.starter.bakery.ui.presenter;

import com.vaadin.data.provider.CallbackDataProvider;
import com.vaadin.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.starter.bakery.backend.data.entity.AbstractEntity;
import com.vaadin.starter.bakery.backend.service.FilterableCrudService;

public class DefaultEntityPresenter<T extends AbstractEntity> extends EntityPresenter<T> {

	private final ConfigurableFilterDataProvider<T, String, String> filteredDataProvider;

	public DefaultEntityPresenter(FilterableCrudService<T> crudService, EntityView<T> view, String entityName) {
		super(crudService, view, entityName);

		DataProvider<T, String> dataProvider = new CallbackDataProvider<>(
				query -> crudService.findAnyMatching(query.getFilter()).stream(),
				query -> crudService.findAnyMatching(query.getFilter()).size());

		filteredDataProvider = dataProvider.withConfigurableFilter((String queryFilter, String filter) -> filter);
		view.setDataProvider(filteredDataProvider);
	}

	public void filter(String filter) {
		filteredDataProvider.setFilter(filter);
	}

	@Override
	protected void onSaveSuccess() {
		if (isNew) {
			filteredDataProvider.refreshAll();
		} else {
			filteredDataProvider.refreshItem(getEntity());
		}
		super.onSaveSuccess();
	}

	@Override
	protected void onDeleteSuccess() {
		filteredDataProvider.refreshAll();
		super.onDeleteSuccess();
	}

}
