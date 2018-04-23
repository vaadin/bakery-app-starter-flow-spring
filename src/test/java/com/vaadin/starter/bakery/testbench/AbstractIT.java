package com.vaadin.starter.bakery.testbench;

import com.vaadin.starter.bakery.ui.utils.BakeryConst;
import org.junit.Rule;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.LoggerFactory;

import com.vaadin.starter.bakery.testbench.elements.ui.LoginViewElement;
import com.vaadin.testbench.IPAddress;
import com.vaadin.testbench.ScreenshotOnFailureRule;
import com.vaadin.testbench.TestBenchDriverProxy;
import com.vaadin.testbench.parallel.ParallelTest;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;

public abstract class AbstractIT extends ParallelTest {

	public String APP_URL = "http://localhost:8080/";

	static {
		// Prevent debug logging from Apache HTTP client
		Logger root = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
		root.setLevel(Level.INFO);
		// Let notifications persist longer during tests
		BakeryConst.NOTIFICATION_DURATION = 10000;
	}

	@Rule
	public ScreenshotOnFailureRule screenshotOnFailure = new ScreenshotOnFailureRule(this, true);

	@Override
	public void setup() throws Exception {
		super.setup();
		if (getRunLocallyBrowser() == null) {
			APP_URL = "http://" + IPAddress.findSiteLocalAddress() + ":8080/";
		}
	}

	@Override
	public TestBenchDriverProxy getDriver() {
		return (TestBenchDriverProxy) super.getDriver();
	}

	@Override
	public void setDesiredCapabilities(DesiredCapabilities desiredCapabilities) {
		// Disable interactivity check in Firefox https://github.com/mozilla/geckodriver/#mozwebdriverclick
		DesiredCapabilities disableInteractivityCheck = new DesiredCapabilities();
		disableInteractivityCheck.setCapability("moz:webdriverClick", false);
		super.setDesiredCapabilities(desiredCapabilities.merge(disableInteractivityCheck));
	}

	protected LoginViewElement openLoginView() {
		return openLoginView(getDriver(), APP_URL);
	}

	protected LoginViewElement openLoginView(WebDriver driver, String url) {
		driver.get(url);
		return $(LoginViewElement.class).waitForFirst();
	}
}
