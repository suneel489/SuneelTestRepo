package com.qa.utilities;

import java.io.File;
import java.io.IOException;

import com.relevantcodes.extentreports.DisplayOrder;
import com.relevantcodes.extentreports.ExtentReports;

public class ExtentManager {
	private static ExtentReports extent;
	public static String fileName;

	public static ExtentReports getInstance() throws java.io.IOException {
		if (extent == null) {
			fileName = "TestReport_" + Utilities.dateformat() + ".html";
			String reportPath = System.getProperty("user.dir") + File.separator + "Report" +
								File.separator + "ExtentReport" + File.separator +
								Utilities.currentdate() + File.separator + fileName;

			extent = new ExtentReports(reportPath, true, DisplayOrder.OLDEST_FIRST);
		}
		return extent;
	}
}
