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
@HtmlImport("/src/app/style-modules/shared-styles.html")
@HtmlImport("/bower_components/vaadin-themes/valo/typography.html")
@HtmlImport("/bower_components/vaadin-themes/valo/color.html")
@HtmlImport("/bower_components/vaadin-themes/valo/sizing-and-spacing.html")
@HtmlImport("/bower_components/vaadin-themes/valo/style.html")
@HtmlImport("/bower_components/vaadin-themes/valo/icons.html")
@HtmlImport("/src/app/bakery-app.html")
@Route(value = "*")
@ParentView(MainView.class)
public class BakeryApp extends PolymerTemplate<BakeryApp.Model> implements View {
    public interface Model extends TemplateModel {
        void setPage(String page);
    }

    @Override
    public void onLocationChange(LocationChangeEvent event) {
        getModel().setPage(event.getLocation().getPath());
    }
}