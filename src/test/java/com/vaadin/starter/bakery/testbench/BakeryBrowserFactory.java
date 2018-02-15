package com.vaadin.starter.bakery.testbench;

import com.vaadin.testbench.parallel.Browser;
import com.vaadin.testbench.parallel.DefaultBrowserFactory;
import org.openqa.selenium.Platform;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

public class BakeryBrowserFactory extends DefaultBrowserFactory {

    @Override
    public DesiredCapabilities create(Browser browser) {
        switch (browser) {
            case SAFARI:
                return create(Browser.SAFARI, "11", Platform.EL_CAPITAN);
            case EDGE:
                return create(Browser.EDGE, "", Platform.WINDOWS);
            case IE11:
                return createIE(browser, "11");
            case CHROME:
                return create(browser, "", Platform.WINDOWS);
            case FIREFOX:
            default:
                return createFirefox();
        }
    }

    private DesiredCapabilities createFirefox() {
        DesiredCapabilities capabilities = create(Browser.FIREFOX,"", Platform.WINDOWS);
        capabilities.setCapability(FirefoxDriver.MARIONETTE, false);
        return capabilities;
    }

    private DesiredCapabilities createIE(Browser browser, String version) {
        DesiredCapabilities capabilities = create(browser, version,
                Platform.WINDOWS);
        capabilities.setCapability(
                InternetExplorerDriver.IE_ENSURE_CLEAN_SESSION, true);
        return capabilities;
    }

}
