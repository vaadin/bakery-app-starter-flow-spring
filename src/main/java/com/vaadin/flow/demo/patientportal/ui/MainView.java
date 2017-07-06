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

import com.vaadin.annotations.HtmlImport;
import com.vaadin.annotations.Id;
import com.vaadin.annotations.Tag;
import com.vaadin.flow.router.HasChildView;
import com.vaadin.flow.router.LocationChangeEvent;
import com.vaadin.flow.router.View;
import com.vaadin.flow.template.PolymerTemplate;
import com.vaadin.flow.template.model.TemplateModel;
import com.vaadin.hummingbird.ext.spring.annotations.UIScope;

/**
 * @author Vaadin Ltd
 *
 */
@SuppressWarnings("serial")
@UIScope
@Tag("main-view")
@HtmlImport("frontend://src/main-view.html")
public class MainView extends PolymerTemplate<TemplateModel> implements HasChildView {

    /*
     * TODO (2017.07.06, vlukashov): set src/main-view.html as the entry point in polymer.json
     * after the Flow issue #1969 is fixed (https://github.com/vaadin/flow/issues/1969).
     */

    @Id("navigation")
    private BakeryNavigation navigation;

    @Override
    public void onLocationChange(LocationChangeEvent event) {
        navigation.onLocationChange(event);
    }

    @Override
    public void setChildView(View childView) {
        getElement().removeAllChildren();
        getElement().appendChild(childView.getElement());
    }
}
