/**
 *
 */
package com.vaadin.starter.bakery.ui.crud;

import com.vaadin.data.BeanValidationBinder;
import com.vaadin.data.ValidationException;
import com.vaadin.flow.model.TemplateModel;
import com.vaadin.router.HasUrlParameter;
import com.vaadin.router.OptionalParameter;
import com.vaadin.router.event.BeforeNavigationEvent;
import com.vaadin.starter.bakery.app.HasLogger;
import com.vaadin.starter.bakery.backend.data.entity.AbstractEntity;
import com.vaadin.starter.bakery.ui.components.BakerySearch;
import com.vaadin.starter.bakery.ui.components.FormButtonsBar;
import com.vaadin.starter.bakery.ui.components.FormDialog;
import com.vaadin.starter.bakery.ui.event.CloseDialogEvent;
import com.vaadin.starter.bakery.ui.utils.TemplateUtil;
import com.vaadin.starter.bakery.ui.view.EntityView;
import com.vaadin.ui.common.HasText;
import com.vaadin.ui.grid.Grid;
import com.vaadin.ui.polymertemplate.PolymerTemplate;

public abstract class CrudView<E extends AbstractEntity, T extends TemplateModel> extends PolymerTemplate<T>
		implements HasLogger, EntityView<E>, HasUrlParameter<Long> {

	private final String entityName;
	
	protected abstract DefaultEntityPresenter<E> getPresenter();

	protected abstract String getBasePage();

	protected abstract BeanValidationBinder<E> getBinder();

	protected abstract FormButtonsBar getButtons();

	protected abstract FormDialog getDialog();

	protected abstract BakerySearch getSearchBar();

	protected abstract Grid<E> getGrid();

	protected abstract HasText getTitle();

	public CrudView(String entityName) {
		this.entityName = entityName;
	}
	
	public void setupEventListeners() {
		getGrid().addSelectionListener(e -> {
			e.getFirstSelectedItem().ifPresent(entity -> navigateToEntity(entity.getId().toString()));
			getGrid().deselectAll();
		});
		addListener(CloseDialogEvent.class, e -> getPresenter().cancel());

		getButtons().addSaveListener(e -> getPresenter().save());
		getButtons().addCancelListener(e -> getPresenter().cancel());
		getButtons().addDeleteListener(e -> getPresenter().delete());

		getSearchBar().addActionClickListener(e -> getPresenter().createNew());
		getSearchBar()
				.addFilterChangeListener(e -> getPresenter().filter(getSearchBar().getFilter()));

		getSearchBar().setActionText("New " + entityName);
		getBinder().addValueChangeListener(e -> getPresenter().onValueChange(isDirty()));
	}

	protected void navigateToEntity(String id) {
		getUI().ifPresent(ui -> ui.navigateTo(TemplateUtil.generateLocation(getBasePage(), id)));
	}

	@Override
	public void setParameter(BeforeNavigationEvent event, @OptionalParameter Long id) {
		if (id != null) {
			getPresenter().loadEntity(id, true);
		}
	}

	public void closeDialog() {
		getDialog().setOpened(false);
		navigateToEntity(null);
	}

	public void updateTitle(boolean newEntity) {
		getTitle().setText((newEntity ? "New" : "Edit") + " " + entityName);
	}

	@Override
	public void write(E entity) throws ValidationException {
		getBinder().writeBean(entity);
	}

	@Override
	public boolean isDirty() {
		return getBinder().hasChanges();
	}
	
}
