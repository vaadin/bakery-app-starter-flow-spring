package com.vaadin.starter.bakery.testbench;

import com.vaadin.testbench.annotations.BrowserConfiguration;
import com.vaadin.testbench.parallel.Browser;
import com.vaadin.testbench.parallel.ParallelTest;
import org.junit.Rule;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.LoggerFactory;

import com.vaadin.starter.bakery.testbench.elements.ui.LoginViewElement;
import com.vaadin.testbench.ScreenshotOnFailureRule;
import com.vaadin.testbench.TestBenchDriverProxy;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractIT extends ParallelTest {

	public static final String APP_URL = "http://localhost:8080/";

	static {
		// Prevent debug logging from Apache HTTP client
		Logger root = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
		root.setLevel(Level.INFO);
	}

	@Rule
	public ScreenshotOnFailureRule screenshotOnFailure = new ScreenshotOnFailureRule(this, true);

	@Override
	public void setup() throws Exception {
		super.setup();

		getCommandExecutor().resizeViewPortTo(1024, 768);
	}

	@Override
	public TestBenchDriverProxy getDriver() {
		return (TestBenchDriverProxy) super.getDriver();
	}

	protected LoginViewElement openLoginView() {
		return openLoginView(getDriver(), APP_URL);
	}

	protected LoginViewElement openLoginView(WebDriver driver, String url) {
		driver.get(url);
		return $(LoginViewElement.class).waitForFirst();
	}

	// Is needed for running tests on hub
	@BrowserConfiguration
	public List<DesiredCapabilities> getBrowsersToTest() {
		List<DesiredCapabilities> allBrowsers = new ArrayList<>();
		allBrowsers.add(Browser.IE11.getDesiredCapabilities());
		allBrowsers.add(Browser.CHROME.getDesiredCapabilities());
		return allBrowsers;
	}
}
