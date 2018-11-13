# Artisan Bootcamp Project

## Required Dependencies
* Docker
* Java 1.8+

## Building and Running the Project
* Build full project with all tests `./gradlew build`
* Run only unit tests `./gradlew test`
* Run only integration tests (will automatically start/stop resources) `./gradlew integrationTest`
* Start resources for manual test/debug of application code from IDE `./gradlew integrationTestSetup`
* Stop resources for integration testing  `./gradlew integrationTestTeardown`
* Run only cucumber tests (will automatically start/stop resources) `./gradlew cucumberTest`
* Start resources for manually running acceptance tests from IDE `./gradlew cucumberTestSetup`
* Stop acceptance test resources `./gradlew cucumberTestTeardown`
