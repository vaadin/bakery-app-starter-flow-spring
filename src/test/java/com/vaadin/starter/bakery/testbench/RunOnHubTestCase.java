package com.vaadin.starter.bakery.testbench;

import com.vaadin.testbench.Parameters;
import com.vaadin.testbench.TestBenchDriverProxy;
import com.vaadin.testbench.annotations.BrowserConfiguration;
import com.vaadin.testbench.annotations.BrowserFactory;
import com.vaadin.testbench.annotations.RunOnHub;
import com.vaadin.testbench.commands.TestBenchCommands;
import com.vaadin.testbench.parallel.Browser;
import com.vaadin.testbench.parallel.ParallelTest;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

@RunOnHub("tb3-hub.intra.itmill.com")
@BrowserFactory(BakeryBrowserFactory.class)
public abstract class RunOnHubTestCase extends ParallelTest {

    private static final String REF_IMAGE_ROOT = "src/test/resources/screenshots/reference";
    protected static final String ERROR_IMAGE_ROOT = "target/testbench/errors/";

    @Override
    public void setup() throws Exception {
        super.setup();

        new File(ERROR_IMAGE_ROOT).mkdirs();
        Parameters.setScreenshotErrorDirectory(ERROR_IMAGE_ROOT);
        Parameters.setScreenshotComparisonTolerance(0.05);
        Parameters.setScreenshotReferenceDirectory(REF_IMAGE_ROOT);
    }

    @BrowserConfiguration
    public List<DesiredCapabilities> getBrowsersToTest() {
        List<DesiredCapabilities> allBrowsers = new ArrayList<>();
        allBrowsers.add(Browser.IE11.getDesiredCapabilities());
        allBrowsers.add(Browser.CHROME.getDesiredCapabilities());
        return allBrowsers;
    }

}