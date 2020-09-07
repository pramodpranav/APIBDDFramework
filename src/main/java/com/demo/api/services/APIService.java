package com.demo.api.services;

import static com.jayway.restassured.RestAssured.given;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.demo.api.commonutils.Config;
import com.demo.api.enums.APIMethodType.APIMethodsType;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.config.EncoderConfig;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Header;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;


public class APIService {

	/**
	 * 
	 * @param testConfig
	 * @param fullUrl	Complete API request URL (baseUrl + command + parameters)
	 * @param method	get/post/delete/put
	 * @param apiParameters	API Query parameters, if it is null the excel parameters will be used
	 * @param apiHeaders	API Headers
	 * @return complete raw restassured Response
	 */
	public static Response executeAndGetResponse(Config testConfig, String fullUrl, String methodType, Map<String, String> apiParameters, Map<String, String> apiHeaders,String jsonBody,Boolean paraminbody){

		//remove the query params from full url
		String[] cmd = fullUrl.split("\\?");
		String requestUrl = cmd[0];
		// Prepare request
		RequestSpecification reqspec = given();

		if (apiHeaders != null && apiHeaders.size() > 0)
		{   
			// set request headers
			for (Entry<String, String> entry : apiHeaders.entrySet()) 
			{
				String key = entry.getKey();
				String value = entry.getValue();
				reqspec = reqspec
						.header(key, value);
			}
		}

		reqspec=reqspec.config(RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().defaultContentCharset("UTF-8")));

		if(paraminbody && jsonBody!=null){
			reqspec = reqspec.body(jsonBody);
		}
		else if(apiParameters != null)
		{
			// set request query params from passed in HashMap
			if (apiParameters != null && apiParameters.size() > 0) 
			{
				for (Entry<String, String> entry : apiParameters.entrySet()) 
				{
					String key = entry.getKey();
					String value = entry.getValue();
					reqspec = reqspec
							.queryParam(key, value);
				}
			}
		}
		else if(cmd.length >1){
			String parameters[] = cmd[1].split("&");
			for(int i=0 ; i<parameters.length; i++) {
				String key = parameters[i].split("=")[0];
				String value=parameters[i].split("=")[1];
				reqspec = reqspec
						.queryParam(key, value);
			}
		}

		// Log the request details
		reqspec = reqspec
				.log().all();

		// Execute API
		reqspec = reqspec
				.when();

		Response response = null;
		switch(methodType.toLowerCase())
		{
		case "get":
			response = reqspec
			.get(requestUrl);
			break;
		case "post":
			response = reqspec
			.post(requestUrl);
			break;
		case "delete":
			response = reqspec
			.delete(requestUrl);
			break;
		case "put":
			response = reqspec
			.put(requestUrl);
			break;
		}

		response = response
				.then()
				.log().all()
				.extract()
				.response();

		LoggerHelper.logComment(testConfig,"API Response for " + requestUrl + " :- "+ response.asString());
		return response;
	}

	/**
	 * Extracts the Json Body response from the raw restassured Response
	 * @param testConfig
	 * @param response	complete raw restassured Response
	 * @return	Json response body
	 */
	public static JSONObject parseResponseAsJSON(Config testConfig, Response response)
	{
		String responseAsString = response.asString();
		JSONObject jObject = null;

		switch (response.getStatusCode()) 
		{
		case 504:
			testConfig.wait(testConfig, 90);
			break;
		case 505:
			testConfig.wait(testConfig, 30);
			break;
		default:
			responseAsString = response.asString();
			break;
		}

		try 
		{
			jObject = new JSONObject(responseAsString);
			return jObject;
		} 
		catch (JSONException e) {
			LoggerHelper.logException(e,testConfig);
		} 

		return null;
	}

	/** Convert Map of parameters 
	 * @param testConfig
	 * @param parameters
	 * @return paramaters in JSON format
	 */
	public static String createJsonParameters(Config testConfig, HashMap<String, String> parameters){
		JSONObject jsonPostParameters = new JSONObject();
		//JSONArray jsonBodyArray = new JSONArray();
		for (Entry<String, String> entry : parameters.entrySet())
		{
			String key = entry.getKey();
			String value = entry.getValue();
			try {
				 jsonPostParameters.put(key, value);
				
			} catch (JSONException e) {
				LoggerHelper.logException(e,testConfig);
			}
		}
		//jsonBodyArray.put(jsonPostParameters);
		return jsonPostParameters.toString();
	}

	/**
	 * Gets the Authorization value, which is required by the API's
	 * @param testConfig
	 * @param apiUrl	API request URL (baseUrl + command)
	 * @return
	 */
	public static HashMap<String, String> getAuthorizationHeaderFromLoginAPI(Config testConfig)
	{
		HashMap<String, String> authorization = new HashMap<String, String>();
		HashMap<String,String> header = new HashMap<String,String>();
		try
		{
			LoggerHelper.logComment(testConfig,"<<---------------Need to Get Authorization for this API------------------->>");
			HashMap<String, String> apiParameters = new HashMap<String, String>();
			apiParameters.put("username", testConfig.getRunTimeProperty("userName"));
			apiParameters.put("password", testConfig.getRunTimeProperty("password"));
			String fullUrl = testConfig.getRunTimeProperty("APIBaseURL") + testConfig.getRunTimeProperty("AuthorizationAPIEndPoint");
			String jsonBody = createJsonParameters(testConfig,apiParameters);
			header.put("Content-Type", "application/json");
			Response response = executeAndGetResponse( testConfig,  fullUrl,APIMethodsType.POST.getValue(), null,header, jsonBody,true);
			authorization.put("token", response.jsonPath().getString("token"));
			authorization.put("cookie", response.getHeader("Set-Cookie"));
			LoggerHelper.logComment(testConfig,"<<---------------Got Authorization userVal as:- " + authorization + "------------------->>");
			return authorization;
		}
		catch(Exception e)
		{
			LoggerHelper.logException(e,testConfig);
		}

		return null;
	}
	
	/**
	 * Function to convert JSON file to JSON object
	 * @param fileLocationURL
	 * @return
	 */
	public static JSONObject parseJSONFileInJSONObject(String fileLocationURL) {
		
		JSONObject jo = new JSONObject();
		InputStream  is;
		try {
			is = new FileInputStream(fileLocationURL);
	        JSONTokener tokener = new JSONTokener(is);
	        jo = new JSONObject(tokener);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}        
        // typecasting obj to JSONObject
		
		return jo;
	}
	/**
	 * To call API with Parameter in API
	 * @param testConfig
	 * @param fullUrl
	 * @param methodType
	 * @param apiParameters
	 * @param apiHeaders
	 * @param jsonBody
	 * @param paraminbody
	 * @return
	 */
	public static Response executeAndGetResponse(Config testConfig, String fullUrl, String methodType, Map<String, String> apiParameters, Map<String, String> apiHeaders) {
		return executeAndGetResponse(testConfig,fullUrl,methodType,apiParameters,apiHeaders,null,false);
	}

	/**
	 *  For API request with Parameter request in Body
	 * @param testConfig
	 * @param fullUrl
	 * @param methodType
	 * @param apiHeaders
	 * @param apiInBody
	 * @return
	 */
	public static Response executeAndGetResponse(Config testConfig, String fullUrl, String methodType, Map<String, String> apiHeaders,String apiInBody) {
		return executeAndGetResponse(testConfig,fullUrl,methodType,null,apiHeaders,null,true);
	}
}
