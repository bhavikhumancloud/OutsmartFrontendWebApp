package steps;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import utils.WebDriverFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Time;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class LoginSteps {

    private String dashboardUrl;
    private String username;
    private String password;
    private String lastError;
    private String currentUrl;
    private String loginPageUrl;
    private WebDriver driver;

    @Before
    public void loadConfig() {
        Properties props = new Properties();
        try (InputStream in = this.getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (in != null) {
                props.load(in);
                dashboardUrl = props.getProperty("dashboard.url");
                loginPageUrl = props.getProperty("login.page.url", "https://outsmart-dev.outsmart-tech.com/login");
                System.out.println("Loaded config: dashboard.url=" + dashboardUrl + ", login.page.url=" + loginPageUrl);
            } else {
                System.out.println("config.properties not found on classpath; using defaults");
                dashboardUrl = "https://outsmart-dev.outsmart-tech.com/dashboard/community";
                loginPageUrl = "https://outsmart-dev.outsmart-tech.com/login";
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
    }

    @After
    public void tearDown() {
        if (driver != null) {
            WebDriverFactory.quitDriver();
            driver = null;
        }
    }

    @Given("I open the login page")
    public void i_open_the_login_page() {
        // initialize browser via WebDriverFactory
        driver = WebDriverFactory.createDriver();
        driver.get(loginPageUrl);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(java.time.Duration.ofSeconds(10));
        String pageTitle = driver.getTitle();
        System.out.println("Page title is: " + pageTitle);
    }


    @When("I enter username {string} and password {string}")
    public void i_enter_username_and_password(String username, String password) {
        try {
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            driver.findElement(By.xpath("//input[@id='identifier']")).clear();
            driver.findElement(By.xpath("//input[@id='identifier']")).sendKeys(username);
            driver.findElement(By.xpath("//input[@id='password']")).clear();
            driver.findElement(By.xpath("//input[@id='password']")).sendKeys(password);
        }catch (Exception e) {
            throw new RuntimeException("Error entering username and password", e);
        }

        this.username = username;
        this.password = password;
        System.out.println("Enter username: '" + username + "' and password: '" + password + "'");
    }

    @When("I submit the login form")
    public void i_submit_the_login_form() {
        try {
            driver.manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS);
            driver.findElement(By.xpath("//button[@type='submit']")).click();
        } catch (Exception e) {
            throw new RuntimeException("Error submitting login Button", e);
        }
    }

    @Then("I should be logged in and navigated to dashboard")
    public void i_should_be_logged_in_and_navigated_to_dashboard() {
        try {
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            String currentTitile = driver.getTitle();
            System.out.println("Current page title after login: " + currentTitile);
            Assert.assertEquals(currentTitile,"OutSmart Safety","User should be on Dashboard page after login");
        }catch (Exception e){
            throw new RuntimeException("Error verifying dashboard page", e);
        }
    }

    @Then("I should see error message {string}")
    public void i_should_see_error_message(String expected) {
        Assert.assertEquals(lastError, expected, "Expected specific validation message");
    }
}
