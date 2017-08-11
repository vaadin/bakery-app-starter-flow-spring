package com.vaadin.starter.bakery;

import com.vaadin.testbench.HasDriver;
import com.vaadin.testbench.TestBenchDriverProxy;
import com.vaadin.testbench.TestBenchElement;
import org.apache.commons.io.IOUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public abstract class By extends com.vaadin.testbench.By {
	public static org.openqa.selenium.By shadowSelector(final String selector) {
		if (selector == null || selector.equals("")) {
			throw new IllegalArgumentException(
					"Cannot find elements with a null or empty selector.");
		}

		return new ByShadowSelector(selector);
	}

	private static class ByShadowSelector extends ByVaadin {
		private final String selector;

		private static final String QUERY_SELECTOR_IN_SHADOW_ROOT_JS =
				getResource("query-selector-in-shadow-root.js");

		private ByShadowSelector(final String selector) {
			super(selector);
			this.selector = selector;
		}

		/**
		 * Returns a list of WebElements identified by a deep CSS selector.
		 *
		 * @param context
		 *            SearchContext for originating the search
		 * @return List of found WebElements
		 */
		@Override
		public List<WebElement> findElements(SearchContext context) {
			return findElementsInShadowRoot(selector, context);
		}

		/**
		 * Returns a WebElement identified by a deep CSS selector.
		 *
		 * @param context
		 *            SearchContext for originating the search
		 * @return First found WebElement
		 */
		@Override
		public WebElement findElement(SearchContext context) {
			return findElementInShadowRoot(selector, context);
		}

		@Override
		public String toString() {
			return "By.shadowSelector: " + selector;
		}

		/**
		 * Finds an element in the element's Shadow Root by a CSS selector string.
		 *
		 * @param selector
		 *            any CSS selector
		 * @param context
		 *            a suitable search context - either a
		 *            {@link TestBenchDriverProxy} or a {@link TestBenchElement}
		 *            instance.
		 * @return the first element identified by the selector
		 */
		private static WebElement findElementInShadowRoot(String selector,
				SearchContext context) {
			List<WebElement> elements = findElementsInShadowRoot(selector, context);

			if (elements.isEmpty()) {
				throw makeNoSuchElementException(selector,
						new Exception("The provided selector does not match any elements."));
			}

			return elements.get(0);
		}

		/**
		 * Finds a list of elements in the element's Shadow Root by a CSS selector string.
		 *
		 * @param selector
		 *            any CSS selector
		 * @param context
		 *            a suitable search context - either a
		 *            {@link TestBenchDriverProxy} or a {@link TestBenchElement}
		 *            instance.
		 * @return the list of elements identified by the selector
		 */
		private static List<WebElement> findElementsInShadowRoot(String selector, SearchContext context) {
			WebDriver driver = ((HasDriver) context).getDriver();
			JavascriptExecutor jse = (JavascriptExecutor) driver;

			try {
				Object output = context instanceof WebDriver
						? jse.executeScript(QUERY_SELECTOR_IN_SHADOW_ROOT_JS, selector)
						: jse.executeScript(QUERY_SELECTOR_IN_SHADOW_ROOT_JS, selector, context);
				return extractWebElements(output);
			} catch (Exception e) {
				throw makeNoSuchElementException(selector, e);
			}
		}

		private static List<WebElement> extractWebElements(Object elementList) {
			List<WebElement> result = new ArrayList<>();
			if (elementList instanceof WebElement) {
				result.add((WebElement) elementList);
			} else if (elementList instanceof List<?>) {
				for (Object o : (List<?>) elementList) {
					if (null != o && o instanceof WebElement) {
						result.add((WebElement) o);
					}
				}
			}
			return result;
		}

		private static NoSuchElementException makeNoSuchElementException(String selector, Exception cause) {
			return new NoSuchElementException(
					"ByShadowSelector: could not find elements with the selector '" + selector + "'", cause);
		}

		private static String getResource(String name) {
			Logger logger = LoggerFactory.getLogger(By.class);
			InputStream inputStream = By.class.getClassLoader().getResourceAsStream(name);
			if (inputStream != null) {
				try {
					return IOUtils.toString(inputStream, StandardCharsets.UTF_8);
				} catch (IOException e) {
					logger.error("Could not load the resource " + name, e);
				}
			} else {
				logger.error("Could not find the resource " + name);
			}

			return null;
		}
	}
}
