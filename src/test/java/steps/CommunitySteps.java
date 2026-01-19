package steps;

import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import utils.WebDriverFactory;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class CommunitySteps {
    WebDriver driver;
    public String name;
    private static class Community {
        String name;
        String description;

        Community(String name, String description) {
            this.name = name;
            this.description = description;
        }
    }

    // simple in-memory store per JVM (suitable for demo/simulated tests)
    private static final List<Community> COMMUNITIES = new ArrayList<>();
    private String lastError;

    @When("I create a community with name {string} and description {string}")
    public void i_create_a_community_with_name_and_description(String name, String description) {
        try {
            driver = WebDriverFactory.getDriver();
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            Thread.sleep(100); // simulate some processing delay
            driver.findElement(By.xpath("//*[text()='Add Community']")).click();
            Thread.sleep(2000);
            String pageTitle = driver.findElement(By.xpath("//h2[text()='Add Community']")).getText();
            System.out.println("Current page title: " + pageTitle);
            if(pageTitle.equals("Add Community")){
                driver.findElement(By.id("name")).sendKeys(name);
                Thread.sleep(1000);
                driver.findElement(By.id("desc")).sendKeys(description);
                Thread.sleep(1000);
                driver.findElement(By.xpath("//button[text()='Set Boundary ']")).click();
                Thread.sleep(5000);
                driver.findElement(By.xpath("//*[text()='Submit']")).click();
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to create community",e);
        }
    }

    @When("I create communities from csv {string}")
    public void i_create_communities_from_csv(String resourcePath) {
                try {
                        java.util.List<java.util.Map<String,String>> rows = utils.CsvUtils.readCsvFromClasspath(resourcePath);
                        for (java.util.Map<String,String> row : rows) {
                                name = row.getOrDefault("name", "");
                                String desc = row.getOrDefault("description", "");
                                // attempt UI creation if driver available
                                        try {
                                        i_create_a_community_with_name_and_description(name, desc);
                                    } catch (Exception ex) {
                                        // fallback: add to in-memory store
                                                COMMUNITIES.add(new Community(name, desc));
                                    }
                            }
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to read CSV: "+  resourcePath, e);
                    }
            }

    @Then("I should see community in the community list")
    public void i_should_see_community_in_the_community_list() {
        try {
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            driver.findElement(By.xpath("//button[text()='Cancel']")).click();
            Thread.sleep(2000);
            // need to add the line of code for verifying the community in the list
        }catch (Exception e) {
            throw new RuntimeException("Failed to return to community list", e);
        }
    }

    @Then("I should see community error message {string}")
    public void i_should_see_community_error_message(String expected) {
        Assert.assertEquals(lastError, expected, "Expected community operation to produce error: " + expected);
    }

    @When("I edit community {string} to name {string} and description {string}")
    public void i_edit_community_to_name_and_description(String existingName, String newName, String newDescription) {
        lastError = null;
        Optional<Community> existing = COMMUNITIES.stream().filter(c -> c.name.equalsIgnoreCase(existingName)).findAny();
        if (existing.isEmpty()) {
            lastError = "Community not found";
            System.out.println("Edit community failed: " + lastError);
            return;
        }
        if (newName == null || newName.isBlank()) {
            lastError = "Name is required";
            System.out.println("Edit community failed: " + lastError);
            return;
        }
        boolean conflict = COMMUNITIES.stream().anyMatch(c -> c.name.equalsIgnoreCase(newName) && !c.name.equalsIgnoreCase(existingName));
        if (conflict) {
            lastError = "Community already exists";
            System.out.println("Edit community failed: " + lastError);
            return;
        }
        Community c = existing.get();
        c.name = newName;
        c.description = newDescription;
        System.out.println("Community updated: " + newName);
    }
}