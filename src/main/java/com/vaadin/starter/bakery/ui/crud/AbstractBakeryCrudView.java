package com.vaadin.starter.bakery.ui.crud;

import com.vaadin.flow.component.crud.Crud;
import com.vaadin.flow.component.crud.CrudEditor;
import com.vaadin.flow.component.crud.CrudI18n;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.starter.bakery.app.security.CurrentUser;
import com.vaadin.starter.bakery.backend.data.entity.AbstractEntity;
import com.vaadin.starter.bakery.backend.service.FilterableCrudService;
import com.vaadin.starter.bakery.ui.components.SearchBar;
import com.vaadin.starter.bakery.ui.utils.TemplateUtil;
import com.vaadin.starter.bakery.ui.views.HasNotifications;
import elemental.json.Json;

import java.util.function.Consumer;

public abstract class AbstractBakeryCrudView<E extends AbstractEntity> extends Crud<E>
        implements HasUrlParameter<Long>, HasNotifications {

    private static final String DISCARD_MESSAGE = "There are unsaved modifications to the %s. Discard changes?";
    private static final String DELETE_MESSAGE = "Are you sure you want to delete the selected %s? This action cannot be undone.";

    private final Grid<E> grid;

    private final CrudEntityPresenter<E> entityPresenter;

    protected abstract String getBasePage();

    protected abstract void setupGrid(Grid<E> grid);

    public AbstractBakeryCrudView(Class<E> beanType, FilterableCrudService<E> service,
                                  Grid<E> grid, CrudEditor editor, CurrentUser currentUser) {
        super(beanType, grid, editor);
        this.grid = grid;
        grid.setSelectionMode(Grid.SelectionMode.NONE);

        CrudI18n crudI18n = CrudI18n.createDefault();
        crudI18n.setNewItem("New " + beanType.getSimpleName());
        crudI18n.setEditItem("Edit " + beanType.getSimpleName());
        crudI18n.setEditLabel("Edit " + beanType.getSimpleName());
        crudI18n.getConfirm().getCancel().setContent(String.format(DISCARD_MESSAGE, beanType.getSimpleName()));
        crudI18n.getConfirm().getDelete().setContent(String.format(DELETE_MESSAGE, beanType.getSimpleName()));
        setI18n(crudI18n);

        CrudEntityDataProvider<E> dataProvider = new CrudEntityDataProvider<>(service);
        grid.setDataProvider(dataProvider);
        setupGrid(grid);
        Crud.addEditColumn(grid);

        entityPresenter = new CrudEntityPresenter<>(service, currentUser, this);

        SearchBar searchBar = new SearchBar();
        searchBar.setActionText("New " + beanType.getSimpleName());
        searchBar.addFilterChangeListener(e -> dataProvider.setFilter(searchBar.getFilter()));
        searchBar.getActionButton().getElement().setAttribute("slot", "new");

        setFooter(searchBar);
        setupCrudEventListeners(entityPresenter);
    }

    private void setupCrudEventListeners(CrudEntityPresenter<E> entityPresenter) {
        Consumer<E> onSuccess = entity -> navigateToEntity(null);
        Consumer<E> onFail = entity -> {
            // TODO: https://github.com/vaadin/vaadin-crud-flow/issues/76
            // Throw an exception whenever it is supported by component
        };

        addEditListener(e ->
                entityPresenter.loadEntity(e.getItem().getId(),
                        entity -> navigateToEntity(entity.getId().toString())));

        addCancelListener(e -> navigateToEntity(null));

        addSaveListener(e ->
                entityPresenter.save(e.getItem(), onSuccess, onFail));

        addDeleteListener(e ->
                entityPresenter.delete(e.getItem(), onSuccess, onFail));
    }

    protected void navigateToEntity(String id) {
        getUI().ifPresent(ui -> ui.navigate(TemplateUtil.generateLocation(getBasePage(), id)));
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter Long id) {
        if (id != null) {
            E item = getEditor().getItem();
            if (item != null && id.equals(item.getId())) {
                return;
            }
            entityPresenter.loadEntity(id, this::edit);
        }
    }

    private void edit(E entity) {
        // TODO: Use component API after https://github.com/vaadin/vaadin-crud-flow/issues/68
        getElement().callFunction("__edit", Json.instance().parse("{\"key\":\""
                + grid.getDataCommunicator().getKeyMapper().key(entity) + "\"}"));
    }
}
