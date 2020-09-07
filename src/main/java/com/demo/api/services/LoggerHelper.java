package com.demo.api.services;

import org.testng.Assert;

import com.demo.api.commonutils.Config;


public class LoggerHelper {

	/**
	 * 
	 * @param testConfig
	 * @param message
	 * @author i0465
	 */
	public static void logComment(Config testConfig,String message) {
		writeMessageInReport(testConfig,message);
	}
	
	/**
	 * 
	 * @param message
	 * @param testConfig
	 */
	public static void logPass(String message, Config testConfig) {
		message = "[ Pass ] : " + message;
			writeMessageInReport(testConfig,message);
	}
	
	/**
	 * 
	 * @param testConfig
	 * @param message
	 * @author i0465
	 */
	private static void writeMessageInReport(Config testConfig,String message) {
		testConfig.testScenario.write(message);
		System.out.println(message);
	}
	
	/**
	 * 
	 * @param message
	 * @param testConfig
	 * @author pramod.singh
	 */
	public static void failFinal(String message, Config testConfig) {
		String tempMessage =  "[Fail] " + message;	
		testConfig.isFailScenarioStatus=true;
		Assert.fail(tempMessage);

		}
	
	/**
	 * 
	 * @param message
	 * @param testConfig
	 * @author pramod.singh
	 */
	public static void logFail(String message, Config testConfig) {
		String tempMessage =  "[Fail] " + message;	
		testConfig.isFailScenarioStatus=true;
		testConfig.softAssert.fail(tempMessage);
		writeMessageInReport(testConfig,message);

		}
	
	/**
	 * 
	 * @param msg
	 * @param testConfig
	 */
	public static void logException(String msg, Config testConfig) {
		String tempMessage =  "[Exception e] " + msg;
		testConfig.isFailScenarioStatus=true;
		testConfig.softAssert.fail(tempMessage);
		writeMessageInReport(testConfig,tempMessage);
	}
	
	/**
	 * 
	 * @param e
	 * @param testConfig
	 */
	public static void logException(Exception e , Config testConfig) {
		String tempMessage =  "[Exception e] " + e.toString();	
		testConfig.isFailScenarioStatus=true;
		testConfig.softAssert.fail(tempMessage);
		writeMessageInReport(testConfig,tempMessage);
	}
	
	/**
	 * Function for Embed Message In Report
	 * @author ranjeet
	 * @param testConfig
	 * @param message
	 */
	public static void embedMessageAsHTMLInReport(Config testConfig ,String message) {		
		String msg = "<p style=\"color:red;\">"+message.replace("\n", "</br>")+"</span>";
		testConfig.testScenario.embed(msg.getBytes(), "text/html");
		testConfig.testLog = testConfig.testLog.concat(msg);
	}
}
