package com.cucumber.Nexon;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(
//		plugin = {"pretty"}, 
		features ={"src/test/resource"},
		format = {"pretty",
				"json:target/cucumber.json",
				"html:target/cucumber-html-report/"}
		
		)

public class RunnerTest {

}