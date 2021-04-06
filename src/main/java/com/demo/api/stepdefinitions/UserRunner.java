package com.demo.api.stepdefinitions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import com.demo.api.commonutils.Config;
import com.demo.api.commonutils.GlobalConfigManager;
import com.demo.api.commonutils.INVCommonTestBase;
import com.demo.api.pojoclasses.request.CreateUser;
import com.demo.api.services.APIService;
import com.demo.api.services.CommonHelper;
import com.demo.api.services.LoggerHelper;
import com.google.inject.Inject;

import cucumber.api.CucumberOptions;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import gherkin.deps.com.google.gson.Gson;
import io.cucumber.datatable.DataTable;

@CucumberOptions(features = {"src/test/resources/APIFeatureFile/"}, glue = {
"com/demo/api/stepdefinitions" }, tags = {"@WorkFlow1"} ,monochrome = true ,plugin = { "pretty",
		"html:target/cucumber-reports/cucumber-pretty", "json:target/cucumber-reports/CucumberTestReport.json",
		"rerun:target/cucumber-reports/rerun.txt" })
public class UserRunner extends INVCommonTestBase{
	@Inject
	private GlobalConfigManager testConfigManager;
	
	public Config testConfig;
	public String arrayOfUserReq=null;
	public static List<CreateUser> arrayOfNewUsersInfo =new ArrayList<CreateUser>();
	public static List<List<String>> eventexcelRows;
	public static JSONObject systemPerfJsonData;
	
	@When("^Run create multiple users api$")
    public void run_create_multiple_users_api(DataTable dataTable) throws Throwable {
		testConfig = testConfigManager.testConfig;
		String baseURL;
		int count=0;
		HashMap<String, String> apiHeaders = new HashMap<String, String>();
		Map<Object, Object> mapOfTestData = dataTable.asMaps(String.class, String.class).get(0);		
		apiHeaders.put("Content-Type", String.valueOf(mapOfTestData.get("ContentType")));
		baseURL= testConfig.getRunTimeProperty(String.valueOf(mapOfTestData.get("BaseURL")));
		arrayOfNewUsersInfo = new ArrayList<CreateUser>();
		count= Integer.valueOf(mapOfTestData.get("TotalNumberOfUser").toString());
		for(int i=0; i<count; i++) {
			CreateUser createUserObj = new CreateUser();
			createUserObj.intializeObjectForAllParameters(testConfig);
			arrayOfNewUsersInfo.add(createUserObj);
		}
		Gson gson = new Gson();
		arrayOfUserReq=gson.toJson(arrayOfNewUsersInfo);
		
		String fullURL = baseURL+ mapOfTestData.get("EndURL");
		testConfig.apiResponse = APIService.executeAndGetResponse(testConfig, fullURL,
					String.valueOf(mapOfTestData.get("RequestMethod")), apiHeaders,arrayOfUserReq); 
		
		
    }
	
	@Then("^Verify API response code \"([^\"]*)\" and message \"([^\"]*)\"$")
    public void verify_api_response_code_something_and_message_something(String responseCode, String responseMessage) throws Throwable {
		testConfig = testConfigManager.testConfig;
		JSONObject responseJson = APIService.parseResponseAsJSON(testConfig, testConfig.apiResponse);
		if(responseJson.getInt("code") == Integer.valueOf(responseCode)) {
			LoggerHelper.logPass(" Verify Response code : " + responseCode, testConfig);
		}
		else {
			LoggerHelper.logFail("[Fail] to Verify Response code, expected response code " + responseCode + " Actual response Code :" + responseJson.getInt("code"), testConfig);
		}
		
		if(responseJson.get("message").equals(responseMessage) ) {
			LoggerHelper.logPass(" Verify Response Message : " + responseMessage, testConfig);
		}
		else {
			LoggerHelper.logFail("[Fail] to Verify Response Message, expected response code " + responseMessage + " Actual response Message :" + responseJson.get("message"), testConfig);
		}
    }
	
	@And("^Verify all created users details$")
    public void verify_all_created_users_details(DataTable dataTable) throws Throwable {
		testConfig = testConfigManager.testConfig;
		
    }
	
	@And("^Delete All new created user$")
    public void delete_all_new_created_user(DataTable dataTable) throws Throwable {
		testConfig = testConfigManager.testConfig;
		String baseURL;
		
		HashMap<String, String> apiHeaders = new HashMap<String, String>();
		Map<Object, Object> mapOfTestData = dataTable.asMaps(String.class, String.class).get(0);		
		apiHeaders.put("Content-Type", String.valueOf(mapOfTestData.get("ContentType")));
		baseURL= testConfig.getRunTimeProperty(String.valueOf(mapOfTestData.get("BaseURL")));
		List<CreateUser> users = arrayOfNewUsersInfo;
		for(int i=0; i<users.size(); i++) {
			testConfig.putRunTimeProperty("userName", users.get(i).getUsername());
			String fullURL = baseURL+ CommonHelper.replaceArgumentsWithRunTimeProperties(testConfig,String.valueOf(mapOfTestData.get("EndURL")));
			testConfig.apiResponse = APIService.executeAndGetResponse(testConfig, fullURL,
					String.valueOf(mapOfTestData.get("RequestMethod")), apiHeaders);
			String message = APIService.parseResponseAsJSON(testConfig, testConfig.apiResponse).getString("message");
			if((testConfig.apiResponse.getStatusCode() == Integer.valueOf(String.valueOf(mapOfTestData.get("ResponseCode")))) && message.equalsIgnoreCase(users.get(i).getUsername())) {
				LoggerHelper.logPass(" Verify user deleted successfully ", testConfig);
			}
			else {
				LoggerHelper.logFail(" [Fail] to verify  user deleted successfully", testConfig);
			}
		}
    }
	
	@And("^Verify users info for all newly created users$")
    public void verify_users_info_for_all_newly_created_users(DataTable dataTable) throws Throwable {
		testConfig = testConfigManager.testConfig;
		String baseURL;
		
		HashMap<String, String> apiHeaders = new HashMap<String, String>();
		Map<Object, Object> mapOfTestData = dataTable.asMaps(String.class, String.class).get(0);		
		apiHeaders.put("Content-Type", String.valueOf(mapOfTestData.get("ContentType")));
		baseURL= testConfig.getRunTimeProperty(String.valueOf(mapOfTestData.get("BaseURL")));
		List<CreateUser> users = arrayOfNewUsersInfo;
		for(int i=0; i<users.size(); i++) {
			testConfig.putRunTimeProperty("userName", users.get(i).getUsername());
			String fullURL = baseURL+ CommonHelper.replaceArgumentsWithRunTimeProperties(testConfig,String.valueOf(mapOfTestData.get("EndURL")));
			testConfig.apiResponse = APIService.executeAndGetResponse(testConfig, fullURL,
					String.valueOf(mapOfTestData.get("RequestMethod")), apiHeaders); 
			Gson gson = new Gson();
			String jsonresstring = APIService.parseResponseAsJSON(testConfig, testConfig.apiResponse).toString();
			CreateUser user =gson.fromJson(jsonresstring, CreateUser.class);
			CommonHelper.verifyValueOfUserInfo(testConfig, users.get(i), user);
		}
		
    }
	
	@When("^Run user update api$")
    public void run_user_update_api(DataTable dataTable) throws Throwable {
		testConfig = testConfigManager.testConfig;
		String baseURL;
		int count=0;
		String reqBody;
		HashMap<String, String> apiHeaders = new HashMap<String, String>();
		Map<Object, Object> mapOfTestData = dataTable.asMaps(String.class, String.class).get(0);		
		apiHeaders.put("Content-Type", String.valueOf(mapOfTestData.get("ContentType")));
		baseURL= testConfig.getRunTimeProperty(String.valueOf(mapOfTestData.get("BaseURL")));
		CreateUser user = arrayOfNewUsersInfo.get(0);
		user.updateAllParamterExceptIdAndUserName(testConfig);
		arrayOfNewUsersInfo.clear();
		arrayOfNewUsersInfo.add(user);
		Gson gson = new Gson();
		reqBody=gson.toJson(user);
		testConfig.putRunTimeProperty("userName", user.getUsername());
		String fullURL = baseURL+ CommonHelper.replaceArgumentsWithRunTimeProperties(testConfig,String.valueOf(mapOfTestData.get("EndURL")));
		testConfig.apiResponse = APIService.executeAndGetResponse(testConfig, fullURL,
					String.valueOf(mapOfTestData.get("RequestMethod")), apiHeaders,reqBody); 
    }
	
	@Then("Verify API response code {string}")
	public void verify_API_response_code(String string) {
		testConfig = testConfigManager.testConfig;
		JSONObject responseJson = APIService.parseResponseAsJSON(testConfig, testConfig.apiResponse);
		if(testConfig.apiResponse.getStatusCode() == Integer.valueOf(string)) {
			LoggerHelper.logPass(" Verify Response code : " + string, testConfig);
		}
		else {
			LoggerHelper.logFail("[Fail] to Verify Response code, expected response code " + string + " Actual response Code :" + testConfig.apiResponse.getStatusCode(), testConfig);
		}
	}
	
	@When("^Read Event.txt file \"([^\"]*)\"$")
    public void read_eventtxt_file_something(String fileLoc) throws Throwable {
		testConfig = testConfigManager.testConfig;
		String filePath = System.getProperty("user.dir") + fileLoc;
		String systemPerfData = CommonHelper.readFile(filePath);
		systemPerfJsonData = new JSONObject(systemPerfData);
		
    }
	
	@And("^Read Event.xlsx file \"([^\"]*)\"$")
    public void read_eventxlsx_file_something(String fileLoc) throws Throwable {
		testConfig = testConfigManager.testConfig;
		String filePath = System.getProperty("user.dir") + fileLoc;
		eventexcelRows = CommonHelper.readxlsxFile(filePath, "Sheet1");
    }

	@Then("^Verify workflow event$")
    public void verify_workflow_event() throws Throwable {
		testConfig = testConfigManager.testConfig;
		Set<String> expectedSetOfData = new HashSet<String>();
		Set<String> actualSetOfData = new HashSet<String>();
		
		JSONArray jsonArray = systemPerfJsonData.getJSONArray("body");
		for(int i=0; i<jsonArray.length(); i++) {
			JSONObject jsonObj = jsonArray.getJSONObject(i);
			if(jsonObj.has("payload")) {
				actualSetOfData.add(jsonObj.getJSONObject("payload").getJSONObject("content").getString("type"));
			}
		}
		
		for(int i=0; i<eventexcelRows.size();i++ ) {
			if(i==0) {
				continue;
			}
			else {
				expectedSetOfData.add(eventexcelRows.get(i).get(2));
			}
		}
		
		expectedSetOfData.removeAll(actualSetOfData);
		if(expectedSetOfData.isEmpty()) {
			LoggerHelper.logPass(" Successfully Verified WorkFlow event ", testConfig);
		}
		else{
			LoggerHelper.logFail(" Faile to verify Workflow event, Following list of events not exist in system generated Event file  : " + expectedSetOfData , testConfig);
		}
		
    }
	
	
}
