@SearchCustomers
Feature: Verify Work Flow event

  @Regression @WorkFlow1
  Scenario Outline: Verify work flow event on basis of excel file
    When Read Event.txt file <EventTxtSourcePath>
    And Read Event.xlsx file <EventxlsSourcePath>
    Then Verify workflow event 

    Examples: 
      | EventTxtSourcePath                       | EventxlsSourcePath                        |
      | "/src/test/resources/TestData/Event.txt" | "/src/test/resources/TestData/Event.xlsx" |
