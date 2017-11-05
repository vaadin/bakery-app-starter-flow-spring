/**
 *
 */
package com.vaadin.starter.bakery.ui.view.admin;

import com.vaadin.data.ValidationException;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.flow.model.TemplateModel;
import com.vaadin.router.HasUrlParameter;
import com.vaadin.router.OptionalParameter;
import com.vaadin.router.event.BeforeNavigationEvent;
import com.vaadin.starter.bakery.app.HasLogger;
import com.vaadin.starter.bakery.backend.data.entity.AbstractEntity;
import com.vaadin.starter.bakery.ui.event.CancelEvent;
import com.vaadin.starter.bakery.ui.event.CloseDialogEvent;
import com.vaadin.starter.bakery.ui.event.DeleteEvent;
import com.vaadin.starter.bakery.ui.event.SaveEvent;
import com.vaadin.starter.bakery.ui.view.EntityView;
import com.vaadin.ui.grid.Grid;
import com.vaadin.ui.polymertemplate.PolymerTemplate;

public abstract class PolymerEntityView<E extends AbstractEntity, T extends TemplateModel> extends PolymerTemplate<T>
		implements HasLogger, EntityView<E>, HasUrlParameter<Long> {

	protected void setupEventListeners() {
		getGrid().addSelectionListener(e -> {
			e.getFirstSelectedItem().ifPresent(entity -> navigateToEntity(entity.getId().toString()));
			getGrid().deselectAll();
		});
		addListener(CloseDialogEvent.class, e -> getPresenter().cancel());
		getEditor().addListener(CancelEvent.class, e -> getPresenter().cancel());
		getEditor().addListener(SaveEvent.class, e -> getPresenter().save());
		getEditor().addListener(DeleteEvent.class, e -> getPresenter().delete());
		getItemsView().addActionClickListener(e -> getPresenter().createNew());
		getItemsView().addFilterChangeListener(f -> getPresenter().filter(f));
	}

	protected abstract DefaultEntityPresenter<E> getPresenter();

	protected abstract String getBasePage();

	protected abstract EntityEditor<E> getEditor();

	protected abstract ItemsView getItemsView();

	protected abstract Grid<E> getGrid();

	@Override
	public void setDataProvider(DataProvider<E, Void> dataProvider) {
		getGrid().setDataProvider(dataProvider);
	}

	protected void navigateToEntity(String id) {
		final String location = getBasePage() + (id == null || id.isEmpty() ? "" : "/" + id);
		getUI().ifPresent(ui -> ui.navigateTo(location));
	}

	@Override
	public void setParameter(BeforeNavigationEvent event, @OptionalParameter Long id) {
		if (id != null) {
			getPresenter().loadEntity(id, true);
		}
	}

	@Override
	public void closeDialog() {
		getItemsView().openDialog(false);
		navigateToEntity(null);
	}

	@Override
	public void openDialog(E entity, boolean edit) {
		getEditor().read(entity);
		getItemsView().openDialog(true);
	}

	@Override
	public boolean isDirty() {
		return getEditor().isDirty();
	}

	@Override
	public void write(E entity) throws ValidationException {
		getEditor().write(entity);
	}

}
