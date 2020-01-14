package com.starling.exercise.roundup;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
    plugin = {"pretty", "html:build/cucumber", "json:build/cucumber.json"},
    features = "src/acceptance/resources/features",
    glue = "classpath:com.starling.exercise.roundup",
    tags = "not @Ignore")
public class RunCukesTest {

}