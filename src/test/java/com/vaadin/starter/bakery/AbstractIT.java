package com.vaadin.starter.bakery;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import com.vaadin.starter.bakery.ui.view.LoginViewElement;
import com.vaadin.testbench.*;
import com.vaadin.testbench.commands.TestBenchCommandExecutor;
import org.junit.Before;
import org.junit.Rule;
import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.LoggerFactory;

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
     * @param element   the element to check
     * @param className the class name to check for
     * @return <code>true</code> if the element has the given class name,
     * <code>false</code> otherwise
     */
    protected static boolean hasClassName(TestBenchElement element, String className) {
        return element.getClassNames().contains(className);
    }

    protected LoginViewElement openLoginView() {
        return openLoginView(getDriver(), APP_URL);
    }

    protected LoginViewElement openLoginView(WebDriver driver, String url) {
        driver.get(url);
        TestBenchElement body = (TestBenchElement) driver.findElement(By.tagName("body"));
        TestBenchCommandExecutor executor = ((HasTestBenchCommandExecutor) driver).getCommandExecutor();
        return TestBench.createElement(LoginViewElement.class, body.getWrappedElement(), executor);
    }

    /**
     * Waits for a browser alert window to appear and returns the alert object.
     * The default timeout is 10 seconds. If no alert appears in that time, an exception is thrown.
     *
     * @return an {@link Alert} object
     */
    protected Alert waitForAlert() {
        WebDriverWait wait = new WebDriverWait(getDriver(), 10);
        return wait.until(ExpectedConditions.alertIsPresent());
    }
}
