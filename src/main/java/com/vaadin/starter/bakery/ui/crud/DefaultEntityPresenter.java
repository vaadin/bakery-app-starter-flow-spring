/**
 *
 */
package com.vaadin.starter.bakery.ui.crud;

import com.vaadin.data.provider.CallbackDataProvider;
import com.vaadin.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.starter.bakery.backend.data.entity.AbstractEntity;
import com.vaadin.starter.bakery.backend.service.FilterableCrudService;
import com.vaadin.starter.bakery.ui.event.CloseDialogEvent;
import com.vaadin.starter.bakery.ui.view.CrudOperationListener;
import com.vaadin.starter.bakery.ui.view.EntityPresenter;

public abstract class DefaultEntityPresenter<T extends AbstractEntity> {

	private final ConfigurableFilterDataProvider<T, Void, String> filteredDataProvider;

	private final EntityPresenter<T> entityPresenter;

	private final String entityName;

	public DefaultEntityPresenter(EntityPresenter<T> entityPresenter, FilterableCrudService<T> crudService, String entityName) {
		this.entityPresenter = entityPresenter;
		this.entityName = entityName;
		DataProvider<T, String> dataProvider = new CallbackDataProvider<>(
				query -> crudService.findAnyMatching(query.getFilter()).stream(),
				query -> crudService.findAnyMatching(query.getFilter()).size());

		filteredDataProvider = dataProvider.withConfigurableFilter();
	}

	public void filter(String filter) {
		filteredDataProvider.setFilter(filter);
	}

	public void cancel() {
		entityPresenter.cancel();
	}

	public void createNew() {
		entityPresenter.createNew();
	}

	public void init(CrudView<T, ?> view) {
		entityPresenter.setView(view);
		view.setEntityName(entityName);
		view.setDataProvider(filteredDataProvider);
		view.getGrid().addSelectionListener(e -> {
			e.getFirstSelectedItem().ifPresent(entity -> view.navigateToEntity(entity.getId().toString()));
			view.getGrid().deselectAll();
		});
		view.addListener(CloseDialogEvent.class, e -> entityPresenter.cancel());

		view.getButtons().addSaveListener(e -> save());
		view.getButtons().addCancelListener(e -> entityPresenter.cancel());
		view.getButtons().addDeleteListener(e -> delete());

		view.getSearchBar().addActionClickListener(e -> entityPresenter.createNew());
		view.getSearchBar()
				.addFilterChangeListener(e -> filteredDataProvider.setFilter(view.getSearchBar().getFilter()));

		view.getSearchBar().setActionText("New " + entityName);
		view.getBinder().addValueChangeListener(e -> view.getButtons().setSaveDisabled(!view.isDirty()));
	}

	public void loadEntity(Long id, boolean edit) {
		entityPresenter.loadEntity(id, edit);
	}

	private void save() {
		if (entityPresenter.writeEntity()) {
			CrudOperationListener<T> onSaveSuccess = e -> {
				filteredDataProvider.refreshAll();
				entityPresenter.close();
			};
			entityPresenter.save(onSaveSuccess);
		}
	}

	private void delete() {
		entityPresenter.delete(e -> {
			filteredDataProvider.refreshItem(e);
			entityPresenter.close();
		});
	}
}
