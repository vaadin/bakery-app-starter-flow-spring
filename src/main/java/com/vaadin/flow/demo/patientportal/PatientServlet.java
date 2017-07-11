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

package com.vaadin.flow.demo.patientportal;

import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.flow.demo.patientportal.ui.AccessDeniedView;
import com.vaadin.flow.demo.patientportal.ui.NotFoundView;
import com.vaadin.hummingbird.ext.spring.SpringAwareConfigurator;
import com.vaadin.hummingbird.ext.spring.SpringAwareVaadinService;
import com.vaadin.server.DeploymentConfiguration;
import com.vaadin.server.ServiceException;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinServletService;

/**
 * The main servlet for the application.
 *
 *
 * @author Vaadin Ltd
 *
 */
@VaadinServletConfiguration(routerConfigurator = SpringAwareConfigurator.class, productionMode = false)
public class PatientServlet extends VaadinServlet {

    @Override
    protected VaadinServletService createServletService(
            DeploymentConfiguration deploymentConfiguration)
            throws ServiceException {

        SpringAwareConfigurator.setAccessForbiddenView(AccessDeniedView.class);
        SpringAwareConfigurator.setFileNotFoundView(NotFoundView.class);
        VaadinServletService service = new SpringAwareVaadinService(this,
                deploymentConfiguration);
        service.init();

        return service;
    }
}
