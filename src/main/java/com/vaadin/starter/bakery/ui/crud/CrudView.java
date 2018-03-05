/**
 *
 */
package com.vaadin.starter.bakery.ui.crud;

import com.vaadin.flow.component.HasText;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.templatemodel.TemplateModel;
import com.vaadin.starter.bakery.app.HasLogger;
import com.vaadin.starter.bakery.backend.data.entity.AbstractEntity;
import com.vaadin.starter.bakery.ui.components.FormButtonsBar;
import com.vaadin.starter.bakery.ui.components.SearchBar;
import com.vaadin.starter.bakery.ui.utils.TemplateUtil;
import com.vaadin.starter.bakery.ui.views.EntityView;

public abstract class CrudView<E extends AbstractEntity, T extends TemplateModel> extends PolymerTemplate<T>
		implements HasLogger, EntityView<E>, HasUrlParameter<Long> {

	public interface CrudForm<E> {
		FormButtonsBar getButtons();

		HasText getTitle();

		void setBinder(BeanValidationBinder<E> binder);
	}

	private final String entityName;

	protected abstract DefaultEntityPresenter<E> getPresenter();

	protected abstract String getBasePage();

	protected abstract BeanValidationBinder<E> getBinder();

	protected abstract CrudForm<E> getForm();

	protected abstract Dialog getDialog();

	protected abstract SearchBar getSearchBar();

	protected abstract Grid<E> getGrid();

	public CrudView(String entityName) {
		this.entityName = entityName;
	}


	public void setupEventListeners() {
		getGrid().addSelectionListener(e -> {
			e.getFirstSelectedItem().ifPresent(entity -> {
				navigateToEntity(entity.getId().toString());
				getGrid().deselectAll();
			});
		});

		getForm().getButtons().addSaveListener(e -> getPresenter().save());
		getForm().getButtons().addCancelListener(e -> getPresenter().cancel());

		getDialog().getElement().addEventListener("opened-changed", e -> {
			if (!getDialog().isOpened()) {
				getPresenter().cancel();
			}
		});

		getForm().getButtons().addDeleteListener(e -> getPresenter().delete());

		getSearchBar().addActionClickListener(e -> getPresenter().createNew());
		getSearchBar()
				.addFilterChangeListener(e -> getPresenter().filter(getSearchBar().getFilter()));

		getSearchBar().setActionText("New " + entityName);
		getBinder().addValueChangeListener(e -> getPresenter().onValueChange(isDirty()));
	}

	protected void navigateToEntity(String id) {
		getUI().ifPresent(ui -> ui.navigate(TemplateUtil.generateLocation(getBasePage(), id)));
	}

	@Override
	public void setParameter(BeforeEvent event, @OptionalParameter Long id) {
		if (id != null) {
			getPresenter().loadEntity(id, true);
		} else if (getDialog().isOpened()) {
		    getPresenter().closeSilently();
        }
	}

	public void closeDialog() {
		getDialog().setOpened(false);
		navigateToEntity(null);
	}

	public void openDialog() {
		getDialog().setOpened(true);
	}

	public void updateTitle(boolean newEntity) {
		getForm().getTitle().setText((newEntity ? "New" : "Edit") + " " + entityName);
	}

	@Override
	public void write(E entity) throws ValidationException {
		getBinder().writeBean(entity);
	}

	@Override
	public boolean isDirty() {
		return getBinder().hasChanges();
	}

	@Override
	public void clear() {
		getBinder().readBean(null);
	}
}
