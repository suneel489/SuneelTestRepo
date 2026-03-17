package com.ExtentReportListener;

import java.io.File;
import java.io.IOException;

import com.qa.utilities.ExtentManager;
import com.qa.utilities.Screenshot;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import org.testng.Reporter;

import com.qa.base.Baseclass;
import com.qa.utilities.Utilities;
import com.relevantcodes.extentreports.LogStatus;

public class CustomListeners extends Baseclass implements ITestListener, ISuiteListener {
	
//public static String executionMode = Baseclass.getExecutionMode();
public static String currentSuiteName;
	public void onFinish(ITestContext arg0) {
		
     // TODO Auto-generated method stub
	}

	public void onStart(ITestContext arg0) {
		
    // TODO Auto-generated method stub
	}

	public void onTestFailedButWithinSuccessPercentage(ITestResult arg0) {
		
    // TODO Auto-generated method stub getName().toUpperCase()
	}

	public void onTestFailure(ITestResult arg0) {
		
		System.setProperty("org.uncommons.reportng.escape-output", "false");
	//	test.log(LogStatus.FAIL, test.addBase64ScreenShot(Utilities.addScreenshot())+arg0.getName()+ " Failed with exception : " + arg0.getThrowable());
		try {
			Utilities.captureScreenshotForExtentReport();
			test.log(LogStatus.FAIL, test.addScreenCapture(GetrelativePath())+arg0.getName() + " Failed with exception : " + arg0.getThrowable());
		} catch (Exception e) {

		}
		rep.endTest(test);
		rep.flush();

	}

	public void onTestSkipped(ITestResult arg0) {
		
		//test.log(LogStatus.SKIP, test.addBase64ScreenShot(Utilities.addScreenshot())+arg0.getName()+ " Skipped with exception : " + arg0.getThrowable());
		try {
			Utilities.captureScreenshotForExtentReport();
			test.log(LogStatus.SKIP, test.addScreenCapture(GetrelativePath())+arg0.getName() + " is Skipped ");
		} catch (Exception e) {

		}

		rep.endTest(test);
		rep.flush();
	}

	public void onTestStart(ITestResult arg0) {


		test = rep.startTest("<span style='color:blue;'>" + arg0.getName() + "</span>");
		// Assign suite name as category for dropdown under Test
		if (CustomListeners.currentSuiteName != null) {

			test.assignCategory(CustomListeners.currentSuiteName);
		}


	}

	public void onTestSuccess(ITestResult arg0) {
		
		try {
			
			/*if (executionMode.equalsIgnoreCase("Pipeline"))
				 
			  {
				test.log(LogStatus.PASS, arg0.getName() + " PASS");
			  }
			else {*/
				
				
				Utilities.captureScreenshotForExtentReport();
				//String imagepath = System.getProperty("user.dir")+ File.separator+"ExtentScreenShots"+File.separator+ Utilities.currentdate()+File.separator+Utilities.screenshotName;
				test.log(LogStatus.PASS, test.addScreenCapture(GetrelativePath())+arg0.getName() + " is Passed ");

			
			
			rep.endTest(test);
			rep.flush();


		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	public void onFinish(ISuite arg0) {

	}
	@Override
	public void onStart(ISuite suite) {
		currentSuiteName = suite.getName(); // this fetches the suite name from the XML actually running
		System.out.println("Running suite: " + currentSuiteName);
		// Always create a new ExtentReport for each suite
        try {
            ExtentManager.getInstance();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


}
