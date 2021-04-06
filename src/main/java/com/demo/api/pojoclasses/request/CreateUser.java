package com.demo.api.pojoclasses.request;

import java.util.List;
import java.util.Map;

import javax.annotation.Generated;

import org.json.JSONArray;
import org.json.JSONObject;

import com.demo.api.commonutils.Config;
import com.demo.api.services.CommonHelper;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.response.ResponseBody;

import gherkin.deps.com.google.gson.Gson;

@Generated("jsonschema2pojo")
public class CreateUser {

	@SerializedName("id")
	@Expose
	private Integer id;
	@SerializedName("username")
	@Expose
	private String username;
	@SerializedName("firstName")
	@Expose
	private String firstName;
	@SerializedName("lastName")
	@Expose
	private String lastName;
	@SerializedName("email")
	@Expose
	private String email;
	@SerializedName("password")
	@Expose
	private String password;
	@SerializedName("phone")
	@Expose
	private String phone;
	@SerializedName("userStatus")
	@Expose
	private Integer userStatus;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Integer getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(Integer userStatus) {
		this.userStatus = userStatus;
	}

	/**
	 * 
	 * @param testConfig
	 * @param testCaseId
	 * @return
	 */
	public Object intializeObjectForAllParameters(Config testConfig) {

		int id = (int) CommonHelper.generateRandomNumber(8);
		String username = "Test" + CommonHelper.generateRandomAlphabetsString(10);
		String firstName = "TestAutomation" + CommonHelper.generateRandomAlphabetsString(10);
		;
		String lastName = CommonHelper.generateRandomAlphabetsString(10);
		String email = "testemail" + CommonHelper.generateRandomNumber(10) + "@mailinator.com";
		String password = "Password$" + CommonHelper.generateRandomNumber(10);
		String phone = "4" + CommonHelper.generateRandomNumber(9);
		int userStatus = (int) CommonHelper.generateRandomNumber(1);
		this.setId(id);
		this.setFirstName(firstName);
		this.setLastName(lastName);
		this.setEmail(email);
		this.setUsername(username);
		this.setPassword(password);
		this.setPhone(phone);
		this.setUserStatus(userStatus);
		return this;
	}
	
	/**
	 * 
	 * @param testConfig
	 * @return
	 */
	public Object updateAllParamterExceptIdAndUserName(Config testConfig) {
	
		String firstName = CommonHelper.generateRandomAlphabetsString(10);
		String lastName = CommonHelper.generateRandomAlphabetsString(10);
		String email = "testemail" + CommonHelper.generateRandomNumber(10) + "@mailinator.com";
		String password = "Password$" + CommonHelper.generateRandomNumber(10);
		String phone = "4" + CommonHelper.generateRandomNumber(9);
		int userStatus = (int) CommonHelper.generateRandomNumber(1);
		this.setId(id);
		this.setFirstName(firstName);
		this.setLastName(lastName);
		this.setEmail(email);
		this.setUsername(username);
		this.setPassword(password);
		this.setPhone(phone);
		this.setUserStatus(userStatus);
		return this;
	}

}
