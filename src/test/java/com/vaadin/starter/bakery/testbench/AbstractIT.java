package com.vaadin.starter.bakery.testbench;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import com.vaadin.starter.bakery.Application;
import com.vaadin.starter.bakery.testbench.elements.ui.LoginViewElement;
import com.vaadin.starter.bakery.ui.utils.BakeryConst;
import com.vaadin.testbench.ElementQuery;
import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.addons.junit5.extensions.container.SpringBootConf;
import com.vaadin.testbench.addons.junit5.extensions.unittest.VaadinTest;
import com.vaadin.testbench.addons.junit5.pageobject.VaadinPageObject;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.slf4j.LoggerFactory;

@VaadinTest
@SpringBootConf(source = Application.class)
public abstract class AbstractIT<E extends TestBenchElement> {

	static {
		// Prevent debug logging from Apache HTTP client
		Logger root = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
		root.setLevel(Level.INFO);
		// Let notifications persist longer during tests
		BakeryConst.NOTIFICATION_DURATION = 10000;
	}

	String APP_URL;
	VaadinPageObject pageObject;

	@BeforeEach
	void setup(VaadinPageObject pageObject) {
		this.pageObject = pageObject;
		APP_URL = pageObject.url();
	}

	protected <T extends TestBenchElement> ElementQuery<T> $(Class<T> type) {
		return pageObject.$(type);
	}

	protected ElementQuery<TestBenchElement> $(String tag) {
		return pageObject.$(tag);
	}

	WebDriver getDriver() {
		return pageObject.getDriver();
	}

	LoginViewElement openLoginView() {
		return openLoginView(getDriver(), APP_URL);
	}

	LoginViewElement openLoginView(WebDriver driver, String url) {
		driver.get(url);
		return $(LoginViewElement.class).waitForFirst();
	}

	protected abstract E openView();
}
