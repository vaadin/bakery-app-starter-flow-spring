package com.vaadin.flow.demo.patientportal.ui;

import org.springframework.security.access.annotation.Secured;

import com.vaadin.annotations.HtmlImport;
import com.vaadin.annotations.Tag;
import com.vaadin.flow.demo.patientportal.backend.data.Role;
import com.vaadin.flow.demo.patientportal.ui.utils.BakeryConst;
import com.vaadin.flow.router.View;
import com.vaadin.flow.template.PolymerTemplate;
import com.vaadin.flow.template.model.TemplateModel;
import com.vaadin.hummingbird.ext.spring.annotations.ParentView;
import com.vaadin.hummingbird.ext.spring.annotations.Route;

@Tag("bakery-admin")
@HtmlImport("frontend://src/admin/bakery-admin.html")
@Route(BakeryConst.PAGE_ADMIN)
@ParentView(BakeryApp.class)
@Secured(Role.ADMIN)
public class AdminView extends PolymerTemplate<TemplateModel> implements View {

}
