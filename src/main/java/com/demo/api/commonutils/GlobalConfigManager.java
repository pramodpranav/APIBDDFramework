package com.demo.api.commonutils;

import java.io.File;


import com.google.inject.Inject;

import cucumber.runtime.java.guice.ScenarioScoped;

@ScenarioScoped
public class GlobalConfigManager {
	protected static ThreadLocal<Config[]> threadLocalConfig = new ThreadLocal<Config[]>();
	String localConfigPath = System.getProperty("user.dir") +File.separator + "src/test/resources/Config/config.properties";
	public Config testConfig;
	//threadLocalConfig.set(new Config[] { testConfig });
	
	@Inject
    public void loadConfigInlocalThread() {
		String localConfigPath = System.getProperty("user.dir") +File.separator + "src/test/resources/Config/config.properties";
		testConfig = new Config(localConfigPath);
		threadLocalConfig.set(new Config[] { testConfig });
	}	
    
}