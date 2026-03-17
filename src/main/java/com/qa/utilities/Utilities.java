package com.qa.utilities;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.List;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import com.relevantcodes.extentreports.LogStatus;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import com.qa.base.Baseclass;
import org.testng.asserts.SoftAssert;
import org.apache.commons.codec.binary.Base64;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.MemoryCacheImageOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import org.monte.media.Format;
import org.monte.media.FormatKeys.MediaType;
import org.monte.media.Registry;
import org.monte.media.math.Rational;
import org.monte.screenrecorder.ScreenRecorder;

import static org.monte.media.AudioFormatKeys.*;
import static org.monte.media.VideoFormatKeys.*;
import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Image.*;

public class Utilities extends Baseclass {

    public static String screenshotPath;
    public static String screenshotName;
    public static String path;
    public static String methodName;

    public static ScreenRecorder screenRecorder;
    public static int min = 200;
    public static int max = 400;
    public static int i = (int) (Math.random() * (max - min + 1) + min);
    public static Calendar calendar = Calendar.getInstance();
    public static int hour = calendar.get(Calendar.HOUR_OF_DAY);
    public static int minute = calendar.get(Calendar.MINUTE);
    public static String strBrowser = System.getProperty("user.dir");
    static FileInputStream Tfis;
    public static XSSFWorkbook wb;

    // Helper method to get available sheet names
    private static String getAvailableSheets(XSSFWorkbook workbook) {
        StringBuilder sheets = new StringBuilder();
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            if (i > 0) sheets.append(", ");
            sheets.append(workbook.getSheetName(i));
        }
        return sheets.toString();
    }

    public static void captureScreenshot() throws IOException {

        File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

        Date d = new Date();
        screenshotName = d.toString().replace(":", "_").replace(" ", "_") + ".jpg";
        // FileUtils.copyFile(scrFile, new File(System.getProperty("user.dir") + "\\target\\surefire-reports\\html\\" + screenshotName));
        FileUtils.copyFile(scrFile, new File(System.getProperty("user.dir") + File.separator + "ExtentReport" + File.separator + screenshotName));

    }


    public static void WaitUpToDownloadFile(String fileName) {

        String Home = System.getProperty("user.home");
        File file = new File(Home + "\\Downloads\\" + fileName);
        FluentWait<WebDriver> wait = new FluentWait<WebDriver>(driver)
                .withTimeout(Duration.ofSeconds(70)).pollingEvery(Duration.ofMillis(1000));
        wait.until(x -> file.exists());

        if (file.exists()) {
            test.log(LogStatus.PASS, fileName + " -> is download successfully");
            log.info(fileName + " is download successfully");
        } else {
            test.log(LogStatus.FAIL, fileName + " -> is doesn't Exist");
            log.info(fileName + " doesn't Exist");
        }

    }

    public static void DeleteDownloadFile(String fileName) {


        String Home = System.getProperty("user.home");

        File file = new File(Home + "\\Downloads\\" + fileName);

        if (file.exists()) {

            file.delete();
            test.log(LogStatus.INFO, fileName + " -> is deleted successfully");
            log.info(fileName + " is deleted successfully");
        } else {
            test.log(LogStatus.INFO, fileName + " -> is doesn't Exist");
            log.info(fileName + " is doesn't Exist");
        }

    }

    public static void verifyLinks(List<WebElement> links) {

        for (WebElement link : links) {

            String url = link.getAttribute("href");
            HttpURLConnection connection = null;
            try {
                if (link.isDisplayed()) {

                    connection = (HttpURLConnection) new URL(url).openConnection();
                    connection.setRequestMethod("HEAD");
                    connection.connect();
                    int responseCode = connection.getResponseCode();

                    if (responseCode >= 400) {

                        test.log(LogStatus.INFO, url + " :- is Displayed but it is Broken Link");
                        log.info(url + "  :- HTTP STATUS - " + connection.getResponseMessage());
                        throw new RuntimeException("Broken link: " + url);

                    } else {
                        test.log(LogStatus.INFO, url + " :- is Displayed and it is Valid Link");
                        log.info(url + "  :- HTTP STATUS - " + connection.getResponseMessage());

                    }
                } else {
                    test.log(LogStatus.INFO, url + "-> is not Displayed");
                }
            } catch (IOException e) {

                throw new RuntimeException("verifying links are not Available:" + url, e);

            } finally {

                if (connection != null) {
                    connection.disconnect();
                }
            }
        }
    }

    public static void captureScreenshotForExtentReport() throws IOException {

        File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        Date d = new Date();

        // Format filename
        screenshotName = d.toString().replace(":", "_").replace(" ", "_") + ".jpg";

        // Format date folder (e.g., Jul_23_2025)
        String dateFolder = currentdate(); // Make sure this returns something like "Jul_23_2025"

        // Folder to store screenshot
        String screenshotDir = System.getProperty("user.dir") + File.separator + "Report"+ File.separator + "ExtentScreenShots" + File.separator + dateFolder;
        File folder = new File(screenshotDir);
        if (!folder.exists()) folder.mkdirs();

        // Save file
        String fullPath = screenshotDir + File.separator + screenshotName;
        FileUtils.copyFile(scrFile, new File(fullPath));
    }

    public static String checkDomain(String toAddress) {
        if (!toAddress.contains("@accenture.com")) {
            toAddress = (new StringBuilder()).append(toAddress).append("@accenture.com").toString();
        }
        return toAddress;
    }

    public static void catchException(NoSuchElementException e) throws IOException {

        e.printStackTrace();
        Boolean close = driver.findElement(By.xpath("//*[@class='icon-close']")).isDisplayed();
        if (close)
            driver.findElement(By.xpath("//*[@class='icon-close']")).click();
        Assert.fail();
    }

    /*
 public static String  CaptureFullPageForExtentReport() throws IOException {

       Date d = new Date();
       screenshotName = d.toString().replace(":", "_").replace(" ", "_") + ".png";
       Screenshot screenshot = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(ShootingStrategies.scaling(1.5f), 1000)).takeScreenshot(driver);
       String imagepath = System.getProperty("user.dir")+ File.separator+"ExtentScreenShots"+File.separator+ Utilities.currentdate()+File.separator+Utilities.screenshotName;

       try {
         ImageIO.write(screenshot.getImage(),"PNG",new File(imagepath));
     } catch (IOException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
     }
 return imagepath;
 }

     */
    public static void Closewindow() throws IOException {

        Boolean close = driver.findElement(By.xpath("(//*[text()='Cancel'])[2]")).isDisplayed();
        if (close)
            driver.findElement(By.xpath("//*[@class='icon-close']")).click();
        Assert.fail();
    }


    public static String Getdata(String sheetName, String eleName) {
        String value = null;
        ArrayList<Object> columndata = null;
        try {
            File f = new File(strBrowser + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "test.xlsx");
            Tfis = new FileInputStream(f);
            wb = new XSSFWorkbook(Tfis);
            Tfis.close();
            XSSFSheet sh = wb.getSheet(sheetName);
            if (sh == null) {
                System.err.println("ERROR: Sheet '" + sheetName + "' not found in test.xlsx. Available sheets: " + getAvailableSheets(wb));
                return null;
            }
            Iterator<Row> rowIterator = sh.iterator();
            columndata = new ArrayList<Object>();
            CellStyle cellStyle = wb.createCellStyle();
            CreationHelper createHelper = wb.getCreationHelper();
            cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("m/d/yy h:mm"));
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    if (row.getRowNum() >= 0) {
                        //To filter column headings
                        if (cell.getColumnIndex() == 0) {
                            //To match column index
                            switch (cell.getCellType()) {
                                case NUMERIC:
                                    columndata.add(cell.getNumericCellValue() + "");
                                    break;
                                case STRING:
                                    columndata.add(cell.getStringCellValue());
                                    break;
                                case BLANK:
                                    break;
                                case BOOLEAN:
                                    columndata.add(cell.getBooleanCellValue());
                                    break;
                                case ERROR:
                                    break;
                                case FORMULA:
                                    break;
                                case _NONE:
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                }
            }
            ArrayList<Object> ele_name1 = columndata;
            if (ele_name1.contains(eleName)) {
                int req_index = ele_name1.indexOf(eleName);
                value = sh.getRow(req_index).getCell(1).getStringCellValue();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    public static String updatevalue(String sheetName, String eleName, String value) throws IOException {

        ArrayList<String> columndata = null;

        try {
            File f = new File(strBrowser + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "test.xlsx");
            Tfis = new FileInputStream(f);
            wb = new XSSFWorkbook(Tfis);
            Tfis.close();

            XSSFSheet sh = wb.getSheet(sheetName);
            Iterator<Row> rowIterator = sh.iterator();
            columndata = new ArrayList<String>();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    if (row.getRowNum() >= 0) { //To filter column headings
                        if (cell.getColumnIndex() == 0) {//To match column index
                            switch (cell.getCellType()) {
                                case NUMERIC:
                                    columndata.add(cell.getNumericCellValue() + "");
                                    break;
                                case STRING:
                                    columndata.add(cell.getStringCellValue());
                                    break;
                                case BLANK:
                                    break;
                                case BOOLEAN:
                                    break;
                                case ERROR:
                                    break;
                                case FORMULA:
                                    break;
                                case _NONE:
                                    break;
                                default:
                                    break;

                            }
                        }
                    }
                }
            }
            ArrayList<String> ele_name1 = columndata;
            if (ele_name1.contains(eleName)) {
                int req_index = ele_name1.indexOf(eleName);
                Cell cell = sh.getRow(req_index).getCell(1);
                cell.setCellValue(value);
                FileOutputStream fos = new FileOutputStream(strBrowser + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "test.xlsx");
                wb.write(fos);
                fos.close();

                FileInputStream Tfis = new FileInputStream(f);
                Workbook workbook = WorkbookFactory.create(f);

                //wb = new XSSFWorkbook(Tfis);
                Tfis.close();
                workbook.close();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    public static void CaptureErrorMessage() {


        try {
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
            WebElement element = driver.findElement(By.xpath("(//*[text()='Error'])[1]"));
            element.isDisplayed();
            Utilities.captureScreenshotForExtentReport();
            test.log(LogStatus.PASS,  "Error pop up message"+test.addScreenCapture(GetrelativePath()));
        } catch (Exception e) {


        }


    }


    public static String dateformat() throws IOException {

        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMMyyyy_hh_mm a");
        String formattedDate = dateFormat.format(date);
        //String date1 = dateFormat.format(date);
        return formattedDate;

    }


    public static void Isdisabled(WebElement element) throws IOException {

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.visibilityOf(element));
        if (!element.isEnabled()) {
            test.log(LogStatus.PASS, element.getText() + " : is disabled and not clickable.");
        } else if (element.isEnabled() && element.isDisplayed()) {
            element.click();
            test.log(LogStatus.ERROR, element.getText() + " : is clickable and clicked.");
        }


    }

    public static void IsNotdisplayed(WebElement elementToValidate) throws IOException {

        SoftAssert softAssert = new SoftAssert();
        if (!elementToValidate.isDisplayed()) {
            test.log(LogStatus.PASS, elementToValidate.getText() + " : is Not Visible");

        } else {
            test.log(LogStatus.FAIL, elementToValidate.getText() + " : is Visible");
            softAssert.fail("Element is  Visible");
        }
        softAssert.assertAll();


    }

    public static void VerifyLink(WebElement link) throws IOException, InterruptedException {
        scrollIntoView(link);

        if (link.isDisplayed()) {
            if (link.isEnabled()) {
                test.log(LogStatus.PASS, "Link is displayed and Enabled");
            } else {
                test.log(LogStatus.ERROR, link.getText() + " is displayed but is not enabled");
            }
        } else {
            test.log(LogStatus.FAIL, "Link is not displayed");
        }
        // Scroll back to top after verification
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        Thread.sleep(2000);
        jse.executeScript("window.scrollTo(0, 0)");
        Thread.sleep(2000);

        //softAssert.assertAll();
    }

    public static void verifyLink(WebElement link) throws IOException, InterruptedException {
        scrollIntoView(link);

        SoftAssert softAssert = new SoftAssert();

        if (link.isDisplayed()) {
            if (link.isEnabled()) {
                String href = link.getAttribute("href");
                String linkText = link.getText().trim();

                test.log(LogStatus.PASS, linkText + " link is displayed & enabled");

                if (href != null && !href.isEmpty()) {
                    test.log(LogStatus.INFO, "Href: " + href);

                    // Check if href contains any uppercase characters
                    if (!href.equals(href.toLowerCase())) {
                        softAssert.fail("Href contains uppercase letters: " + href);
                        test.log(LogStatus.FAIL, "Href contains uppercase letters: " + href);
                    } else {
                        test.log(LogStatus.PASS, "Href is all lowercase: " + href);
                    }

                } else {
                    softAssert.fail("Href is null or empty for link: " + linkText);
                    test.log(LogStatus.FAIL, "Href is null or empty for link: " + linkText);
                }

            } else {
                test.log(LogStatus.ERROR, link.getText() + " is displayed but is not enabled");
            }
        } else {
            test.log(LogStatus.FAIL, "Link is not displayed");
        }

        // Scroll back to top after verification
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        Thread.sleep(2000);
        jse.executeScript("window.scrollTo(0, 0)");
        Thread.sleep(2000);

        softAssert.assertAll();
    }


    public static void isButtonNotClickable(WebElement button) throws IOException {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            wait.until(ExpectedConditions.elementToBeClickable(button));
            button.click();
            test.log(LogStatus.ERROR, button.getText() + "  : is clickable:");
        } catch (Exception e) {
            Utilities.captureScreenshotForExtentReport();
            // String imagepath = System.getProperty("user.dir") + File.separator + "ExtentScreenShots" + File.separator + Utilities.currentdate() + File.separator + Utilities.screenshotName;
            test.log(LogStatus.PASS, button.getText() + "  : is Disabled and Not clickable:" + test.addScreenCapture(GetrelativePath()));
        }
    }

    /*
     * public static String addScreenshot() { byte[] screenshotBytes =
     * ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES); byte[]
     * compressedScreenshotBytes = compressImage(screenshotBytes); String
     * encodedScreenshot = Base64.encodeBase64String(compressedScreenshotBytes);
     * return "data:image/png;base64," + encodedScreenshot; }
     */


    /*
     * public static byte[] compressImage(byte[] imageData) { try { BufferedImage
     * image = ImageIO.read(new ByteArrayInputStream(imageData));
     * ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
     *
     * // Get ImageWriter instance for JPG format ImageWriter jpgWriter =
     * ImageIO.getImageWritersByFormatName("jpeg").next(); ImageWriteParam param =
     * jpgWriter.getDefaultWriteParam();
     *
     * // Set compression quality
     * param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
     * param.setCompressionQuality(1.0f); // Adjust quality (0.0f - 1.0f)
     *
     * // Compress the image to the ByteArrayOutputStream jpgWriter.setOutput(new
     * MemoryCacheImageOutputStream(outputStream)); jpgWriter.write(null, new
     * IIOImage(image, null, null), param); jpgWriter.dispose();
     *
     * return outputStream.toByteArray(); } catch (IOException e) {
     * e.printStackTrace(); return imageData; // Return original image data if
     * compression fails }
     *
     * }
     */

    public static boolean isImageBroken(String imgUrl) {
        try {
            URL url = new URL(imgUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            int responseCode = connection.getResponseCode();
            return responseCode != 200;
        } catch (IOException e) {
            e.printStackTrace();
            return true;  // Assume the image is broken if there's an exception
        }
    }

    public static void checkCharacterLimit(WebElement textBox, int charLimit) throws InterruptedException {

        // Create a string longer than charLimit
        String longString = new String(new char[charLimit + 10]).replace('\0', 'T');

        textBox.clear(); // Clear any existing text
        Thread.sleep(2000);
        textBox.sendKeys(longString); // Input the long string into the text box
        Thread.sleep(2000);

        String actualText = textBox.getAttribute("value"); // Get the value from the text box
        int actualLength = actualText.length();

        System.out.println("Expected max length: " + charLimit);
        System.out.println("Actual length: " + actualLength);

        // Soft assert that the actual length does not exceed the character limit
        Assert.assertTrue(actualLength <= charLimit, "Text box exceeds character limit!");
        test.log(LogStatus.INFO, "Text box correctly limits input to " + charLimit + " characters.");

    }


    //private  static final SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");

    private static final SimpleDateFormat formatter = new SimpleDateFormat("MMM dd, yyyy");


    public static String getFormattedDate(Date date) throws IOException {

        return formatter.format(date);

    }

    public static String Todaydate() throws IOException {

        return getFormattedDate(calendar.getTime());

    }

    public static String yesterdaydate() throws IOException {

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -2);
        return getFormattedDate(calendar.getTime());

    }

    public static String futuredate(int days) throws IOException {

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, days);
        return getFormattedDate(calendar.getTime());

    }

    public static String currentdate() throws IOException {

        DateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
        Date date = new Date();
        String date1 = dateFormat.format(date);
        return date1;

    }

    public static String Pastdate(int days) throws IOException {

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, days);
        return getFormattedDate(calendar.getTime());

    }

    public static void isButtonClickable(WebElement button) throws IOException {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            wait.until(ExpectedConditions.elementToBeClickable(button));
            button.isEnabled();
            Utilities.captureScreenshotForExtentReport();
            String imagepath = System.getProperty("user.dir") + File.separator + "ExtentScreenShots" + File.separator + Utilities.currentdate() + File.separator + Utilities.screenshotName;
            test.log(LogStatus.PASS, button.getText() + "  : is clickable:" + test.addScreenCapture(GetrelativePath()));

        } catch (Exception e) {
            test.log(LogStatus.ERROR, button.getText() + "  : is Not  clickable:");
        }
    }

    public static void FluentWait(WebElement element) throws IOException {
        FluentWait<WebDriver> wait = new FluentWait<WebDriver>(driver)
                .withTimeout(Duration.ofSeconds(20))
                .pollingEvery(Duration.ofMillis(1000));
        wait.until(driver -> element.isDisplayed());
        /*wait.until(ExpectedConditions.and(ExpectedConditions.visibilityOf(element), ExpectedConditions.elementToBeClickable(element)

        ));*/
    }


    /*  DO not Uncomment this code is for reference Please Do not Touch it.
    public static String Getdatafromexcel(String sheetName, String rowName, String columnName) throws IOException {
          FileInputStream file = new FileInputStream(strBrowser + "/TestData/test.xlsx");
          Workbook workbook = new XSSFWorkbook(file);

          // Get the sheet by name
          Sheet sheet = workbook.getSheet(sheetName);

          // Find the row with the given rowName (assuming it's the first column)
          Row row = null;
          int rowIndex = -1;
          for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i++) {
              row = sheet.getRow(i);
              if (row != null && row.getCell(0).getStringCellValue().equals(rowName)) { // assuming rowName is in the first column
                  rowIndex = i;
                  break;
              }
          }

          if (rowIndex == -1) {
              workbook.close();
              return "Row with the specified name not found.";
          }

          // Get the column index based on columnName (assuming column names are in the first row)
          Row headerRow = sheet.getRow(0);  // First row contains column names
          int colIndex = -1;
          for (int j = 0; j < headerRow.getPhysicalNumberOfCells(); j++) {
              if (headerRow.getCell(j).getStringCellValue().equals(columnName)) {
                  colIndex = j;
                  break;
              }
          }

          if (colIndex == -1) {
             // workbook.close();
              return "Column with the specified name not found.";
          }

          // Get the value from the cell in the given row and column
          row = sheet.getRow(rowIndex);
          Cell cell = row.getCell(colIndex);
          String cellValue = cell != null ? cell.toString() : null;

          workbook.close();
          return cellValue;
      }
  */
    public static String updateDataInExcel(String sheetName, String rowName, String columnName, String newValue) throws IOException {
        // Open the Excel file
        FileInputStream file = new FileInputStream(strBrowser + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "test.xlsx");
        Workbook workbook = new XSSFWorkbook(file);

        // Get the sheet by name
        Sheet sheet = workbook.getSheet(sheetName);

        // Find the row with the given rowName (assuming it's in the first column)
        Row row = null;
        int rowIndex = -1;
        for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i++) {
            row = sheet.getRow(i);
            if (row != null && row.getCell(0).getStringCellValue().equals(rowName)) { // assuming rowName is in the first column
                rowIndex = i;
                break;
            }
        }

        if (rowIndex == -1) {
            workbook.close();
            return "Row with the specified name not found.";
        }

        // Get the column index based on columnName (assuming column names are in the first row)
        Row headerRow = sheet.getRow(0);  // First row contains column names
        int colIndex = -1;
        for (int j = 0; j < headerRow.getPhysicalNumberOfCells(); j++) {
            if (headerRow.getCell(j).getStringCellValue().equals(columnName)) {
                colIndex = j;
                break;
            }
        }

        if (colIndex == -1) {
            workbook.close();
            return "Column with the specified name not found.";
        }

        // Get the cell in the specified row and column
        row = sheet.getRow(rowIndex);
        Cell cell = row.getCell(colIndex);

        if (cell == null) {
            cell = row.createCell(colIndex);  // Create a new cell if it doesn't exist
        }

        // Update the value of the cell
        cell.setCellValue(newValue);

        // Write the changes back to the Excel file
        FileOutputStream outFile = new FileOutputStream(new File(strBrowser + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "test.xlsx"));
        workbook.write(outFile);

        // Close the workbook and file streams
        outFile.close();
        workbook.close();

        return "Data updated successfully.";
    }

    public static String Getdatafromexcel(String sheetName, String rowName, String columnName) throws IOException {
        FileInputStream file = null;
        Workbook workbook = null;
        String result = null;

        try {
            // Open the Excel file
            file = new FileInputStream(strBrowser + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "test.xlsx");
            workbook = new XSSFWorkbook(file);

            // Get the sheet by name
            Sheet sheet = workbook.getSheet(sheetName);

            if (sheet == null) {
                System.err.println("ERROR: Sheet '" + sheetName + "' not found in test.xlsx. Available sheets: " + getAvailableSheets((XSSFWorkbook) workbook));
                return "Sheet '" + sheetName + "' not found";
            }

            // Find the row with the given rowName (assuming it's in the first column)
            Row row = null;
            int rowIndex = -1;
            for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i++) {
                row = sheet.getRow(i);
                if (row != null && row.getCell(0).getStringCellValue().equals(rowName)) { // assuming rowName is in the first column
                    rowIndex = i;
                    break;
                }
            }

            if (rowIndex == -1) {
                return "Row with the specified name not found.";
            }

            // Get the column index based on columnName (assuming column names are in the first row)
            Row headerRow = sheet.getRow(0);  // First row contains column names
            int colIndex = -1;
            for (int j = 0; j < headerRow.getPhysicalNumberOfCells(); j++) {
                if (headerRow.getCell(j).getStringCellValue().equals(columnName)) {
                    colIndex = j;
                    break;
                }
            }

            if (colIndex == -1) {
                return "Column with the specified name not found.";
            }

            // Get the value from the cell in the given row and column
            row = sheet.getRow(rowIndex);
            Cell cell = row.getCell(colIndex);

            // Initialize FormulaEvaluator to evaluate formulas
            FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();

            // Check if the cell contains a formula
            if (cell != null && cell.getCellType() == CellType.FORMULA) {
                // Evaluate the formula result
                CellValue cellValue = formulaEvaluator.evaluate(cell);
                switch (cellValue.getCellType()) {
                    case NUMERIC:
                        result = String.valueOf(cellValue.getNumberValue());
                        break;
                    case STRING:
                        result = cellValue.getStringValue();
                        break;
                    case BOOLEAN:
                        result = String.valueOf(cellValue.getBooleanValue());
                        break;
                    default:
                        result = "Unknown cell type";
                        break;
                }
            } else if (cell != null) {
                // If not a formula, just retrieve the cell's value directly based on the cell type
                switch (cell.getCellType()) {
                    case STRING:
                        result = cell.getStringCellValue();
                        break;
                    case NUMERIC:
                        result = String.valueOf(cell.getNumericCellValue());
                        break;
                    case BOOLEAN:
                        result = String.valueOf(cell.getBooleanCellValue());
                        break;
                    case BLANK:
                        result = "Blank cell";
                        break;
                    default:
                        result = "Unsupported cell type";
                        break;
                }
            } else {
                result = "Cell is empty";
            }
        } finally {
            // Close the workbook and file input stream
            if (workbook != null) {
                workbook.close();
            }
            if (file != null) {
                file.close();
            }
        }

        return result;
    }


    /**
     * Method to select a specific date from MUI Calendar Picker
     * @param targetDate - Date in format "MMM dd, yyyy" (e.g., "Dec 15, 2025")
     * @throws IOException, InterruptedException
     */
    public static void selectDateFromMUICalendar(String targetDate) throws IOException, InterruptedException {
        try {
            // Parse the target date
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);
            Date target = dateFormat.parse(targetDate);
            Calendar targetCal = Calendar.getInstance();
            targetCal.setTime(target);

            int targetYear = targetCal.get(Calendar.YEAR);
            int targetMonth = targetCal.get(Calendar.MONTH); // 0-based
            int targetDay = targetCal.get(Calendar.DAY_OF_MONTH);

            // Navigate to correct month and year
            navigateToMonthYear(targetYear, targetMonth);

            // Select the day
            Thread.sleep(1000);
            List<WebElement> dayButtons = driver.findElements(By.xpath("//button[@role='gridcell' and @aria-selected='false']"));

            for (WebElement dayButton : dayButtons) {
                if (dayButton.getText().trim().equals(String.valueOf(targetDay))) {
                    JavascriptExecutor js = (JavascriptExecutor) driver;
                    js.executeScript("arguments[0].click();", dayButton);
                    test.log(LogStatus.PASS, "Successfully selected date: " + targetDate);
                    log.info("Successfully selected date: " + targetDate);
                    Thread.sleep(1000);
                    break;
                }
            }
        } catch (Exception e) {
            captureScreenshotForExtentReport();
            test.log(LogStatus.FAIL, "Failed to select date: " + targetDate + " - " + e.getMessage() + test.addScreenCapture(GetrelativePath()));
            log.error("Failed to select date: " + targetDate, e);
            throw new IOException("Date selection failed", e);
        }
    }

    /**
     * Navigate to the target month and year in MUI Calendar
     * @param targetYear - Target year
     * @param targetMonth - Target month (0-based)
     */
    private static void navigateToMonthYear(int targetYear, int targetMonth) throws InterruptedException {
        try {
            // Get current displayed month and year from calendar header
            WebElement calendarLabel = driver.findElement(By.xpath("//div[contains(@class, 'MuiPickersCalendarHeader-label')]"));
            String currentMonthYear = calendarLabel.getText(); // e.g., "December 2025"

            SimpleDateFormat headerFormat = new SimpleDateFormat("MMMM yyyy", Locale.ENGLISH);
            Date currentDate = headerFormat.parse(currentMonthYear);
            Calendar currentCal = Calendar.getInstance();
            currentCal.setTime(currentDate);

            int currentYear = currentCal.get(Calendar.YEAR);
            int currentMonth = currentCal.get(Calendar.MONTH);

            // Calculate difference in months
            int monthDiff = (targetYear - currentYear) * 12 + (targetMonth - currentMonth);

            // Navigate using next/previous buttons
            if (monthDiff > 0) {
                // Navigate forward
                WebElement nextButton = driver.findElement(By.xpath("//button[@aria-label='Next month']"));
                for (int i = 0; i < monthDiff; i++) {
                    nextButton.click();
                    Thread.sleep(500);
                }
            } else if (monthDiff < 0) {
                // Navigate backward
                WebElement prevButton = driver.findElement(By.xpath("//button[@aria-label='Previous month']"));
                for (int i = 0; i < Math.abs(monthDiff); i++) {
                    prevButton.click();
                    Thread.sleep(500);
                }
            }

            Thread.sleep(1000);
            log.info("Navigated to: " + targetMonth + "/" + targetYear);
        } catch (Exception e) {
            log.error("Error navigating to month/year", e);
            throw new InterruptedException("Navigation failed: " + e.getMessage());
        }
    }

    /**
     * Select a specific day from the currently displayed calendar month
     * @param day - Day of the month (1-31)
     * @throws InterruptedException
     */
    public static void selectDay(String day) throws InterruptedException {
        // Find all the date cells in the calendar
        List<WebElement> dates = driver.findElements(By.xpath("//button[@role='gridcell']"));

        // Loop through the list and find the matching day
        for (WebElement date : dates) {
            String dayText = date.getText().trim();
            if (dayText.equals(day)) {  // Compare the text with the required day
                JavascriptExecutor js = (JavascriptExecutor) driver;
                js.executeScript("arguments[0].click();", date);
                test.log(LogStatus.PASS, "Selected day: " + day);
                log.info("Selected day: " + day);
                Thread.sleep(1000);
                break;
            }
        }

    }

    

    public static void verifyAndSetCheckbox(WebElement checkboxElement, boolean expectedState) {
        try {

            // Ensure the checkbox element is displayed and enabled
            boolean currentState = false;
            if (checkboxElement.isEnabled()) {
                currentState = checkboxElement.isSelected();
                // Change state only if it does not match the desired state
                if (!currentState) {
                    Thread.sleep(2000);
                    JavascriptExecutor js = (JavascriptExecutor) driver;
                    js.executeScript("arguments[0]. click();", checkboxElement);
                    test.log(LogStatus.PASS, "Checkbox is Selected");
                    Thread.sleep(1000);
                }
            } else if (currentState == expectedState) {
                System.out.println("Checkbox is already selected");
                test.log(LogStatus.PASS, "Checkbox is already selected");
            }

        } catch (Exception e) {
            System.out.println("Error while interacting with checkbox: " + e.getMessage());
            test.log(LogStatus.FAIL,  "Error while interacting with checkbox: "+e.getMessage());
        }
    }

   

}

