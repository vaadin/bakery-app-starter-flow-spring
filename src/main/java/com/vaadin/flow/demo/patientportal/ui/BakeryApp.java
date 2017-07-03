package com.vaadin.flow.demo.patientportal.ui;

import com.vaadin.annotations.HtmlImport;
import com.vaadin.annotations.Tag;
import com.vaadin.flow.router.View;
import com.vaadin.flow.template.PolymerTemplate;
import com.vaadin.flow.template.model.TemplateModel;
import com.vaadin.hummingbird.ext.spring.annotations.ParentView;
import com.vaadin.hummingbird.ext.spring.annotations.Route;

/**
 * Created by viktor on 03/07/2017.
 */
@Tag("bakery-app")
@HtmlImport("src/app/bakery-app.html")
@Route(value = "")
@ParentView(MainView.class)
public class BakeryApp extends PolymerTemplate<TemplateModel> implements View {
}