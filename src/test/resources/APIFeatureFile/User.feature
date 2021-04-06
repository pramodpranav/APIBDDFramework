#Author: your.email@your.domain.com
@AuthorizationAPI
Feature: Verify User Operations

  @Regression @test1
  Scenario: Verify Flow of Create multiple users with array
    When Run create multiple users api
      | ModuleName | BaseURL    | EndURL                | ContentType      | RequestMethod | TotalNumberOfUser |
      | User_Auth  | APIBaseURL | /user/createWithArray | application/json | Post          |                 2 |
    Then Verify API response code "200" and message "ok"
    And Verify users info for all newly created users
      | ModuleName | BaseURL    | EndURL            | ContentType      | RequestMethod |
      | User_Auth  | APIBaseURL | /user/{$userName} | application/json | get           |
    And Delete All new created user
      | ModuleName | BaseURL    | EndURL            | ContentType      | RequestMethod | ResponseCode |
      | User_Auth  | APIBaseURL | /user/{$userName} | application/json | delete        |          200 |

  @Regression @test2
  Scenario: Verify Flow to update user info
    When Run create multiple users api
      | ModuleName | BaseURL    | EndURL                | ContentType      | RequestMethod | TotalNumberOfUser |
      | User_Auth  | APIBaseURL | /user/createWithArray | application/json | Post          |                 1 |
    Then Verify API response code "200" and message "ok"
    When Run user update api
      | ModuleName | BaseURL    | EndURL            | ContentType      | RequestMethod |
      | User_Auth  | APIBaseURL | /user/{$userName} | application/json | put           |
    Then Verify API response code "200"
    And Verify users info for all newly created users
      | ModuleName | BaseURL    | EndURL            | ContentType      | RequestMethod |
      | User_Auth  | APIBaseURL | /user/{$userName} | application/json | get           |
    And Delete All new created user
      | ModuleName | BaseURL    | EndURL            | ContentType      | RequestMethod | ResponseCode |
      | User_Auth  | APIBaseURL | /user/{$userName} | application/json | delete        |          200 |
