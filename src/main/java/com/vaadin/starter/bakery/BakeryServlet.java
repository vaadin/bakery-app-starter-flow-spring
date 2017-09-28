package com.vaadin.starter.bakery;

import com.vaadin.function.DeploymentConfiguration;
import com.vaadin.server.VaadinServletConfiguration;
import com.vaadin.hummingbird.ext.spring.SpringAwareConfigurator;
import com.vaadin.hummingbird.ext.spring.SpringAwareVaadinService;
import com.vaadin.server.ServiceException;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinServletService;
import com.vaadin.starter.bakery.ui.AccessDeniedView;
import com.vaadin.starter.bakery.ui.NotFoundView;

/**
 * 
 * The main servlet for the application.
 *
 */
@VaadinServletConfiguration(routerConfigurator = SpringAwareConfigurator.class, productionMode = false, usingNewRouting = false)
public class BakeryServlet extends VaadinServlet {

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
