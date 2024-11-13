package com.home.assignment.urlshortener.cucumber;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",
        plugin = {"pretty", "json:target/cucumber-report.json", "html:target/cucumber-report.html"},
        glue = "com.home.assignment.urlshortener.cucumber"
)
public class ShortUrlControllerTest {
}

