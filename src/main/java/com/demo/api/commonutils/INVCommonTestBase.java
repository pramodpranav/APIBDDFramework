package com.demo.api.commonutils;


import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import com.demo.api.commonutils.CommonTestBase;

import cucumber.api.testng.TestNGCucumberRunner;

public class INVCommonTestBase extends CommonTestBase {

	@BeforeSuite(alwaysRun = true)
	
	
	public void setUpClass() {

		testNGCucumberRunner = new TestNGCucumberRunner(this.getClass());

	}

	@AfterSuite(alwaysRun = true)
	public void tearDownClass() {

		testNGCucumberRunner.finish();

	}
}
