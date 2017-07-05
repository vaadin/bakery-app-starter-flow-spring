package com.vaadin.flow.demo.patientportal.ui;

import com.vaadin.annotations.HtmlImport;
import com.vaadin.annotations.Tag;
import com.vaadin.flow.router.LocationChangeEvent;
import com.vaadin.flow.router.View;
import com.vaadin.flow.template.PolymerTemplate;
import com.vaadin.flow.template.model.TemplateModel;
import com.vaadin.hummingbird.ext.spring.annotations.ParentView;
import com.vaadin.hummingbird.ext.spring.annotations.Route;

/**
 * Created by viktor on 03/07/2017.
 */
@Tag("bakery-app")
@HtmlImport("frontend://src/app/bakery-app.html")
@Route(value = "*")
@ParentView(MainView.class)
public class BakeryApp extends PolymerTemplate<BakeryApp.Model> implements View {

    private static final String DEFAULT_VIEW = "storefront";

    public interface Model extends TemplateModel {
        void setPage(String page);
    }

    @Override
    public void onLocationChange(LocationChangeEvent event) {
        String page = event.getLocation().getPath();
        if ("".equals(page)) {
            page = DEFAULT_VIEW;
        }
        getModel().setPage(page);
    }
}