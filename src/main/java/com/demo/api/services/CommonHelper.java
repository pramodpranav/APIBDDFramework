package com.demo.api.services;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.demo.api.commonutils.Config;
import com.jayway.restassured.response.Response;

public class CommonHelper {

	/**
	 * 
	 * @param testConfig
	 * @param response
	 * @param key
	 */
	public static void responseKeyValidationFromAuthorizationAPI(Config testConfig, Response response, String key) {
		org.json.JSONObject responseObject = APIService.parseResponseAsJSON(testConfig, testConfig.apiResponse);
		if (responseObject.has(key) && responseObject.get(key) != null) {
			LoggerHelper.logPass(" Validate value of key  " + key, testConfig);
		} else {
			LoggerHelper.logFail(" To Validate value of key  " + key, testConfig);
		}
	}

	/**
	 * 
	 * @param testConfig
	 * @param response
	 * @param expectedListOfRecords
	 */
	public static void verifyUserDetailsAPIResponseData(Config testConfig, Response response,
			List<HashMap<String, String>> expectedListOfRecords) {
		
		Object jsonResponse = null;
		JSONParser parser = new JSONParser();
		try {
			jsonResponse = parser.parse(response.body().asString());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JSONArray JSONResponseBody = new JSONArray();
		JSONObject jsonRequestObj;
		List<HashMap<String, String>> listOfMapOfActualRecords = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> userInfo;

		if (jsonResponse instanceof JSONArray) {
			JSONResponseBody = (JSONArray) jsonResponse ; 
					//new JSONArray(response.body().asString());
		} else {
			jsonRequestObj = (JSONObject) jsonResponse;
					//new JSONObject(response.body().asString());
			userInfo = new HashMap<String, String>();
			userInfo.put("first_name", jsonRequestObj.get("first_name").toString());
			userInfo.put("last_name", jsonRequestObj.get("last_name").toString());
			userInfo.put("career", jsonRequestObj.get("career").toString());
			userInfo.put("phone", jsonRequestObj.get("phone").toString());
			listOfMapOfActualRecords.add(userInfo);
		}
		for (int i = 0; i < JSONResponseBody.size(); i++) {
			userInfo = new HashMap<String, String>();
			JSONObject jsonObj = (JSONObject) JSONResponseBody.get(i);
			userInfo.put("first_name", jsonObj.get("first_name").toString());
			userInfo.put("last_name", jsonObj.get("last_name").toString());
			userInfo.put("career", jsonObj.get("career").toString());
			userInfo.put("phone", jsonObj.get("phone").toString());
			listOfMapOfActualRecords.add(userInfo);
		}

		Map<List<?>, Map<String, String>> inputKeys = listOfMapOfActualRecords.stream()
				.collect(Collectors.toMap(
						m -> Arrays.asList(m.get("first_name"), m.get("last_name"), m.get("career"), m.get("phone")),
						m -> m, (a, b) -> {
							throw new IllegalStateException("duplicate " + a + " and " + b);
						}, LinkedHashMap::new));

		List<Map<String, String>> matchinRecords = expectedListOfRecords.stream()
				.filter(m -> inputKeys.containsKey(
						Arrays.asList(m.get("first_name"), m.get("last_name"), m.get("career"), m.get("phone"))))
				.collect(Collectors.toList());

		matchinRecords.forEach(m -> inputKeys
				.remove(Arrays.asList(m.get("first_name"), m.get("last_name"), m.get("career"), m.get("phone"))));
		List<Map<String, String>> notMatchinRecords = new ArrayList<>(inputKeys.values());
		if (listOfMapOfActualRecords.size() == expectedListOfRecords.size()) {
			LoggerHelper.logPass(" Successfully verify total count of records in payload, total count : "
					+ listOfMapOfActualRecords.size(), testConfig);
		} else {
			LoggerHelper.logFail(" Expected total records are : " + expectedListOfRecords.size()
					+ " Actual total records : " + listOfMapOfActualRecords.size(), testConfig);
		}
		if (notMatchinRecords.size() == 0) {
			LoggerHelper.logPass(" Successfully Verified all records of user " + listOfMapOfActualRecords, testConfig);
		} else {
			LoggerHelper.logFail(
					" Some unmatched records are not in response, unmatched records are : " + notMatchinRecords,
					testConfig);
		}
	}
}
