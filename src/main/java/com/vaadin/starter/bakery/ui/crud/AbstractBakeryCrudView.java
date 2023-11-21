package com.vaadin.starter.bakery.ui.crud;

import com.vaadin.flow.component.crud.Crud;
import com.vaadin.flow.component.crud.CrudEditor;
import com.vaadin.flow.component.crud.CrudI18n;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.starter.bakery.app.security.CurrentUser;
import com.vaadin.starter.bakery.backend.data.entity.AbstractEntity;
import com.vaadin.starter.bakery.backend.data.entity.util.EntityUtil;
import com.vaadin.starter.bakery.backend.service.FilterableCrudService;
import com.vaadin.starter.bakery.ui.components.SearchBar;
import com.vaadin.starter.bakery.ui.utils.TemplateUtil;
import com.vaadin.starter.bakery.ui.views.HasNotifications;

import java.util.function.Consumer;

public abstract class AbstractBakeryCrudView<E extends AbstractEntity> extends VerticalLayout
        implements HasUrlParameter<Long>, HasNotifications {

    private static final String DISCARD_MESSAGE = "There are unsaved modifications to the %s. Discard changes?";
    private static final String DELETE_MESSAGE = "Are you sure you want to delete the selected %s? This action cannot be undone.";

    private final CrudEntityPresenter<E> entityPresenter;

    private final Crud<E> crud;

    protected abstract String getBasePage();

    protected abstract void setupGrid(Grid<E> grid);

    protected abstract E createItem();

    public AbstractBakeryCrudView(Class<E> beanType, FilterableCrudService<E> service,
                                  Grid<E> grid, CrudEditor<E> editor, CurrentUser currentUser) {
        setHeightFull();
        setPadding(false);
        setSpacing(false);

        crud = new Crud<>(beanType, grid, editor);

        grid.setSelectionMode(Grid.SelectionMode.NONE);

        CrudI18n crudI18n = CrudI18n.createDefault();
        String entityName = EntityUtil.getName(beanType);
        crudI18n.setEditItem("Edit " + entityName);
        crudI18n.setEditLabel("Edit " + entityName);
        crudI18n.getConfirm().getCancel().setContent(String.format(DISCARD_MESSAGE, entityName));
        crudI18n.getConfirm().getDelete().setContent(String.format(DELETE_MESSAGE, entityName));
        crudI18n.setDeleteItem("Delete");
        crud.setI18n(crudI18n);
        crud.setToolbarVisible(false);
        crud.setHeightFull();

        CrudEntityDataProvider<E> dataProvider = new CrudEntityDataProvider<>(service);
        grid.setDataProvider(dataProvider);
        setupGrid(grid);
        Crud.addEditColumn(grid);

        entityPresenter = new CrudEntityPresenter<>(service, currentUser, this);

        SearchBar searchBar = new SearchBar();
        searchBar.setActionText("New " + entityName);
        searchBar.setPlaceHolder("Search");
        searchBar.addFilterChangeListener(e -> dataProvider.setFilter(searchBar.getFilter()));
        searchBar.getActionButton().getElement().setAttribute("new-button", true);
        searchBar.addActionClickListener(e -> {
            crud.edit(createItem(), Crud.EditMode.NEW_ITEM);
        });

        setupCrudEventListeners(entityPresenter);

        add(searchBar, crud);
    }

    private void setupCrudEventListeners(CrudEntityPresenter<E> entityPresenter) {
        Consumer<E> onSuccess = entity -> navigateToEntity(null);
        Consumer<E> onFail = entity -> {
            throw new RuntimeException("The operation could not be performed.");
        };

        crud.addEditListener(e ->
                entityPresenter.loadEntity(e.getItem().getId(),
                        entity -> navigateToEntity(entity.getId().toString())));

        crud.addCancelListener(e -> navigateToEntity(null));

        crud.addSaveListener(e ->
                entityPresenter.save(e.getItem(), onSuccess, onFail));

        crud.addDeleteListener(e ->
                entityPresenter.delete(e.getItem(), onSuccess, onFail));
    }

    protected void navigateToEntity(String id) {
        getUI().ifPresent(ui -> ui.navigate(TemplateUtil.generateLocation(getBasePage(), id)));
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter Long id) {
        if (id != null) {
            E item = crud.getEditor().getItem();
            if (item != null && id.equals(item.getId())) {
                return;
            }
            entityPresenter.loadEntity(id, entity -> crud.edit(entity, Crud.EditMode.EXISTING_ITEM));
        } else {
            crud.setOpened(false);
        }
    }
}
