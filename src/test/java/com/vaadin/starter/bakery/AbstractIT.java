package com.vaadin.starter.bakery;

import org.junit.Before;
import org.junit.Rule;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.LoggerFactory;

import com.vaadin.starter.bakery.ui.view.LoginViewElement;
import com.vaadin.testbench.HasTestBenchCommandExecutor;
import com.vaadin.testbench.ScreenshotOnFailureRule;
import com.vaadin.testbench.TestBench;
import com.vaadin.testbench.TestBenchDriverProxy;
import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.TestBenchTestCase;
import com.vaadin.testbench.commands.TestBenchCommandExecutor;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;

public class AbstractIT extends TestBenchTestCase {

	public static final String APP_URL = "http://localhost:8080/";

	static {
		// Prevent debug logging from Apache HTTP client
		Logger root = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
		root.setLevel(Level.INFO);
	}

	@Rule
	public ScreenshotOnFailureRule screenshotOnFailure = new ScreenshotOnFailureRule(this, true);

	@Before
	public void setup() {
		setDriver(createDriver());
		getDriver().resizeViewPortTo(800, 600);
	}

	protected WebDriver createDriver() {
		return TestBench.createDriver(new ChromeDriver());
	}

	@Override
	public TestBenchDriverProxy getDriver() {
		return (TestBenchDriverProxy) super.getDriver();
	}

	protected static boolean hasAttribute(TestBenchElement element, String name) {
		return internalGetAttribute(element, name) != null;
	}

	protected static Object internalGetAttribute(TestBenchElement element, String name) {
		return element.getCommandExecutor().executeScript("return arguments[0].getAttribute(arguments[1]);", element,
				name);
	}

	/**
	 * Checks if the given element has the given class name.
	 *
	 * @param element
	 *            the element to check
	 * @param className
	 *            the class name to check for
	 * @return <code>true</code> if the element has the given class name,
	 *         <code>false</code> otherwise
	 */
	protected static boolean hasClassName(TestBenchElement element, String className) {
		return element.getClassNames().contains(className);
	}

	protected LoginViewElement openLoginView() {
		return openLoginView(getDriver(), APP_URL);
	}

	protected LoginViewElement openLoginView(WebDriver driver, String url) {
		driver.get(url);
		return ((TestBenchElement) findElement(By.tagName("bakery-login"))).wrap(LoginViewElement.class);
	}

	/**
	 * Waits for a WebElement matching the selector to be found and returns the
	 * object. The default timeout is 10 seconds.
	 *
	 * @return a {@link WebElement} object if found before timeout
	 * 
	 *         * @throws TimeoutException If 10 seconds passed.
	 */
	protected WebElement waitUntilElementPresent(By by) {
		return new WebDriverWait(getDriver(), 10).until(ExpectedConditions.presenceOfElementLocated(by));
	}
}
