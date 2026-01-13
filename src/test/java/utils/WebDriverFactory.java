package utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class WebDriverFactory {

    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

    // Create and register a ChromeDriver for the current thread
    public static WebDriver createDriver() {
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        DRIVER.set(driver);
        return driver;
    }

    // Get the current thread's WebDriver (may be null)
    public static WebDriver getDriver() {
        return DRIVER.get();
    }

    // Quit and remove the WebDriver for the current thread
    public static void quitDriver() {
        WebDriver driver = DRIVER.get();
        if (driver != null) {
            try {
                driver.quit();
            } catch (Exception ignored) {
            } finally {
                DRIVER.remove();
            }
        }
    }
}

