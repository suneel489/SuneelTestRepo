# AGENTS.md

## Project Overview
This is a Selenium WebDriver automation framework using TestNG for testing the SwagLabs e-commerce application. It follows Page Object Model (POM) pattern with ExtentReports for detailed test reporting and screenshot capture.

## Architecture
- **Baseclass** (`src/main/java/com/qa/base/Baseclass.java`): Core setup class handling WebDriver initialization, ExtentReports configuration, logging, and test lifecycle management.
- **Page Object Model**: Actions and Locators separated in `com.qa.pom` package, using Selenium PageFactory with AjaxElementLocatorFactory for robust element loading.
- **Utilities** (`com.qa.utilities`): Common functions for Excel data reading, screenshot capture, date formatting, and screen recording.
- **CustomListeners** (`com.ExtentReportListener.CustomListeners.java`): TestNG listener implementing ITestListener and ISuiteListener for automated reporting and screenshot attachment.

## Key Patterns
- **Fluent POM**: Action methods return `this` for method chaining (e.g., `new LoginPageActions().Username("user").Password("pass").clicklogin()`).
- **Data-Driven Testing**: Test data read from `src/test/resources/test.xlsx` using Apache POI, accessed via `Utilities.Getdata(sheetName, key)`.
- **Screenshot on Events**: Automatic screenshot capture on test pass/fail/skip, stored in `Report/ExtentScreenShots/YYYY-MM-DD/` with timestamped filenames.
- **ExtentReports Integration**: Uses legacy ExtentReports (v2.41.2) with custom report paths in `Report/ExtentReport/YYYY-MM-DD/TestReport_DDMMMYYYY_HH_mm_[am/pm].html`.

## Developer Workflows
- **Build Project**: `mvn clean compile` - Compiles Java 17 code.
- **Run Tests**: `mvn test` - Executes TestNG suite from `testng.xml`, runs in parallel if configured.
- **View Reports**: Open HTML reports in `Report/ExtentReport/` directory; TestNG reports in `test-output/`.
- **Debug Tests**: Check console logs and `logs/test.log` for Log4j output; screenshots attached to ExtentReports.
- **Add New Test**: Create class in `com.qa.ATC` extending Baseclass, use POM actions, annotate with `@Test(groups = {"Regression"})`.

## Conventions
- **Browser Default**: Chrome via WebDriverManager; configurable via `browserName` in Baseclass.
- **Implicit Wait**: 10 seconds set in test methods.
- **Test Groups**: Use "Regression" and "Sanity" for suite organization.
- **Element Locators**: Prefer `data-test` attributes in XPath (e.g., `//*[@data-test='username']`).
- **File Paths**: Use `System.getProperty("user.dir")` + `File.separator` for cross-platform compatibility.
- **Logging**: Log4j configured in `log4j.properties` for console and rolling file output.

## Dependencies & Integrations
- **WebDriver**: Selenium 4.38.0 with WebDriverManager 5.3.2 for automatic driver management.
- **Reporting**: ExtentReports for HTML reports with screenshots; TestNG for XML/JSON reports.
- **Data Handling**: Apache POI for Excel operations.
- **Screenshots**: Selenium WebDriver + FileUtils for capture; AShot for advanced screenshotting.
- **Email**: SendGrid integration in Baseclass for report emailing (requires API key setup).</content>
<parameter name="filePath">C:\Users\dsune\git\SuneelTestRepo\AGENTS.md
