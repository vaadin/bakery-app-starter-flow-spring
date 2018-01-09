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
import com.vaadin.starter.bakery.ui.event.CloseDialogEvent;
import com.vaadin.starter.bakery.ui.view.CrudOperationListener;
import com.vaadin.starter.bakery.ui.view.EntityPresenter;

public class DefaultEntityPresenter<T extends AbstractEntity> {

	private final ConfigurableFilterDataProvider<T, Void, String> filteredDataProvider;

	private final EntityPresenter<T> entityPresenter;

	private final String entityName;
	
	public DefaultEntityPresenter(FilterableCrudService<T> crudService, String entityName,
			User currentUser) {
		this.entityPresenter = new EntityPresenter<>(crudService, entityName, currentUser);
		this.entityName = entityName;
		DataProvider<T, String> dataProvider = new CallbackDataProvider<>(
				query -> crudService.findAnyMatching(query.getFilter()).stream(),
				query -> crudService.findAnyMatching(query.getFilter()).size());

		filteredDataProvider = dataProvider.withConfigurableFilter();

		CrudOperationListener<T> refreshAllAndClose = e -> {
			filteredDataProvider.refreshAll();
			entityPresenter.close();
		};
		this.entityPresenter.onInsert(refreshAllAndClose);
		this.entityPresenter.onUpdate(refreshAllAndClose);
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

	public void init(CrudView<T, ?> view) {
		entityPresenter.init(view);
		view.setEntityName(entityName);
		view.setDataProvider(filteredDataProvider);
		view.getGrid().addSelectionListener(e -> {
			e.getFirstSelectedItem().ifPresent(entity -> view.navigateToEntity(entity.getId().toString()));
			view.getGrid().deselectAll();
		});
		view.addListener(CloseDialogEvent.class, e -> entityPresenter.cancel());

		view.getButtons().addSaveListener(e -> entityPresenter.save());
		view.getButtons().addCancelListener(e -> entityPresenter.cancel());
		view.getButtons().addDeleteListener(e -> entityPresenter.delete());

		view.getSearchBar().addActionClickListener(e -> entityPresenter.createNew());
		view.getSearchBar()
				.addFilterChangeListener(e -> filteredDataProvider.setFilter(view.getSearchBar().getFilter()));

		view.getSearchBar().setActionText("New " + entityName);
		view.getBinder().addValueChangeListener(e -> view.getButtons().setSaveDisabled(!view.isDirty()));
	}

	public void loadEntity(Long id, boolean edit) {
		entityPresenter.loadEntity(id, edit);
	}
	
}
