package com.iprodi08.stepsdefs;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class MyStepdefs {
    @Given("today is Sunday")
    public void today_is_sunday() {
        System.out.println("test-1");
    }
    @When("I ask whether it's Friday yet")
    public void i_ask_whether_it_s_friday_yet() {
        System.out.println("test-2");
    }
    @Then("I should be told {string}")
    public void i_should_be_told(String string) {
        System.out.println("test-3");
    }
}
