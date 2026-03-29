package com.bk.automation.core.driver;

import com.bk.automation.core.config.ConfigReader;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.time.Duration;

/**
 * Thread-safe DriverFactory using ThreadLocal for parallel execution support.
 * Manages WebDriver lifecycle: initialization, retrieval, and teardown.
 *
 * <p>Usage:
 * <pre>
 *   DriverFactory.initDriver("chrome");
 *   WebDriver driver = DriverFactory.getDriver();
 *   // ... perform actions ...
 *   DriverFactory.quitDriver();
 * </pre>
 */
public final class DriverFactory {

    private static final Logger logger = LogManager.getLogger(DriverFactory.class);
    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();
    private static final ConfigReader config = ConfigReader.getInstance();

    private DriverFactory() {
        // Utility class — prevent instantiation
    }

    /**
     * Initializes WebDriver based on config.properties browser setting.
     * Uses ThreadLocal to support parallel execution.
     */
    public static void initDriver() {
        initDriver(config.getBrowser());
    }

    /**
     * Initializes WebDriver for the specified browser.
     *
     * @param browser the browser name (chrome, firefox, edge)
     */
    public static void initDriver(String browser) {
        if (driverThreadLocal.get() != null) {
            logger.warn("Driver already initialized for thread: {}. Quitting existing driver.",
                    Thread.currentThread().getName());
            quitDriver();
        }

        WebDriver driver = createDriver(browser.toLowerCase().trim());
        configureDriver(driver);
        driverThreadLocal.set(driver);

        logger.info("WebDriver initialized - Browser: {} | Thread: {} | Headless: {}",
                browser, Thread.currentThread().getName(), config.isHeadless());
    }

    /**
     * Returns the WebDriver instance for the current thread.
     *
     * @return the WebDriver instance
     * @throws IllegalStateException if the driver has not been initialized
     */
    public static WebDriver getDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver == null) {
            throw new IllegalStateException(
                    "WebDriver is not initialized for thread: " + Thread.currentThread().getName()
                            + ". Call DriverFactory.initDriver() first.");
        }
        return driver;
    }

    /**
     * Quits the WebDriver and removes it from ThreadLocal.
     */
    public static void quitDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            try {
                driver.quit();
                logger.info("WebDriver quit successfully for thread: {}", Thread.currentThread().getName());
            } catch (Exception e) {
                logger.error("Error quitting WebDriver for thread: {}", Thread.currentThread().getName(), e);
            } finally {
                driverThreadLocal.remove();
            }
        }
    }

    /**
     * Checks whether the driver is initialized for the current thread.
     */
    public static boolean isDriverInitialized() {
        return driverThreadLocal.get() != null;
    }

    // ======================== Private Helpers ========================

    private static WebDriver createDriver(String browser) {
        switch (browser) {
            case "chrome":
                return createChromeDriver();
            case "firefox":
                return createFirefoxDriver();
            case "edge":
                return createEdgeDriver();
            default:
                logger.error("Unsupported browser: {}. Defaulting to Chrome.", browser);
                return createChromeDriver();
        }
    }

    private static WebDriver createChromeDriver() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();

        if (config.isHeadless()) {
            options.addArguments("--headless=new");
        }
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--remote-allow-origins=*");

        if (config.getBooleanProperty("browser.incognito", false)) {
            options.addArguments("--incognito");
        }

        return new ChromeDriver(options);
    }

    private static WebDriver createFirefoxDriver() {
        WebDriverManager.firefoxdriver().setup();
        FirefoxOptions options = new FirefoxOptions();

        if (config.isHeadless()) {
            options.addArguments("--headless");
        }

        return new FirefoxDriver(options);
    }

    private static WebDriver createEdgeDriver() {
        WebDriverManager.edgedriver().setup();
        EdgeOptions options = new EdgeOptions();

        if (config.isHeadless()) {
            options.addArguments("--headless=new");
        }
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        return new EdgeDriver(options);
    }

    private static void configureDriver(WebDriver driver) {
        // Timeouts
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(config.getImplicitWait()));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(config.getPageLoadTimeout()));
        driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(
                config.getIntProperty("timeout.script", 30)));

        // Window
        if (config.isMaximized()) {
            driver.manage().window().maximize();
        }

        // Delete cookies
        driver.manage().deleteAllCookies();
    }
}
