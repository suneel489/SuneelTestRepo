package com.qa.base;

import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;
import java.io.FileReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.PropertyConfigurator;

import com.qa.utilities.ScreenRecorderUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.*;

import org.testng.annotations.*;
import com.qa.utilities.ExtentManager;
import com.qa.utilities.Utilities;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Attachments;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.testng.asserts.SoftAssert;
import static com.qa.utilities.Screenshot.screenshot;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.interactions.Actions;
import java.net.URL;

public class Baseclass {

    public static WebDriver driver;
    public static Properties prop;
    public static ExtentTest test;
    public static ExtentReports rep ;
    public static Logger log = Logger.getLogger(Baseclass.class);
    /*public static Calendar calendar = Calendar.getInstance();
    public static Date time = calendar.getTime();*/
    public static String MethodName;
    public static String methodName;
    public String SuiteFolder = System.getProperty("user.dir");
    public String Home = System.getProperty("user.home");
    public String downloadDirectory = Home + File.separator + "Downloads";
    public static SoftAssert softAssert = new SoftAssert();
    String url = Utilities.Getdata("TestData", "URL");
    public static String browserName = "chrome";
    public static String[] Email = new String[16];



    public Baseclass() {

        prop = new Properties();
        FileInputStream fs;
        PropertyConfigurator.configure("log4j.properties");

        try {

            FileReader reader = new FileReader("Config.properties");
            prop.load(reader);

        } catch (Exception e) {

            // TODO Auto-generated catch block

        }
    }

    @BeforeSuite
    public void Beforesuite() throws InterruptedException, IOException,UnknownHostException {

        System.out.println("Before Suite:-");
        System.out.println("Start Automation Execution");
        rep = ExtentManager.getInstance();
        MultipelBrowserInitialization(url);
    }

    @BeforeTest
    public void Test() {
        System.out.println("Before Test:-");
        
    }

   

    public WebDriver BrowserInitializationGrid(String URL) throws InterruptedException {
        log.info("Browser Initialization Grid with : " + URL);
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("incognito");
        options.addArguments("--window-size=1280,672");
        URL gridUrl;
        try {
            gridUrl = new URL("http://localhost:4444/");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        driver = new RemoteWebDriver(gridUrl, options);
        driver.get(URL);
        driver.manage().deleteAllCookies();
        log.info("open : " + URL);
        return driver;
    }

    public void MultipelBrowserInitialization(String URL) throws InterruptedException {
        log.info("Multiple Browser Initialization with : " + URL);
        switch (browserName) {

            case "chrome":

                // Set path for download
                String downloadPath = System.getProperty("user.home") + "\\Downloads";

                // Set Chrome preferences
                Map<String, Object> prefs = new HashMap<>();
                prefs.put("download.default_directory", downloadPath);
                prefs.put("download.prompt_for_download", false);
                prefs.put("download.directory_upgrade", true);
                prefs.put("safebrowsing.enabled", true);

                ChromeOptions options = new ChromeOptions();
                 options.addArguments("--remote-allow-origins=*");
                 options.addArguments("user-data-dir=C:\\environments\\selenium");
                  //options.setBinary("C:\\Users\\suneel.dharamavarapu\\Downloads\\chrome-win64\\chrome-win64\\chrome.exe");
                //options.addArguments("--headless=new");
                options.addArguments("incognito");
                options.addArguments("--disable-extensions");
                /* options.addArguments("disable-infobars"); */
                options.setExperimentalOption("useAutomationExtension", false);
                options.setExperimentalOption("excludeSwitches", List.of("enable-automation"));
                //options.addArguments("--window-size=1280,672");


                driver = new ChromeDriver(options);
                // Enable download in headless using CDP
                Map<String, Object> commandParams = new HashMap<>();
                commandParams.put("behavior", "allow");
                commandParams.put("downloadPath", downloadPath);
                ((ChromeDriver) driver).executeCdpCommand("Page.setDownloadBehavior", commandParams);



                break;

            case "edge":

                EdgeOptions edgeopt = new EdgeOptions();
                //edgeopt.addArguments("--headless");
                edgeopt.addArguments("--inprivate");
                edgeopt.addArguments("disable-infobars");
                edgeopt.setExperimentalOption("useAutomationExtension", false);
                edgeopt.setExperimentalOption("excludeSwitches", List.of("enable-automation"));
                driver = new EdgeDriver(edgeopt);

                break;

            case "safari":

                driver = new SafariDriver();

                break;

            case "firefox":

                driver = new FirefoxDriver(new FirefoxOptions());

                break;

            case "ie":

                driver = new InternetExplorerDriver(new InternetExplorerOptions());

                break;

            default:

                System.out.println("Invalid browser name: " + browserName);

                return;

        }

        driver.get(URL);


        // Maximize the window

        driver.manage().window().maximize();

        // Delete all cookies

        driver.manage().deleteAllCookies();
        log.info("open : " + URL);
        if (test != null) {
            test.log(LogStatus.INFO, "You are Logged in  :-" + URL + " ||Env");
        }
        Capabilities caps = ((RemoteWebDriver) driver).getCapabilities();
        String browserName = caps.getBrowserName();
        String browserVersion = caps.getBrowserVersion();
        log.info("chrome : " +browserName +" and  "+ browserVersion);

    }

    public static void click(WebElement element, String stepName) throws IOException {
        // test.log(LogStatus.PASS,stepname+test.addBase64ScreenShot(Utilities.addScreenshot()));
        try {
            // FluentWait - retries every 500ms up to 20s
            Wait<WebDriver> wait = new FluentWait<>(driver)
                    .withTimeout(Duration.ofSeconds(15))
                    .pollingEvery(Duration.ofMillis(500))
                    .ignoring(StaleElementReferenceException.class);

            /*// ✅ Wait until page is fully loaded
            wait.until(d -> ((JavascriptExecutor) d)
                    .executeScript("return document.readyState").equals("complete"));
*/
            // ✅ Ensure element is visible and clickable
            scrollIntoView(element);
            wait.until(ExpectedConditions.visibilityOf(element));
            wait.until(ExpectedConditions.elementToBeClickable(element));

            // 📸 Capture screenshot before click
            // Highlight the element with a red border
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].style.border='2px solid red'", element);
            Utilities.captureScreenshotForExtentReport();
            // Clear the red border from the previous element
            js.executeScript("arguments[0].style.border=''", element);
            try {
                element.click(); // normal click
            } catch (ElementClickInterceptedException e) {
                // Retry once if intercepted
                log.info("Click intercepted → retrying for: " + stepName);
                scrollIntoView(element);
                wait.until(ExpectedConditions.elementToBeClickable(element));
                Thread.sleep(500);
                element.click();
            }
            // ✅ Log success after click
            test.log(LogStatus.PASS,   stepName + test.addScreenCapture(GetrelativePath()));
            log.info("Clicked successfully: " + stepName);

        }/**/ catch (Exception e) {
            // ❌ Log failure
            test.log(LogStatus.FAIL, "Failed to click  : " + stepName, e);
            log.info("Failed to click: " + stepName, e);
            //throw new IOException("Element not clickable: " + stepName, e);
        }
    }

    public static void type(WebElement element, String value, String stepName) throws IOException {
        // test.log(LogStatus.PASS,stepname+test.addBase64ScreenShot(Utilities.addScreenshot()));

        try {
            // FluentWait - retries every 500ms up to 20s
            Wait<WebDriver> wait = new FluentWait<>(driver)
                    .withTimeout(Duration.ofSeconds(15))
                    .pollingEvery(Duration.ofMillis(500))
                    .ignoring(StaleElementReferenceException.class);
/*

            // ✅ Wait until page is fully loaded
            wait.until(d -> ((JavascriptExecutor) d)
                    .executeScript("return document.readyState").equals("complete"));
*/

            // ✅ Ensure element is visible and clickable
            scrollIntoView(element);
            wait.until(ExpectedConditions.visibilityOf(element));
            wait.until(ExpectedConditions.elementToBeClickable(element));

            // 📸 Capture screenshot before click
            // Highlight the element with a red border
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].style.border='2px solid red'", element);
            Utilities.captureScreenshotForExtentReport();
            // Clear the red border from the previous element
            js.executeScript("arguments[0].style.border=''", element);
            element.clear();
            element.sendKeys(value);

            // ✅ Log success after click
            test.log(LogStatus.PASS,   stepName + test.addScreenCapture(GetrelativePath()));
            log.info("Clicked successfully: " + stepName);

        } catch (Exception e) {
            // ❌ Log failure
            test.log(LogStatus.FAIL, "Failed to click and Type : " + stepName, e);
            log.info("Failed to click and Type : " + stepName, e);
            //throw new IOException("Element not clickable and not able to Type: " + stepName, e);
        }
    }

    public static WebElement GetWebElement(WebElement element, String stepname) throws IOException {
        WebElement webElement = element;
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOf(webElement));
            JavascriptExecutor js = (JavascriptExecutor) driver;

            if (element.isDisplayed()) {

                // test.log(LogStatus.PASS,stepname+test.addBase64ScreenShot(Utilities.addScreenshot()));
				/*if (executionMode.equals("Pipeline")) {
					test.log(LogStatus.PASS, stepname);
					log.info(stepname);
				} else*/ {
                    // Highlight the element with a red border
                    //js.executeScript("arguments[0].style.border='2px solid red'", element);
                    Utilities.captureScreenshotForExtentReport();
                    //String imagepath = "ExtentScreenShots" + File.separator + Utilities.currentdate() + File.separator + Utilities.screenshotName;
                    test.log(LogStatus.PASS, stepname + test.addScreenCapture(GetrelativePath()));
                    log.info(stepname);
                    // Clear the red border from the previous element
                    //js.executeScript("arguments[0].style.border=''", element);
                    Thread.sleep(1000);
                }
            }
        } catch (NoSuchElementException | ElementClickInterceptedException | InterruptedException e) {
            test.log(LogStatus.FAIL, stepname);
            log.info(e.getMessage());

        }
        return webElement;
    }

    public static WebElement GetWebElementPopUp(WebElement element, String stepname) throws IOException {
        WebElement webElement = element;
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(25));
            wait.until(ExpectedConditions.visibilityOf(webElement));
            JavascriptExecutor js = (JavascriptExecutor) driver;
            if (element.isDisplayed()) {

                // test.log(LogStatus.PASS,stepname+test.addBase64ScreenShot(Utilities.addScreenshot()));
				/*if (executionMode.equals("Pipeline")) {
					test.log(LogStatus.PASS, stepname);
					log.info(stepname);

				} else*/ {
                    // Highlight the element with a red border
                    //js.executeScript("arguments[0].style.border='2px solid red'", element);
                    Utilities.captureScreenshotForExtentReport();
					/*String imagepath = "ExtentScreenShots" + File.separator + Utilities.currentdate() + File.separator + Utilities.screenshotName;
					String htmlPath = imagepath.replace(File.separatorChar, '/');*/
                    test.log(LogStatus.PASS, stepname + test.addScreenCapture(GetrelativePath()));
                    log.info(stepname);
                    // Clear the red border from the previous element
                    //js.executeScript("arguments[0].style.border=''", element);
                    Thread.sleep(500);
                }

            }
        } catch (NoSuchElementException | ElementClickInterceptedException | InterruptedException e) {
            test.log(LogStatus.FAIL, stepname);
            log.info(e.getMessage());

        }
        return webElement;
    }


    public static void Rightclick(WebElement element, String stepname) throws IOException {
        try {

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOf(element));
            wait.until(ExpectedConditions.elementToBeClickable(element));
            Thread.sleep(1500);
            if (element.isDisplayed()) {

                Actions actions = new Actions(driver);
                actions.contextClick(element).build().perform();
                test.log(LogStatus.PASS, stepname);
                log.info(stepname);

            }
        } catch (Exception e) {
            test.log(LogStatus.FAIL, stepname + e);
            log.info(e.getMessage());
        }
    }

    public static void scrollIntoView(WebElement element) {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView({ behavior: 'auto', block: 'center' });", element);

    }

    public static void uploadFile(String fileLocation) {
        try {

            setClipboardData(fileLocation);
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_V);
            robot.keyRelease(KeyEvent.VK_V);
            Thread.sleep(3000);
            robot.keyRelease(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
    }



    /**
     * Reusable method to press any key using Robot class with delay
     * @param keyEvent - KeyEvent constant (e.g., KeyEvent.VK_ENTER, KeyEvent.VK_TAB)
     * @param stepName - Description for logging
     * @param delayMs - Delay in milliseconds after key press (optional, default 0)
     */
    public static void pressKey(int keyEvent, String stepName, int delayMs) {
        try {
            Robot robot = new Robot();
            robot.keyPress(keyEvent);
            robot.keyRelease(keyEvent);
            if (delayMs > 0) {
                Thread.sleep(delayMs);
            }
            test.log(LogStatus.PASS, stepName);
            log.info(stepName);
        } catch (Exception exp) {
            test.log(LogStatus.FAIL, stepName + " - Failed to press key: " + exp.getMessage());
            log.error("Failed to press key: " + exp.getMessage());
            exp.printStackTrace();
        }
    }

    /**
     * Overloaded method to press any key without delay
     * @param keyEvent - KeyEvent constant (e.g., KeyEvent.VK_ENTER, KeyEvent.VK_TAB)
     * @param stepName - Description for logging
     */
    public static void pressKey(int keyEvent, String stepName) {
        pressKey(keyEvent, stepName, 0);
    }

    public static void downloadFileSave() {

        if (browserName.equals("chrome")) {
            try {
                Thread.sleep(2000);
				/*Robot robot = new Robot();

				robot.keyPress(KeyEvent.VK_ENTER);
				robot.keyRelease(KeyEvent.VK_ENTER);*/

            } catch (Exception exp) {
                exp.printStackTrace();
            }
        } else {

            System.out.println("Download Successfully in Edge");
        }
    }

    public static void setClipboardData(String string) throws IOException {

        StringSelection stringSelection = new StringSelection(string);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
    }

    public void explicitWait(WebDriver driver, WebElement element, int i) throws IOException {

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(i));
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    public void explicitWaitclickable(WebDriver driver, WebElement element, int i) throws IOException {

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(i));
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    public static void explicitWaitInvisible(WebDriver driver, WebElement element, int timeOut) throws IOException {

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
        wait.until(ExpectedConditions.invisibilityOf(element));
    }

    public void pageLoadTimeOut(WebDriver driver, int timeOut) throws IOException {
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(timeOut));
    }

    public void scrollByVisibilityOfElement(WebDriver driver, WebElement ele) throws IOException {

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView();", ele);

    }

    public void scrollByPixel(WebDriver driver, Integer x, Integer y) throws IOException {

        JavascriptExecutor js = (JavascriptExecutor) driver;
        String x_string = x.toString();
        String y_string = y.toString();
        js.executeScript("javascript:window.scrollBy(" + x_string + "," + y_string + ")");

    }

    public void CloseDownloadBar() throws Exception {

        if (browserName.equals("chrome")) {
            Robot bot = new Robot();
            Thread.sleep(1000);
            bot.keyPress(KeyEvent.VK_CONTROL);
            bot.keyPress(KeyEvent.VK_J);
            bot.keyRelease(KeyEvent.VK_CONTROL);
            bot.keyRelease(KeyEvent.VK_J);
            Thread.sleep(1500);
            ArrayList<String> tabs2 = new ArrayList<String>(driver.getWindowHandles());
            driver.switchTo().window(tabs2.get(1));
            Thread.sleep(3000);
            driver.close();
            driver.switchTo().window(tabs2.get(0));
            Thread.sleep(3000);
        } else {

            System.out.println("execution in edge");
        }
    }

    public void closeDownloadBar() throws Exception {

        Robot bot = new Robot();
        Thread.sleep(1000);
        bot.keyPress(KeyEvent.VK_CONTROL);
        bot.keyPress(KeyEvent.VK_J);
        bot.keyRelease(KeyEvent.VK_CONTROL);
        bot.keyRelease(KeyEvent.VK_J);

        String winHandleBefore = driver.getWindowHandle();
        for (String winHandle : driver.getWindowHandles()) {
            driver.switchTo().window(winHandle);

        }
        Thread.sleep(1000);
        // driver.close();
        driver.switchTo().window(winHandleBefore);

    }

    public static void ClickEnterKey(WebElement element) throws IOException {
        element.sendKeys(Keys.ENTER);
    }

    public static void ClickEscapeKey(WebElement element) throws IOException {
        element.sendKeys(Keys.ESCAPE);
    }

    public static void select1(WebElement element, String value) throws IOException {
        Select drpCountry = new Select(element);
        drpCountry.selectByVisibleText(value);
        screenshot("abc", MethodName);
    }

    public static void clear(WebElement element) throws IOException {

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOf(element));
        wait.until(ExpectedConditions.elementToBeClickable(element));
        try {
            if (browserName.equals("safari")) {
                element.sendKeys(Keys.COMMAND, "a");
                element.sendKeys(Keys.DELETE);
                JavascriptExecutor js = (JavascriptExecutor) driver;
                js.executeScript("arguments[0].value ='';", element);
            } else {
                element.sendKeys(Keys.CONTROL, "a");
                element.sendKeys(Keys.DELETE);

            }
            test.log(LogStatus.PASS,"Clear the text box");
            Thread.sleep(1000);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static String getRandomString(int n) {

        // length is bounded by 256 Character
        byte[] array = new byte[256];
        new Random().nextBytes(array);

        String randomString = new String(array, Charset.forName("UTF-8"));

        // Create a StringBuffer to store the result
        StringBuffer r = new StringBuffer();
        // remove all spacial char
        String AlphaNumericString = randomString.replaceAll("[^A-Za-z]", "");

        // Append first 20 alphanumeric characters from the generated random String into
        // the result 0-9
        for (int k = 0; k < AlphaNumericString.length(); k++) {

            if (Character.isLetter(AlphaNumericString.charAt(k)) && (n > 0)
                    || Character.isDigit(AlphaNumericString.charAt(k)) && (n > 0)) {
                r.append(AlphaNumericString.charAt(k));
                n--;
            }
        }
        // return the resultant string
        return r.toString();
    }

    public static WebElement VerifyNumeric(WebElement element, String stepname) throws IOException {

        WebElement webElement = element;
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOf(webElement));
        test.log(LogStatus.INFO, stepname);
        return webElement;
    }

    public boolean containsValue(XSSFRow row, int fcell, int lcell) {
        boolean flag = false;
        for (int i = fcell; i < lcell; i++) {
            if (StringUtils.isEmpty(String.valueOf(row.getCell(i))) == true
                    || StringUtils.isWhitespace(String.valueOf(row.getCell(i))) == true
                    || StringUtils.isBlank(String.valueOf(row.getCell(i))) == true
                    || String.valueOf(row.getCell(i)).length() == 0 || row.getCell(i) == null) {
            } else {
                flag = true;
            }
        }
        return flag;

    }

    public void WriteExcelData(String Sheetname, String path) throws IOException {
        String value1 = "100";
        String value2 = "100";
        System.out.println("path..........." + path + ".........Sheetname" + Sheetname);
        FileInputStream fs = new FileInputStream(path);
        FileOutputStream outputStream = null;
        XSSFWorkbook workbook = new XSSFWorkbook(fs);
        XSSFSheet worksheet = workbook.getSheet(Sheetname);
        Iterator<Row> iterator = worksheet.iterator();

        int fcell = worksheet.getFirstRowNum();
        int lcell = worksheet.getLastRowNum();
        int tcolomns = 0;
        int trows = 0;
        XSSFRow row = null;

        while (iterator.hasNext()) {
            row = (XSSFRow) iterator.next();// increment the row iterator
            if (trows == 0) {
                tcolomns = row.getPhysicalNumberOfCells();
            }

            if (containsValue(row, fcell, lcell) == true) {
                trows++;
                System.out.println("Row data....");
            }
        }

        Cell cell = null;

        worksheet.getRow(1).createCell(8).setCellValue(value1);
        worksheet.getRow(2).createCell(8).setCellValue(value2);

        outputStream = new FileOutputStream(path);
        workbook.write(outputStream);
    }

    public static void enterOnlyLogs(String stepname) throws IOException {
        try {

            Thread.sleep(500);
            test.log(LogStatus.PASS, stepname);
            log.info(stepname);
        } catch (Exception e) {
            test.log(LogStatus.FAIL, stepname + e);
            log.info(e.getMessage());
        }
    }

    @AfterTest
    public void CloseDriver() throws IOException {

        try {

            System.out.println("Automation Execution Completed");
            //driver.close();
            Thread.sleep(3000);
            //driver.quit();
            Thread.sleep(3000);
        } catch (Exception e) {

            e.printStackTrace();
        }
    }


    @AfterSuite
    public void AfterSuite() throws IOException {

        System.out.println("STOP Automation Execution");
        try {

            /*driver.close();
            Thread.sleep(3000);
            driver.quit();
            Thread.sleep(3000);*/
        } catch (Exception e) {

            e.printStackTrace();
        }
        log.info("Driver Closed and email will be sent");

    }


    

    public static void Mouseover(WebElement element, String stepname) throws IOException {
        try {

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            wait.until(ExpectedConditions.visibilityOf(element));
            Thread.sleep(500);
            JavascriptExecutor js = (JavascriptExecutor) driver;
            if (element.isDisplayed()) {

                Actions actions = new Actions(driver);
                actions.moveToElement(element).build().perform();
                Thread.sleep(1000);
                Utilities.captureScreenshotForExtentReport();
				/*String imagepath = System.getProperty("user.dir") + File.separator + "ExtentScreenShots"
						+ File.separator + Utilities.currentdate() + File.separator + Utilities.screenshotName;
			*/	test.log(LogStatus.PASS, stepname + test.addScreenCapture(GetrelativePath()));
                log.info(stepname);
                Thread.sleep(1000);

            }
        } catch (Exception e) {
            test.log(LogStatus.FAIL, stepname + e);
            log.info(e.getMessage());
        }
    }

    public boolean Link_Enabled(WebElement webelement) {
        String enable = webelement.getDomAttribute("aria-disabled");
        System.out.println(webelement + "link is " + enable);
        return !enable.equalsIgnoreCase("true");

    }

    public static void HandleContinuePopup() {

        Actions actions = new Actions(driver);
        actions.sendKeys(Keys.ESCAPE).build().perform();

        try {
            Thread.sleep(2000);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(6));
            WebElement continueButton = driver.findElement(By.xpath("(//*[text()='Confirm Navigation']/following::button[text()='Continue'])[1]"));

            if (continueButton.isDisplayed()) {
                Thread.sleep(2000);
                continueButton.click();
                test.log(LogStatus.INFO, "Handle Continue Popup Confirmation");
                Thread.sleep(2000);
            }
        } catch (Exception e) {

        }

    }


    public void ValidateUniqueURL() {

        String currentURL = driver.getCurrentUrl();

        // Regex Patterns
        //Pattern patternWithNumberAndSegment = Pattern.compile("^(https?://[^/]+)/(\\d+)/([^/]+)$");
        Pattern patternWithNumberOnly = Pattern.compile("(.?)/(\\d+)(/.)?");
        Pattern patternWithSegmentOnly = Pattern.compile("^(https?://[^/]+)/(?!\\d+)([^/]+)(/.+)$");
        Pattern patternWithNumberAndSegment = Pattern.compile("^(https?://[^/]+)/(\\d+)(/.+)$");

        Matcher matcherWithNumberAndSegment = patternWithNumberAndSegment.matcher(currentURL);
        Matcher matcherWithNumberOnly = patternWithNumberOnly.matcher(currentURL);
        Matcher matcherWithSegmentOnly = patternWithSegmentOnly.matcher(currentURL);

        String modifiedURL = "";

        // Condition 1: Number & Middle Segment Available
        if (matcherWithNumberAndSegment.find()) {
            //String baseUrl = matcherWithNumberAndSegment.group(1);
            String baseUrl = currentURL.split("\\.com")[0] + ".com"; // Base URL
            String numberSegment = matcherWithNumberAndSegment.group(2);  // The numeric segment
            String pathAfterSegment = matcherWithNumberAndSegment.group(3);  // Everything after the numeric segment

            // Modify the URL by inserting a random string in the middle of the path
            modifiedURL = baseUrl + "/" + numberSegment + "/" + getRandomString(5) + pathAfterSegment;
            System.out.println("Navigate To (number and segment): " + modifiedURL);
            driver.get(modifiedURL);
        }
        // Condition 2: Only Number Available
        else if (matcherWithNumberOnly.find()) {
            String baseURL = currentURL.split("\\.com")[0] + ".com";   // Base URL
            String number = matcherWithNumberOnly.group(2);   // Number
            // Construct new URL
            modifiedURL = baseURL + "/" + number + "/" + getRandomString(5);
            System.out.println("Navigate To  : " + modifiedURL);
            driver.get(modifiedURL);
        }
        // Condition 3: No Number in URL and middle segment
        else if (matcherWithSegmentOnly.find()) {

            String baseURL = currentURL.split("\\.com")[0] + ".com"; // Base URL
            String segmentPath = matcherWithSegmentOnly.group(2);  // The first segment (non-numeric)
            String pathAfterSegment = matcherWithSegmentOnly.group(3);  // Everything after the segment

            // Modify the URL by inserting a random string in the middle of the path
            modifiedURL = baseURL + "/" + segmentPath + "/" + getRandomString(5) + pathAfterSegment;
            System.out.println("Navigate To (segment only): " + modifiedURL);
            driver.get(modifiedURL);
        } else {
            String baseURL = currentURL.split("\\.com")[0] + ".com";   // Base URL
            // Construct new URL
            modifiedURL = baseURL + "/" + getRandomString(5);
            System.out.println("Navigate To  : " + modifiedURL);
            driver.get(modifiedURL);
        }


        // Navigate to the modified URL

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            WebElement locator = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//*[contains(text(),\"The URL you've entered appears \")]")));
            Utilities.captureScreenshotForExtentReport();
			/*String imagepath = System.getProperty("user.dir") + File.separator + "ExtentScreenShots"
					+ File.separator + Utilities.currentdate() + File.separator + Utilities.screenshotName;*/
            test.log(LogStatus.PASS, modifiedURL + "  : is " + locator.getText() + test.addScreenCapture(GetrelativePath()));
            log.info(locator.getText());

        } catch (NoSuchElementException e) {
            WebElement locator = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//*[contains(text(),\"We couldn't find the requested \")]")));
            test.log(LogStatus.PASS, modifiedURL + "  : is " + locator.getText());

        } catch (Exception e) {

            test.log(LogStatus.FAIL, e);
        }
    }

    public void ValidateTrainingLink() throws InterruptedException, IOException {


        String orginalTab = driver.getWindowHandle();

        WebElement traininglink = driver.findElement(By.xpath("(//*[contains(text(),'Training')])[1]"));

        int attempts = 6;

        for (int i = 0; i < attempts; i++) {
            try {
                // Scroll the element into view
                Thread.sleep(2000);
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", traininglink);
                Thread.sleep(2000);
                // Try clicking the element
                traininglink.click();
                System.out.println("Element clicked!");
                Set<String> tabs = driver.getWindowHandles();
                for (String tab : tabs) {
                    if (!tab.equals(orginalTab)) {
                        driver.switchTo().window(tab);
                        break;
                    }
                }
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));
                WebElement locator = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@name=\"username\"]")));
                locator.isDisplayed();
                Utilities.captureScreenshotForExtentReport();
                //	String imagepath = System.getProperty("user.dir") + File.separator + "ExtentScreenShots" + File.separator + Utilities.currentdate() + File.separator + Utilities.screenshotName;
                test.log(LogStatus.PASS, "User Click on Training Link and Navigate Training tab" + test.addScreenCapture(GetrelativePath()));
                driver.close();
                driver.switchTo().window(orginalTab);
                return; // Exit if the element is successfully clicked

            } catch (ElementClickInterceptedException | StaleElementReferenceException e) {
                // If the element is not clickable, scroll and retry
                System.out.println("Element not clickable yet, scrolling again...");
            }

            // Scroll down by half the window height if element isn't clickable
            //((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");


        }
    }

    public static ThreadLocal<String> testname = new ThreadLocal<>();
    @BeforeMethod(alwaysRun=true)
    public void beforeTC(java.lang.reflect.Method m){
        testname.set(m.getName());

    }


    public static String GetrelativePath() throws IOException {
        String dateFolder = Utilities.currentdate();
        String relativePath = ".." + File.separator + ".." + File.separator + "ExtentScreenShots"
                + File.separator + dateFolder + File.separator + Utilities.screenshotName;
        String htmlPath = relativePath.replace(File.separatorChar, '/');
        return htmlPath;
    }


    public void ValidateURLlowercase() {

        String currentUrl = driver.getCurrentUrl();
        try {

            if (currentUrl != null) {

                // 1. Check if it's all lowercase
                softAssert.assertEquals(currentUrl, currentUrl.toLowerCase(),
                        "Current URL is not lowercase: " + currentUrl);
                test.log(LogStatus.INFO, "Current URL is in lowercase : " + currentUrl);
            } else {
                softAssert.fail("Current URL is null");
            }
        }
        catch (Exception e) {
//
            test.log(LogStatus.FAIL, "Current URL is in not lowercase : " + currentUrl);
        }

    }



    // Reusable method to close bubble using the locator for bubble-close
    public void closeBubble() {
        try {
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
            WebElement bubbleClose = driver.findElement(By.xpath("//*[@class='bubble-close']"));
            if (bubbleClose.isDisplayed()) {
                Thread.sleep(2000);
                JavascriptExecutor js = (JavascriptExecutor) driver;
                js.executeScript("arguments[0].click();", bubbleClose);
                test.log(LogStatus.INFO, "Bubble closed successfully.");
                log.info("Bubble closed successfully.");
                Thread.sleep(2000);
            }
        } catch (NoSuchElementException e) {
            log.info("Bubble close element not found, nothing to close.");

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

