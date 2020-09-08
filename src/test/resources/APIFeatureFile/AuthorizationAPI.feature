#Author: your.email@your.domain.com
@AuthorizationAPI
Feature: Verify Authorization API

  @Regression @AuthorizationAPI_tc01
  Scenario Outline: Validate Negative test scenarios for authorization API
    When Run Authorization API with invalid user credential where username is <username> and password is <password>
    Then Verify API response code <APIResponseCode>
    And For invalid authorization, Validate authorization API payload

    Examples: 
      | username  | password    | APIResponseCode |
      | "rupeek"  | " password" | "401"           |
      | " rupeek" | "password"  | "401"           |
      | "rupee"   | "passwor"   | "401"           |
      | "!@@@"    | " @@@@"     | "401"           |
      | ""        | "password"  | "401"           |
      | "rupeek"  | ""          | "401"           |


@Regression
  Scenario Outline: Validate Negative test scenarios for authorization API
    When Run Authorization API with valid user credential where username is <username> and password is <password>
    Then Verify API response code <APIResponseCode>
    And For authorization API, Validate authorization API payload

    Examples: 
      | username  | password    | APIResponseCode |
      | "rupeek"  | "password" | "200"           |