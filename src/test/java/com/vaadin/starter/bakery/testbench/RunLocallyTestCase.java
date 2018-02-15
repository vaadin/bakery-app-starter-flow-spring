package com.vaadin.starter.bakery.testbench;

import com.vaadin.testbench.TestBench;
import com.vaadin.testbench.TestBenchTestCase;
import org.junit.Before;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class RunLocallyTestCase extends TestBenchTestCase {

    @Before
    public void setup() throws Exception {
        setDriver(createDriver());
    }

    protected WebDriver createDriver() {
        return TestBench.createDriver(new ChromeDriver());
    }
}
