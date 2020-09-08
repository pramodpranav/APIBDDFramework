# APIBDDFramework

** This project is based on Hybrid BDD framework using JAVA
    
    * Step definition,Runner files, and service hooks are in com/demo/api/stepdefinition package
    * Features file are in src/test/resources/APIFeatureFile
    * Common services are in com/demo/api/services

** To run All test scenarios use below maven build command on jenkins or terminal
    
    clean install -P RunProfile -DstepRunner=CommonRunner -Dcucumber.options="--tags @Regression" 


 ** Features files:

      a) AuthorizationAPI.feature : this feature file include test scenarios Authorization api (/authenticate)
      b) GetUserDetailsAPI : This features file include test scenarios of get all customer info and get customer info 
      by customer phone numbers, API are ( /api/v1/users ) and ( /api/v1/users/{phone})

** Test scenarios covered in automation : 

      a) Authorization API : 
          * Verify payload, token validation, and response code incase of valid username and password
          * Verify payload, and response code incase of Invalid username and password test data
           Test data example : 
           Examples: 
                | username  | password    | APIResponseCode |
                | "rupeek"  | " password" | "401"           |
                | " rupeek" | "password"  | "401"           |
                | "rupee"   | "passwor"   | "401"           |
                | "!@@@"    | " @@@@"     | "401"           |
                | ""        | "password"  | "401"           |
                | "rupeek"  | ""          | "401"           |

      b) Get all customer info api (/api/v1/users)
          * Verify payload and response code in case of invalid authorization token
          * Verify key parameter and key value of payload in case of successfull response
          * Verify response code in case of successfull response
      c) Get customer info by phone no API (/api/v1/users/{phone}):
      	  * Verify payload and response code in case of invalid authorization token
          * Verify response code in case of successfull response
          * Verify key parameter and key value of payload in case of successfull response with below set of valid test data of phone nos
                Examples: 
      				| phoneno      | responseCode |
      				| "8037602400" | "200"        |
      				| "9995879555" | "200"        |
      				| "9972939567" | "200"        |

      	   * Verify response code with below set of InValid set of test data of phone nos
                Examples: 
      				| phoneno      | responseCode |
      				| "8037602400" | "200"        |
      				| "9995879555" | "200"        |
      				| "9972939567" | "200"        |
