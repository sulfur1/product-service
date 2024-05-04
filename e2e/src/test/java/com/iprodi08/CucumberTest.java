package com.iprodi08;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",
        plugin = {"json:target/cucumber.json", "html:target/cucumber-html-report"},
        glue = "com.iprodi08.stepsdefs")
public class CucumberTest {
}
