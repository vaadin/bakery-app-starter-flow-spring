package com.vaadin.starter.bakery.testbench;

import com.vaadin.testbench.addons.junit5.extensions.container.ContainerInfo;
import com.vaadin.testbench.addons.webdriver.BrowserType;
import com.vaadin.testbench.configuration.Target;
import com.vaadin.testbench.configuration.TestConfiguration;
import org.openqa.selenium.Platform;

import java.util.Collections;
import java.util.List;

public class ITConfiguration implements TestConfiguration {

    @Override
    public List<Target> getBrowserTargets() {
        return Collections.singletonList(
                TestConfiguration.saucelabs(BrowserType.CHROME, "75", Platform.LINUX)
//                TestConfiguration.localChrome(".driver/osx/googlechrome/64bit/chromedriver", true)
        );
    }

    @Override
    public ContainerInfo getContainerInfo() {
        return TestConfiguration.defaultContainerInfo();
    }
}
