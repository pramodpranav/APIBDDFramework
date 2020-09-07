package com.demo.api.stepdefinitions;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import org.testng.asserts.SoftAssert;

import com.demo.api.commonutils.Config;
import com.demo.api.commonutils.GlobalConfigManager;
import com.demo.api.commonutils.INVCommonTestBase;
import com.demo.api.services.LoggerHelper;
import com.google.inject.Inject;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.AfterStep;
import cucumber.api.java.Before;
import cucumber.api.java.BeforeStep;

public class ServiceHooks extends INVCommonTestBase{


	@Inject
	private GlobalConfigManager configManager;
	
	public  Config testConfig;
	
	@Before
	public void initializeTest(Scenario scenario) {
		testConfig = configManager.testConfig;
		testConfig.testScenario = scenario;
		testConfig.scenarioName = scenario.getName();
		testConfig.listOfFailedStep = new ArrayList<String>();
		testConfig.listOfLogsOfEachFailedStep = new ArrayList<String>();
		testConfig.stepNumber = 0;

	}
	
	@After
	public void endTest(Scenario scenario) {
		testConfig = configManager.testConfig;
		try {

			if (scenario.isFailed() || testConfig.listOfFailedStep.size() > 0) {
				LoggerHelper.logComment(testConfig,scenario.getName() + " is Failed");
				if (testConfig.listOfFailedStep.size() > 0) {
					
					StringBuilder finalReport = new StringBuilder();
					//finalReport.append("<br><br>[Following Guidelines to debug Issues comming in steps");
					//finalReport.append("<br> To get assertion error in details, Go to the After hook of given Steps and for more details then go to the logs \n");
					finalReport.append("===================================================================================================\n");
					finalReport.append("<h2> Following list of Steps are failed due to exceptions or failure of Soft Assertion check </h2>");
					
					LoggerHelper.logComment(testConfig,"===========================================================================================");
					LoggerHelper.logComment(testConfig,"Following list of Steps are failed due to exceptions or failure of Soft Assertion check \n\n");
				

					//testConfig.logComment("Following list of Steps are Failed");
					for (int i = 0; i < testConfig.listOfFailedStep.size(); i++) {
						LoggerHelper.logComment(testConfig,testConfig.listOfFailedStep.get(i) + " is failed due to some handled exception or Soft Assertion");			
						finalReport.append("<h4>" + testConfig.listOfFailedStep.get(i) + " is failed due to some handled exception or Soft Assertion </h4> </br>");
					}
					finalReport.append("\n ---------------------------------------------------------------------------------------------------- \n");	
					finalReport.append("</br> <h3> Attention Please  -> Please follow any one instructions to get details of failure reason of above mention steps </h3></br> ");
					finalReport.append("<li> a) Follow attached html reports where step wise failure reasons are fiven </li> </br>");
					finalReport.append("<li> b) For particular step sequence number, Follow after hook of that step or Last html file attached </li> </br>");
					
					LoggerHelper.logComment(testConfig," Attention Please  -> Please follow any one instructions to get details of failure reason of above mention steps");
					LoggerHelper.logComment(testConfig," Follow attached html reports where step wise failure reasons are fiven");
					LoggerHelper.logComment(testConfig," For particular step sequence number, Follow after hook of that step or Last html file attached ");

					
					finalReport.append("---------------------------------------------------------------------------------------------------- \n");	
					finalReport.append("<p> Please Find Failure reason in details Step wise </p> ");
					for (int i = 0; i < testConfig.listOfFailedStep.size(); i++) {
						//testConfig.logComment("<br><br><br> --- Please find failure reason for  " + testConfig.listOfFailedStep.get(i));
						finalReport.append("/</br></br> <h3> Failure reason details for  " + testConfig.listOfFailedStep.get(i) + "</h3>");
						//testConfig.logComment("-------------------------------Step Failure reason in details--------------------");
						finalReport.append("</br> ------------------------------- Step Failure reason in details-------------------- </br>");
						//testConfig.logComment(testConfig.listOfLogsOfEachFailedStep.get(i));
						finalReport.append(testConfig.listOfLogsOfEachFailedStep.get(i));
					}
					
					LoggerHelper.logComment(testConfig,"========================================================================");
			
					LoggerHelper.embedMessageAsHTMLInReport(testConfig, finalReport.toString());
				}
				
				LoggerHelper.failFinal("Something went wrong !!!!!", testConfig);
			} else {
				LoggerHelper.logComment(testConfig,scenario.getName() + " is pass");
			}
		} catch (Exception e) {
			LoggerHelper.logException(e,testConfig);
		}
		finally {
			//
		}
	}

	@BeforeStep
	public void beforeStep() {
		testConfig = configManager.testConfig;
		testConfig.stepNumber++;
	}

	@AfterStep
	public void afterStep() {
		testConfig = configManager.testConfig;
		if (testConfig.isFailScenarioStatus) {
			LoggerHelper.logComment(testConfig,"Start of After Step Hook " + testConfig.stepNumber);
			testConfig.listOfFailedStep.add(" Step Sequnce Number : " + testConfig.stepNumber);
			try {
				testConfig.softAssert.assertAll();
			} catch (AssertionError e) {
				LoggerHelper.logComment(testConfig,"----------------------------------------\n");
				LoggerHelper.logComment(testConfig,e.getMessage());
				testConfig.listOfLogsOfEachFailedStep.add(e.getMessage());
				LoggerHelper.embedMessageAsHTMLInReport(testConfig, e.getMessage());
			}
			LoggerHelper.logComment(testConfig,"End of After Step Hook " + testConfig.stepNumber);
			LoggerHelper.logComment(testConfig,"----------------------------------------\n");
			testConfig.isFailScenarioStatus = false;
		}
	}


}