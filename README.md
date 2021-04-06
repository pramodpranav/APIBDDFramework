# APIBDDFramework

** This project is based on Hybrid BDD framework using JAVA
    
    * Step definition,Runner files, and service hooks are in com/demo/api/stepdefinition package
    * Features file are in src/test/resources/APIFeatureFile
    * Common services are in com/demo/api/services

** To run All test scenarios use below maven build command on jenkins or terminal
    
    clean install -P RunProfile -DstepRunner=CommonRunner -Dcucumber.options="--tags @Regression" 


 ** Features files:

      a) User.feature : this feature file include test scenarios to create User, Update user info and Delete user
      b) WrokFlowEvent.feature : this feature file include generic test scenarios to verify Workflow event 

