package com.qa.utilities;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import com.qa.base.Baseclass;

public class Screenshot extends Baseclass {

	public static String screenshotName;
	public static String path;
	public static String methodName;
	public static int min = 200;  
	public static int max = 400; 
	public static int i = (int)(Math.random()*(max-min+1)+min);  
	public static Calendar calendar = Calendar.getInstance();
	public static int hour = calendar.get(Calendar.HOUR_OF_DAY);
	public static int minute = calendar.get(Calendar.MINUTE);

	
	public static void screenshotfoldergeneration(String methodName) {
		
		Date d = new Date();
		
		//Copy the screenshot in the desire location with Time
		
		//String scrFolder = ((System.getProperty("user.dir")) +"\\Screenshot\\Screenshot_"+i+"_"+ hour + "h_" + minute + "min"+"\\" +methodName);
		  String scrFolder = ((System.getProperty("user.dir")) +"/Screenshot/Screenshot_"+i+"_"+ hour + "h_" + minute + "min"+"/" +methodName);

		//String scrFolder = ((System.getProperty("user.dir")) +"\\Screenshot\\Screenshot_"+ d.toString().replace(":", "_").replace(" ", "_")+"\\" +methodName);	
		
		new File(scrFolder).mkdir();		
		System.setProperty("scr.folder", scrFolder);
	}
	
	
	public static void screenshot(String Title,String methodName)throws IOException
	{
		String scrFolder1 = System.getProperty("scr.folder");
		
	// Take the screenshot and store as file format
		
	File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);

	// Open the current date and time
	String timestamp = new SimpleDateFormat("dd__MM_yyyy_hh_mm_ss").format(new Date());

	//Copy the screenshot on the desire location with different name using current date and time
	
	FileUtils.copyFile(scrFile, new File(scrFolder1+"\\screenshot_"+timestamp+".jpg"));
	}
}

