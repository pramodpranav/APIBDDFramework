@SearchCustomers
Feature: Verify user details api

  @Regression @Search_Customers
  Scenario: Verify to search customer Details using seach all customer API
    Given Run Authorization API and Get authorization Token and set this token in local properties file
    When Run get all user details API
    Then Verify API response code "200"
    And For get customers info API, Verify payload data

  @Regression @Search_Customers
  Scenario: Verify to search customer details API with invalid Authentication
    When Run get all user details API
    Then Verify API response code "401"
    And For invalid authorization, Validate key paramter of search all customers API payload

  @Regression @Search_Customers_by_phone_tc01
  Scenario Outline: Verify to search customer Details API using customer's phone nos
    Given Run Authorization API and Get authorization Token and set this token in local properties file
    When Run customer search api using phone number <phoneno>
    Then Verify API response code <responseCode>
    And For search customers by phone API, Verify payload data with phone nos <phoneno>

    Examples: 
      | phoneno      | responseCode |
      | "8037602400" | "200"        |
      | "9995879555" | "200"        |
      | "9972939567" | "200"        |

  @Regression @Search_Customers_by_phone
  Scenario Outline: Verify to search customer details using customer phone number with invalid Authentication
    When Run customer search api using phone number <phoneno>
    Then Verify API response code <responseCode>
    And For invalid authorization, Validate key paramter of search customer by phone API payload

    Examples: 
      | phoneno      | responseCode |
      | "8037602400" | "401"        |

  @Regression @Search_Customers_by_phone
  Scenario Outline: Verify to search customer Details API using customer's phone nos when phone number is invalid
    Given Run Authorization API and Get authorization Token and set this token in local properties file
    When Run customer search api using phone number <phoneno>
    Then Verify API response code <responseCode>
    

    Examples: 
      | phoneno                | responseCode |
      | "80376026534537434343" | "200"        |
      | "dchd53463546343"      | "200"        |
      | "!@#@$$%^&*()@@@"      | "200"        |
