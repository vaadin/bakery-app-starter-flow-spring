/**
 *
 */
package com.vaadin.starter.bakery.ui.presenter;

import java.util.Optional;

import com.vaadin.starter.bakery.backend.data.entity.AbstractEntity;
import com.vaadin.starter.bakery.backend.service.FilterableCrudService;

public class DefaultEntityPresenter<T extends AbstractEntity> extends EntityPresenter<T> {

	private FilterableCrudService<T> crudService;
	private EntityView<T> view;

	public DefaultEntityPresenter(FilterableCrudService<T> crudService, EntityView<T> view, String entityName) {
		super(crudService, view, entityName);
		this.crudService = crudService;
		this.view = view;
		filter(Optional.empty());
	}

	public void filter(Optional<String> filter) {
		view.setListItems(crudService.findAnyMatching(filter));
	}

	@Override
	protected void onSaveSuccess() {
		filter(Optional.empty());
		super.onSaveSuccess();
	}

	@Override
	protected void onDeleteSuccess() {
		filter(Optional.empty());
		super.onDeleteSuccess();
	}

}
