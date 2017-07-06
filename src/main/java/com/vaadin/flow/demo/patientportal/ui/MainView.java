/*
 * Copyright 2000-2017 Vaadin Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.vaadin.flow.demo.patientportal.ui;

import com.vaadin.flow.router.LocationChangeEvent;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.demo.patientportal.backend.service.UserService;
import com.vaadin.flow.html.Div;
import com.vaadin.flow.router.HasChildView;
import com.vaadin.flow.router.View;
import com.vaadin.hummingbird.ext.spring.annotations.UIScope;

import javax.annotation.Resource;

/**
 * @author Vaadin Ltd
 *
 */
@SuppressWarnings("serial")
@UIScope
public class MainView extends Composite<Div> implements HasChildView {

    // TODO : to remove, not needed. Just verifies the existing backend is
    // working
    @Autowired
    private UserService service;

    @Resource
    private BakeryNavigation navigation;
    private Div childContainer = new Div();

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        getContent().add(navigation, childContainer);
		//TODO: move this UI to frontend
		getElement().getStyle().set("height", "100%");
		childContainer.getElement().getStyle().set("height", "100%");
    }

    @Override
    public void onLocationChange(LocationChangeEvent event) {
        navigation.onLocationChange(event);
    }

    @Override
    public void setChildView(View childView) {
        childContainer.removeAll();
        childContainer.add((Component) childView);
    }
}
