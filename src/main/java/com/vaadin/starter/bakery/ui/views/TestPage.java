package com.vaadin.starter.bakery.ui.views;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.starter.bakery.ui.components.GridComponent;

@Route("test")
class TestPage extends VerticalLayout {

    @Autowired
    private GridComponent grid;

    @PostConstruct
    public void init() {
        add(grid);
    }
}
