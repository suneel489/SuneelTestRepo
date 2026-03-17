package com.qa.ATC;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.qa.utilities.Utilities;
import com.relevantcodes.extentreports.LogStatus;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;
import com.qa.base.Baseclass;
import com.qa.pom.LoginPage.LoginPageActions;
import com.qa.utilities.Screenshot;
public class ATC_001_LoginApplication extends Baseclass {
	
	public static String username = Utilities.Getdata("TestData", "UserID");

	public static String password = Utilities.Getdata("TestData", "Password");

	@Test(groups = {"Regression","Sanity"})
	public void Login_Application() throws InterruptedException, IOException {

		PropertyConfigurator.configure("log4j.properties");
		 
		LoginPageActions Action =  new LoginPageActions();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		Action.Username(username);
		Action.Password(password);
		Action.clicklogin();

		  }
	}
