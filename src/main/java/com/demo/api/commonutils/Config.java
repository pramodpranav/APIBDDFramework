package com.demo.api.commonutils;

import java.io.FileInputStream;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import org.testng.asserts.SoftAssert;

import com.demo.api.services.LoggerHelper;
import com.jayway.restassured.response.Response;

import cucumber.api.Scenario;

public class Config {

	public List<String> listOfFailedStep ;
	public List<String> listOfLogsOfEachFailedStep;
	public int stepNumber = 0;
	
	//scenarioTestStatus :  true means pass and false means fail 
	public boolean isFailScenarioStatus = false;
	public Scenario scenario=null;

	// stores the run time properties (different for every test)
	Properties runtimeProperties;

	public SoftAssert softAssert;
	String testEndTime;

	public String testLog;
	public static String scenarioName;
	public static String featureName;
	public Scenario testScenario;
	public boolean testResult;
	public Response apiResponse=null;
	public String authorizationToken="";


	// package fields
	String testStartTime;



	/**
	 * Load Config
	 * @param configPath
	 * @param scenario
	 * @author pramod.singh
	 */
	public Config(String configPath)
	{
		this.testLog = "";
		this.softAssert = new SoftAssert();
		this.testScenario = scenario;

		this.runtimeProperties = new Properties();

		// Read and load the above specified properties file in constructor
		loadPropertiesFile(configPath);
		
	}
	
	/**
	 * read and load properties file into RunTime Properties
	 * @param configPath
	 * @author pramod.singh
	 */
	public void loadPropertiesFile(String configPath ) {
		Properties property = new Properties();
		try {
			FileInputStream fis = new FileInputStream(configPath);
			property.load(fis);
			//fn.close();
			Enumeration<Object> em = property.keys();
			while (em.hasMoreElements())
			{
				String str = (String) em.nextElement();
				putRunTimeProperty(str, (String) property.get(str));
			}
		}catch(Exception e) {
			//this.logFail(e.getMessage(), true);
		}
	}
	
	/**
	 * Add the given key value pair in the Run Time Properties
	 * 
	 * @param key
	 * @param value
	 * @author pramod.singh
	 */
	public void putRunTimeProperty(String key, String value)
	{
		String keyName = key.toLowerCase();
		runtimeProperties.put(keyName, value);
		//LoggerHelper.logComment(this,"Putting Run-Time key-" + keyName + " value:-'" + value + "'");
	}
	
	/**
	 * Get the Run Time Property value
	 * 
	 * @param key
	 *            key name whose value is needed
	 * @return value of the specified key
	 */
	
	public String getRunTimeProperty(String key)
	{
		String keyName = key.toLowerCase();
		String value = "";
		try
		{
			value = runtimeProperties.get(keyName).toString();
			LoggerHelper.logComment(this,"Reading Run-Time key-" + keyName + " value:-'" + value + "'");
		}
		catch (Exception e)
		{
			LoggerHelper.logComment(this,e.toString());
			LoggerHelper.logComment(this,"'" + key + "' not found in Run Time Properties");

			return null;
		}
		return value;
	}
	/**
	 * wait for given second
	 * @param second
	 */
	public static void wait(Config testConfig,int seconds) {
		int milliseconds = seconds * 1000;
		try
		{
			Thread.sleep(milliseconds);
			LoggerHelper.logComment(testConfig,"Wait for '" + seconds + "' seconds");

		}
		catch (InterruptedException e)
		{
			LoggerHelper.logException(e, testConfig);
		}
	}

}

