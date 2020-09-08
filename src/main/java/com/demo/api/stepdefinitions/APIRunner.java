package com.demo.api.stepdefinitions;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.entity.ContentType;
import org.json.JSONArray;
import org.json.JSONObject;

import com.demo.api.commonutils.Config;
import com.demo.api.commonutils.GlobalConfigManager;
import com.demo.api.commonutils.INVCommonTestBase;
import com.demo.api.enums.APIMethodType;
import com.demo.api.services.APIService;
import com.demo.api.services.CommonHelper;
import com.demo.api.services.LoggerHelper;
import com.google.inject.Inject;

import cucumber.api.CucumberOptions;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

@CucumberOptions(features = {"src/test/resources/APIFeatureFile/GetUserDetails.feature","src/test/resources/APIFeatureFile/AuthorizationAPI.feature"}, glue = {
"com/demo/api/stepdefinitions" }, tags = {"@AuthorizationAPI_tc01"} ,monochrome = true ,plugin = { "pretty",
		"html:target/cucumber-reports/cucumber-pretty", "json:target/cucumber-reports/CucumberTestReport.json",
		"rerun:target/cucumber-reports/rerun.txt" })
public class APIRunner extends INVCommonTestBase{
	
	@Inject
	private GlobalConfigManager testConfigManager;
	public static Config testConfig;
	
	@Given("^Run Authorization API and Get authorization Token and set this token in local properties file$")
    public void run_authorization_api_and_get_authorization_token_and_set_this_token_in_local_properties_file() throws Throwable {
		testConfig = testConfigManager.testConfig;
		testConfig.wait(testConfig, 2);
		Map<String, String> token = APIService.getAuthorizationHeaderFromLoginAPI(testConfigManager.testConfig);
		testConfig.authorizationToken = token.get("token");
	}
	
	@When("^Run Authorization API with invalid user credential where username is \"([^\"]*)\" and password is \"([^\"]*)\"$")
    public void run_authorization_api_with_invalid_user_credential_where_username_is_something_and_password_is_something(String userName, String password)  {
		testConfig = testConfigManager.testConfig;
		String jsonBody ;
		testConfig.wait(testConfig, 2);
		HashMap<String, String> apiHeaders = new HashMap<String, String>();
		HashMap<String,String> requestBody = new HashMap<String,String>();
		requestBody.put("username", userName);
		requestBody.put("password", password);
		String fullURL = testConfig.getRunTimeProperty("APIBaseURL") + testConfig.getRunTimeProperty("AuthorizationAPIEndPoint");
		apiHeaders.put("Content-Type", "application/json");
		jsonBody = APIService.createJsonParameters(testConfig,requestBody);
		testConfig.apiResponse = APIService.executeAndGetResponse(testConfig, fullURL, APIMethodType.APIMethodsType.POST.getValue(), null,
				apiHeaders, jsonBody, true);
		
    }
	
	@Then("^Verify API response code \"([^\"]*)\"$")
    public void verify_api_response_code_something(String responseCode) throws Throwable {
		testConfig = testConfigManager.testConfig;
		testConfig.wait(testConfig, 2);
		//JSONObject responseObject = APIService.parseResponseAsJSON(testConfig, testConfig.apiResponse);
		String actualStatusCode = String.valueOf(testConfig.apiResponse.getStatusCode());
		if(actualStatusCode.equalsIgnoreCase(responseCode)) {
			LoggerHelper.logPass("Verified API response  code : "+ responseCode, testConfig);
		}
		else {
			LoggerHelper.logFail(" Expected response code : " +responseCode + " , And actual response code : "+  actualStatusCode, testConfig);
		}
    }
	
	@And("^For invalid authorization, Validate authorization API payload$")
    public void for_invalid_authorization_validate_authorization_api_payload(){
		testConfig = testConfigManager.testConfig;
		testConfig.wait(testConfig, 2);
		CommonHelper.responseKeyValidationFromAuthorizationAPI(testConfig, testConfig.apiResponse, "timestamp");
		CommonHelper.responseKeyValidationFromAuthorizationAPI(testConfig, testConfig.apiResponse, "status");
		CommonHelper.responseKeyValidationFromAuthorizationAPI(testConfig, testConfig.apiResponse, "error");
		CommonHelper.responseKeyValidationFromAuthorizationAPI(testConfig, testConfig.apiResponse, "message");
		CommonHelper.responseKeyValidationFromAuthorizationAPI(testConfig, testConfig.apiResponse, "path");
		JSONObject responseObject = APIService.parseResponseAsJSON(testConfig, testConfig.apiResponse);
		if(responseObject.has("error") && responseObject.get("error").toString().equalsIgnoreCase("Unauthorized")) {
			LoggerHelper.logPass(" Validate value of key 'error' : " + responseObject.get("error").toString(),testConfig);
		}
		else {
			LoggerHelper.logFail(" Expected value of error key is :Unauthorized  , And actual response code : "+  responseObject.get("error").toString(), testConfig);
		}
		
		if(responseObject.has("message") && responseObject.get("message").toString().equalsIgnoreCase("Unauthorized")) {
			LoggerHelper.logPass(" Validate value of key 'error' : " + responseObject.get("error").toString(),testConfig);
		}
		else {
			LoggerHelper.logFail(" Expected value of  key is 'message' is :Unauthorized  , And actual response code : "+  responseObject.get("error").toString(), testConfig);
		}
    }

	@When("^Run Authorization API with valid user credential where username is \"([^\"]*)\" and password is \"([^\"]*)\"$")
    public void run_authorization_api_with_valid_user_credential_where_username_is_something_and_password_is_something(String username, String password) throws Throwable {
		testConfig = testConfigManager.testConfig;
		testConfig.wait(testConfig, 2);
		HashMap<String, String> apiHeaders = new HashMap<String, String>();
		HashMap<String,String> requestBody = new HashMap<String,String>();
		requestBody.put("username", username);
		requestBody.put("password", password);
		String fullURL = testConfig.getRunTimeProperty("APIBaseURL") + testConfig.getRunTimeProperty("AuthorizationAPIEndPoint");
		apiHeaders.put("Content-Type", "application/json");
		testConfig.apiResponse = APIService.executeAndGetResponse(testConfig, fullURL, APIMethodType.APIMethodsType.POST.getValue(), requestBody,
				apiHeaders, null, true);
    }
	

    @And("^For authorization API, Validate authorization API payload$")
    public void for_authorization_api_validate_authorization_api_payload() throws Throwable {
    	testConfig = testConfigManager.testConfig;
    	testConfig.wait(testConfig, 2);
		CommonHelper.responseKeyValidationFromAuthorizationAPI(testConfig, testConfig.apiResponse, "token");
    }
    
    @When("^Run get all user details API$")
    public void run_get_all_user_details_api() throws Throwable {
    	testConfig = testConfigManager.testConfig;
    	HashMap<String, String> apiHeaders = new HashMap<String, String>();
    	String fullURL = "";
    	testConfig.wait(testConfig, 2);
    	apiHeaders.put("Authorization", "Bearer " + testConfig.authorizationToken );
    	apiHeaders.put("Content-Type", ContentType.APPLICATION_FORM_URLENCODED.getMimeType());    	
    	fullURL = testConfig.getRunTimeProperty("APIBaseURL") + testConfig.getRunTimeProperty("APIEndPoint_search_customers");
    	testConfig.apiResponse = APIService.executeAndGetResponse(testConfig, fullURL, APIMethodType.APIMethodsType.GET.getValue(), null,
				apiHeaders, null, true);
    }
    
    @Then("For get customers info API, Verify payload data")
    public void for_get_customers_info_API_Verify_payload_data() {
    	testConfig = testConfigManager.testConfig;
    	List<HashMap<String,String>> listOfMapOfExpectedRecords = new ArrayList<HashMap<String,String>>();
    	HashMap<String,String> userInfo = new HashMap<String,String>();
    	testConfig.wait(testConfig, 2);
    	//will fetched from DB but temp it is hard coded
    	int exp_totalNumberOfRecords = 3;
    	userInfo.put("first_name", "Aliko");
    	userInfo.put("last_name", "Dangote");
    	userInfo.put("career", "Billionaire Industrialist");
    	userInfo.put("phone", "8037602400");
    	
    	listOfMapOfExpectedRecords.add(userInfo);
    	userInfo = new HashMap<String,String>();
    	userInfo.put("first_name", "Bill");
    	userInfo.put("last_name", "Gates");
    	userInfo.put("career", "Billionaire Tech Entrepreneur");
    	userInfo.put("phone", "9972939567");
    	
    	listOfMapOfExpectedRecords.add(userInfo);
    	userInfo = new HashMap<String,String>();
    	userInfo.put("first_name", "Folrunsho");
    	userInfo.put("last_name", "Alakija");
    	userInfo.put("career", "Billionaire Oil Magnate");
    	userInfo.put("phone", "9995879555");
    	listOfMapOfExpectedRecords.add(userInfo);
    	
    	for(int i=0; i<exp_totalNumberOfRecords; i++) {
    		
    	}
    	//CommonHelper.responseKeyValidationFromAuthorizationAPI(testConfig, testConfig.apiResponse, "first_name");
    	
    	CommonHelper.verifyUserDetailsAPIResponseData(testConfig,  testConfig.apiResponse, listOfMapOfExpectedRecords);  	
    }
    
    @And("^For invalid authorization, Validate key paramter of search all customers API payload$")
    public void for_invalid_authorization_validate_key_paramter_of_search_all_customers_api_payload() throws Throwable {
    	testConfig = testConfigManager.testConfig;
    	testConfig.wait(testConfig, 2);
		CommonHelper.responseKeyValidationFromAuthorizationAPI(testConfig, testConfig.apiResponse, "timestamp");
		CommonHelper.responseKeyValidationFromAuthorizationAPI(testConfig, testConfig.apiResponse, "status");
		CommonHelper.responseKeyValidationFromAuthorizationAPI(testConfig, testConfig.apiResponse, "error");
		CommonHelper.responseKeyValidationFromAuthorizationAPI(testConfig, testConfig.apiResponse, "message");
		CommonHelper.responseKeyValidationFromAuthorizationAPI(testConfig, testConfig.apiResponse, "path");
		JSONObject responseObject = APIService.parseResponseAsJSON(testConfig, testConfig.apiResponse);
		if(responseObject.has("error") && responseObject.get("error").toString().equalsIgnoreCase("Unauthorized")) {
			LoggerHelper.logPass(" Validate value of key 'error' : " + responseObject.get("error").toString(),testConfig);
		}
		else {
			LoggerHelper.logFail(" Expected value of error key is :Unauthorized  , And actual response code : "+  responseObject.get("error").toString(), testConfig);
		}
		
		if(responseObject.has("message") && responseObject.get("message").toString().equalsIgnoreCase("Unauthorized")) {
			LoggerHelper.logPass(" Validate value of key 'error' : " + responseObject.get("error").toString(),testConfig);
		}
		else {
			LoggerHelper.logFail(" Expected value of  key is 'message' is :Unauthorized  , And actual response code : "+  responseObject.get("error").toString(), testConfig);
		}
    }
    
    @And("^For invalid authorization, Validate key paramter of search customer by phone API payload$")
    public void for_invalid_authorization_validate_key_paramter_of_search_customer_by_phone_api_payload() throws Throwable {
    	testConfig = testConfigManager.testConfig;
    	testConfig.wait(testConfig, 2);
		CommonHelper.responseKeyValidationFromAuthorizationAPI(testConfig, testConfig.apiResponse, "timestamp");
		CommonHelper.responseKeyValidationFromAuthorizationAPI(testConfig, testConfig.apiResponse, "status");
		CommonHelper.responseKeyValidationFromAuthorizationAPI(testConfig, testConfig.apiResponse, "error");
		CommonHelper.responseKeyValidationFromAuthorizationAPI(testConfig, testConfig.apiResponse, "message");
		CommonHelper.responseKeyValidationFromAuthorizationAPI(testConfig, testConfig.apiResponse, "path");
		JSONObject responseObject = APIService.parseResponseAsJSON(testConfig, testConfig.apiResponse);
		if(responseObject.has("error") && responseObject.get("error").toString().equalsIgnoreCase("Unauthorized")) {
			LoggerHelper.logPass(" Validate value of key 'error' : " + responseObject.get("error").toString(),testConfig);
		}
		else {
			LoggerHelper.logFail(" Expected value of error key is :Unauthorized  , And actual response code : "+  responseObject.get("error").toString(), testConfig);
		}
		
		if(responseObject.has("message") && responseObject.get("message").toString().equalsIgnoreCase("Unauthorized")) {
			LoggerHelper.logPass(" Validate value of key 'error' : " + responseObject.get("error").toString(),testConfig);
		}
		else {
			LoggerHelper.logFail(" Expected value of  key is 'message' is :Unauthorized  , And actual response code : "+  responseObject.get("error").toString(), testConfig);
		}
		
    }
    
    @When("^Run customer search api using phone number$")
    public void run_customer_search_api_using_phone_number() throws Throwable {
    	testConfig = testConfigManager.testConfig;
    	HashMap<String, String> apiHeaders = new HashMap<String, String>();
    	String fullURL = "";
    	testConfig.wait(testConfig, 2);
    	apiHeaders.put("Authorization", "Bearer " + testConfig.authorizationToken );
    	apiHeaders.put("Content-Type", ContentType.APPLICATION_FORM_URLENCODED.getMimeType());    	
    	fullURL = testConfig.getRunTimeProperty("APIBaseURL") + testConfig.getRunTimeProperty("UsersDetailsAPIEndPoint");
    	testConfig.apiResponse = APIService.executeAndGetResponse(testConfig, fullURL, APIMethodType.APIMethodsType.GET.getValue(), null,
				apiHeaders, null, true);
    }
    
    @When("^Run customer search api using phone number \"([^\"]*)\"$")
    public void run_customer_search_api_using_phone_number_something(String phonenos) throws Throwable {
    	testConfig = testConfigManager.testConfig;
    	HashMap<String, String> apiHeaders = new HashMap<String, String>();
    	String fullURL = "";
    	testConfig.wait(testConfig, 2);
    	apiHeaders.put("Authorization", "Bearer " + testConfig.authorizationToken );
    	apiHeaders.put("Content-Type", ContentType.APPLICATION_FORM_URLENCODED.getMimeType());    	
    	fullURL = testConfig.getRunTimeProperty("APIBaseURL") + testConfig.getRunTimeProperty("APIEndPoint_Search_Customer_phone")+"/"+phonenos;
    	testConfig.apiResponse = APIService.executeAndGetResponse(testConfig, fullURL, APIMethodType.APIMethodsType.GET.getValue(), null,
				apiHeaders, null, true);
    }
    
    @And("^For search customers by phone API, Verify payload data with phone nos \"([^\"]*)\"$")
    public void for_search_customers_by_phone_api_verify_payload_data_with_phone_nos_something(String phonenos) throws Throwable {
    	testConfig = testConfigManager.testConfig;
    	List<HashMap<String,String>> listOfMapOfExpectedRecords = new ArrayList<HashMap<String,String>>();
    	HashMap<String,String> userInfo = new HashMap<String,String>();
    	//will fetched from DB but on basis of phone nos, for now here records it is hard coded
    	testConfig.wait(testConfig, 2);
    	userInfo.put("first_name", "Aliko");
    	userInfo.put("last_name", "Dangote");
    	userInfo.put("career", "Billionaire Industrialist");
    	userInfo.put("phone", "8037602400");
    	
    	listOfMapOfExpectedRecords.add(userInfo);
    	userInfo = new HashMap<String,String>();
    	userInfo.put("first_name", "Bill");
    	userInfo.put("last_name", "Gates");
    	userInfo.put("career", "Billionaire Tech Entrepreneur");
    	userInfo.put("phone", "9972939567");
    	
    	listOfMapOfExpectedRecords.add(userInfo);
    	userInfo = new HashMap<String,String>();
    	userInfo.put("first_name", "Folrunsho");
    	userInfo.put("last_name", "Alakija");
    	userInfo.put("career", "Billionaire Oil Magnate");
    	userInfo.put("phone", "9995879555");
    	listOfMapOfExpectedRecords.add(userInfo);
    	
    	for(int i=0; i<listOfMapOfExpectedRecords.size(); i++) {
    		if(! (listOfMapOfExpectedRecords.get(i).get("phone").equalsIgnoreCase(phonenos))) {
    				listOfMapOfExpectedRecords.remove(i);
    				i--;
    		}
    	}
    	CommonHelper.verifyUserDetailsAPIResponseData(testConfig,  testConfig.apiResponse, listOfMapOfExpectedRecords);  	

    }
}
