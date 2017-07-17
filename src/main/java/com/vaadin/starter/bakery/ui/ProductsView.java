package com.vaadin.starter.bakery.ui;

import org.springframework.security.access.annotation.Secured;

import com.vaadin.annotations.HtmlImport;
import com.vaadin.annotations.Tag;
import com.vaadin.flow.router.View;
import com.vaadin.flow.template.PolymerTemplate;
import com.vaadin.flow.template.model.TemplateModel;
import com.vaadin.hummingbird.ext.spring.annotations.ParentView;
import com.vaadin.hummingbird.ext.spring.annotations.Route;
import com.vaadin.starter.bakery.backend.data.Role;
import com.vaadin.starter.bakery.ui.utils.BakeryConst;

@Tag("bakery-products")
@HtmlImport("frontend://src/products/bakery-products.html")
@Route(BakeryConst.PAGE_PRODUCTS)
@ParentView(BakeryApp.class)
@Secured(Role.ADMIN)
public class ProductsView extends PolymerTemplate<TemplateModel> implements View {

}
