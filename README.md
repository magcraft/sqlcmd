# README #

This README would normally document whatever steps are necessary to get your application up and running.

### What is this repository for? ###

* Quick summary
  This is my console SQL client project
* Version
  beta

### How do I get set up? ###

* Summary of set up
This is a maven based project
Uses PostgreSQL 9.1.9 on x86_64-unknown-linux-gnu, compiled by gcc (GCC) 4.1.1 20070105 (Red Hat 4.1.1-51), 64-bit 

* Configuration
 - Main class ua.com.juja.magcraft.sqlcmd.controller.Main

 - There is a config/sqlcmd.properties with congiguration information for connection
  contains database.user.name & database.user.password - using only for tests

### Support commands ###
* connect|YourDatabaseName|YourDatabaseUser|YourDatabasePassword - connect to the YourDatabase
* exit -  close the application
* find|TableName - show content of the table. Which name is TableName
* clear|TableName - clear content of the table. Which name is TableName
* create|TableName|column1|value1|...|columnN|valueN - create new row in the table. Which name is TableName
* tables - if you need to get list of tables in the database
* help - supports comands information message

### Dependencies ###
* postgresql version 42.1.1
* junit version 4.12
* mockito version 1.10.19
* How to run tests
   - src/test/java
   - Also tests run when you do 'test' or 'package' stage in maven lifecycle 

### Contribution guidelines ###

* Writing tests
* Code review
* Other guidelines

### Who do I talk to? ###

* Repo owner or admin
* Other community or team contact
