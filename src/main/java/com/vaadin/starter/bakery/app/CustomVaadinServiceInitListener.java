package com.vaadin.starter.bakery.app;

import com.vaadin.server.BootstrapListener;
import com.vaadin.server.DependencyFilter;
import com.vaadin.server.ServiceInitEvent;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinServiceInitListener;
import com.vaadin.shared.ui.Dependency;
import com.vaadin.shared.ui.LoadMode;
import com.vaadin.spring.annotation.SpringComponent;

/**
 * Configures the {@link VaadinService}:
 * <ul>
 *   <li>adds a {@link BootstrapListener} to add favicons, viewport,
 *   etc to the initial HTML sent to the browser (see {@link CustomBootstrapListener})</li>
 *   <li>adds a {@link DependencyFilter} to allow dependency bundling
 *   in the production mode (when all individual .html dependencies are combined a single
 *   file to improve the page load performance)</li>
 * </ul>
 */
@SpringComponent
public class CustomVaadinServiceInitListener implements VaadinServiceInitListener {

    @Override
    public void serviceInit(ServiceInitEvent event) {
        event.addBootstrapListener(new CustomBootstrapListener());
        event.addDependencyFilter((dependencies, filterContext) -> {
            if (filterContext.getService().getDeploymentConfiguration().isProductionMode()) {
                dependencies.removeIf(e -> e.getType().equals(Dependency.Type.HTML_IMPORT));
                dependencies.add(new Dependency(Dependency.Type.HTML_IMPORT,
                        "src/app/bakery-app.html", LoadMode.EAGER));
            }
            return dependencies;
        });
    }
}