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
	
}
