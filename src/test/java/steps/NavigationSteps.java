package steps;

import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import utils.WebDriverFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class NavigationSteps {

    private String dashboardUrl;
    private String currentUrl;
    public  WebDriver driver;
    public NavigationSteps() {
        // load dashboard url from config.properties
        Properties props = new Properties();
        try (InputStream in = this.getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (in != null) {
                props.load(in);
                dashboardUrl = props.getProperty("dashboard.url", "https://outsmart-dev.outsmart-tech.com/dashboard/community");
            } else {
                dashboardUrl = "https://outsmart-dev.outsmart-tech.com/dashboard/community";
            }
        } catch (IOException e) {
            dashboardUrl = "https://outsmart-dev.outsmart-tech.com/dashboard/community";
        }
    }

    @When("I click the {string} tab")
    public void i_click_the_tab(String tabName) {
        try {
            driver = WebDriverFactory.getDriver();
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            String locator = "";
            switch (tabName) {
                case "community":
                    locator = "//a[contains(@href,'/dashboard/"+tabName+"')]";
                    break;
                case "group":
                    locator = "//a[contains(@href,'/dashboard/"+tabName+"')]";
                    break;
                case "user":
                    locator = "//a[contains(@href,'/dashboard/"+tabName+"')]";
                    break;
                case "emergency":
                    locator = "//a[contains(@href,'/dashboard/"+tabName+"')]";
                    break;
                default:
                    locator = "//a[contains(@href,'/dashboard/"+tabName+"')]";
            }

            if (driver != null) {
                System.out.println("Clicking the '" + tabName + "' tab using locator: " + locator);
                Thread.sleep(2000);
                driver.findElement(By.xpath(locator)).click();
                Thread.sleep(2000);
                currentUrl = driver.getCurrentUrl();
                System.out.println("Browser navigated to: " + currentUrl);
            } else {
                // simulation mode
                currentUrl = driver.getCurrentUrl();
                System.out.println("Simulated navigation to: " + currentUrl);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to click the '" + tabName + "' tab", e);
        }

    }

    @Then("I should see the {string} page")
    public void i_should_see_the_page(String pageName) {
        String expected = pageName.trim().toLowerCase();
        boolean ok;
        switch (expected) {
            case "community":
                ok = currentUrl.contains("/community");
                break;
            case "group":
                ok = currentUrl.contains("/group");
                break;
            case "user":
                ok = currentUrl.contains("/user");
                break;
            case "emergency":
                ok = currentUrl.contains("/emergency");
                break;
            default:
                ok = currentUrl.contains("/community");
        }
        Assert.assertTrue(ok, "Expected current URL '" + currentUrl + "' to contain page identifier '" + expected + "'");
    }
}

