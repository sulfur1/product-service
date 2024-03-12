package steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class TestStepsDefinitions {
    private String inputText;
    private String outputText;

    @Given("the user is on the home page")
    public void userIsOnHomePage() {
        System.out.println("First test");
    }

    @When("the user enters {string}")
    public void userEntersText(String text) {
        System.out.println("When something");
    }

    @Then("the page displays {string}")
    public void pageDisplaysText(String expectedText) {
        System.out.println("Some output");
    }
}
