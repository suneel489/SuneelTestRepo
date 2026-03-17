package com.qa.pom.LoginPage;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.qa.base.Baseclass;

public class LoginPageLocators extends Baseclass{
	
	@FindBy(xpath = "//*[@data-test='username']")
	public WebElement txtUsername;

	@FindBy(xpath = "//*[@data-test='password']")
	public WebElement txtPassword;

	@FindBy(xpath = "//*[@data-test='login-button']")
	public WebElement btnLogin;

	
	
	
}
