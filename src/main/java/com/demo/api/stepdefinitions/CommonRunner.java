package com.demo.api.stepdefinitions;

import com.demo.api.commonutils.INVCommonTestBase;

import cucumber.api.CucumberOptions;

@CucumberOptions(features = { "src/test/resources/APIFeatureFile" }, glue = {
				"com/demo/api/stepdefinitions" }, monochrome = true, plugin = { "pretty",
						"html:target/cucumber-reports/cucumber-pretty",
						"json:target/cucumber-reports/CucumberTestReport.json",
						"rerun:target/cucumber-reports/rerun.txt" })
public class CommonRunner extends INVCommonTestBase {

}
