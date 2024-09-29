This repository contains a Test Automation Framework built using Java, Selenium, and Rest Assured. The framework is designed to automate testing for both Web Applications and REST APIs. It is integrated with CI/CD pipelines to ensure automated execution in different environments.

Table of Contents
Features
Technology Stack
Prerequisites
Installation
Project Structure
How to Run Tests
Test Types
API Tests
UI Tests
Test Data Management
Logging and Reporting
Continuous Integration / Continuous Deployment
Best Practices
Contributing
Features
Java-based Test Automation framework.
API Testing with Rest Assured.
Web UI Testing using Selenium WebDriver.
Integration with CI/CD pipelines (e.g., Jenkins, GitHub Actions).
Supports parallel execution for faster test runs.
Generates detailed HTML reports using Extent Reports.
Custom Logging for both API and UI test scenarios.
Cross-browser and cross-platform support for Web UI tests.
Technology Stack
Programming Language: Java (JDK 8 or above)
Web Automation: Selenium WebDriver
API Testing: Rest Assured
Build Tool: Maven/Gradle
Test Runner: TestNG/JUnit
Reporting: Extent Reports / Allure
Logging: Log4j / SLF4J
CI/CD Tools: Jenkins, GitHub Actions
Prerequisites
Ensure you have the following installed on your system:

Java Development Kit (JDK) - Download Here
Maven/Gradle - Download Maven / Download Gradle
Git - Download Git
WebDriver dependencies (ChromeDriver, GeckoDriver, etc.)
Additionally, configure the following for CI/CD:

Jenkins or GitHub Actions setup
Docker (if running tests in containers)
Installation
Clone the repository and install dependencies.

bash
Copy code
# Clone the repository
git clone https://github.com/your-repository/test-automation-framework.git

# Navigate to the project directory
cd test-automation-framework

# Build the project (for Maven)
mvn clean install

# OR Build the project (for Gradle)
gradle build
Project Structure
bash
Copy code
.
├── src
│   ├── main
│   │   └── java                 # Main Java Source Code
│   └── test
│       ├── java                 # Test Scripts
│       ├── resources            # Test Data, Configurations
│       └── reports              # Generated Test Reports
├── pom.xml / build.gradle        # Dependency Management
├── README.md                     # Project Documentation
├── Jenkinsfile                   # CI/CD Pipeline Configuration
└── .github/workflows             # GitHub Actions Configuration
How to Run Tests
Run all tests
bash
Copy code
# Using Maven
mvn test

# Using Gradle
gradle test
Run specific tests
To run a particular test suite or test class:

bash
Copy code
mvn -Dtest=TestClassName test
Configure the browser (for UI Tests)
By default, tests run in Chrome. You can specify a different browser by setting a system property.

bash
Copy code
mvn test -Dbrowser=firefox
Test Types
API Tests
API tests are written using Rest Assured to validate RESTful services.

Base URI and authentication tokens are configurable via property files.
All API test endpoints are stored in JSON format for easy reference.
Handles both positive and negative test cases for HTTP methods such as GET, POST, PUT, DELETE.
Sample API Test
java
Copy code
@Test
public void testGetUserDetails() {
    given()
        .header("Authorization", "Bearer token")
    .when()
        .get("/users/{id}", 1)
    .then()
        .statusCode(200)
        .body("username", equalTo("john_doe"));
}
UI Tests
UI Tests are performed using Selenium WebDriver to simulate user actions on the web application.

Page Object Model (POM) is used for better code structure.
Supports multiple browsers: Chrome, Firefox, Edge.
UI tests are configured for parallel execution to speed up testing.
Sample UI Test
java
Copy code
@Test
public void testLogin() {
    LoginPage loginPage = new LoginPage(driver);
    loginPage.enterUsername("user")
             .enterPassword("password")
             .clickLoginButton();
    
    Assert.assertTrue(homePage.isUserLoggedIn());
}
Test Data Management
Test data is managed using JSON or Excel files placed under the src/test/resources directory.

You can configure:

API request/response payloads
UI test inputs (e.g., login credentials, form data)
Logging and Reporting
Log4j is used for logging test execution details to console and log files.
Extent Reports are generated after test execution to provide detailed HTML reports with screenshots and test step logs.
Sample Extent Report:
Includes clickable buttons to expand/collapse test logs.
Highlights failed steps with error messages and screenshots.
Continuous Integration / Continuous Deployment (CI/CD)
Jenkins
The framework integrates with Jenkins for continuous testing. The Jenkinsfile defines the pipeline stages:

Build the project
Run the tests
Generate the reports
Publish the results
GitHub Actions
GitHub Actions workflows are available in .github/workflows/. The pipeline automatically runs tests on every push or pull request.
