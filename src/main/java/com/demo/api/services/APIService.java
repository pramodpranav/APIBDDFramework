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
	 * @param apiParameters	API Query parameters
	 * @param apiHeaders	API Headers
	 * @return complete raw restassured Response
	 */
	public static Response executeAndGetResponse(Config testConfig, String fullUrl, String methodType, Map<String, String> apiParameters, Map<String, String> apiHeaders,String jsonBody){

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

		//reqspec=reqspec.config(RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().defaultContentCharset("UTF-8")));

		if(jsonBody!=null){
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
			.get(fullUrl);
			break;
		case "post":
			response = reqspec
			.post(fullUrl);
			break;
		case "delete":
			response = reqspec
			.delete(fullUrl);
			break;
		case "put":
			response = reqspec
			.put(fullUrl);
			break;
		}

		response = response
				.then()
				.log().all()
				.extract()
				.response();

		LoggerHelper.logComment(testConfig,"API Response for " + fullUrl + " :- "+ response.asString());
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
	public static Response executeAndGetResponse(Config testConfig, String fullUrl, String methodType, Map<String, String> queryParameter, Map<String, String> apiHeaders) {
		return executeAndGetResponse(testConfig,fullUrl,methodType,queryParameter,apiHeaders,null);
	}

	/**
	 *  For API request with Parameter request in Body
	 * @param testConfig
	 * @param fullUrl
	 * @param methodType
	 * @param apiHeaders
	 * @param jsonBody
	 * @return
	 */
	public static Response executeAndGetResponse(Config testConfig, String fullUrl, String methodType, Map<String, String> apiHeaders,String jsonBody) {
		return executeAndGetResponse(testConfig,fullUrl,methodType,null,apiHeaders,jsonBody);
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
	public static Response executeAndGetResponse(Config testConfig, String fullUrl, String methodType, Map<String, String> apiHeaders) {
		return executeAndGetResponse(testConfig,fullUrl,methodType,null,apiHeaders,null);
	}
}
