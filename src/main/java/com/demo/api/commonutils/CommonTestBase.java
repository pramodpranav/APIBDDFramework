package com.demo.api.commonutils;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import cucumber.api.CucumberOptions;
import cucumber.api.testng.AbstractTestNGCucumberTests;
import cucumber.api.testng.CucumberFeatureWrapper;
import cucumber.api.testng.PickleEventWrapper;
import cucumber.api.testng.TestNGCucumberRunner;


public class CommonTestBase extends AbstractTestNGCucumberTests {

	protected TestNGCucumberRunner testNGCucumberRunner;

	@Test(groups = "cucumber", description = "Runs Cucumber scenarios", dataProvider = "scenario")
	public void scenario(PickleEventWrapper pickleEvent, CucumberFeatureWrapper 
		    cucumberFeature) throws Throwable {

		testNGCucumberRunner.runScenario(pickleEvent.getPickleEvent());;

	}

	@DataProvider
	public Object[][] scenario() {
		return testNGCucumberRunner.provideScenarios();
	}

}
