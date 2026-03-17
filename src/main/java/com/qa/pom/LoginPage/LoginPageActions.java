package com.qa.pom.LoginPage;

import java.io.IOException;

import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;

import com.qa.base.Baseclass;

public class LoginPageActions extends Baseclass {

	public LoginPageLocators LoginPageLocators;

	public LoginPageActions() {

		if (driver == null) {
			throw new IllegalStateException("WebDriver is not initialized. Please ensure browser is launched before creating page objects.");
		}

		this.LoginPageLocators = new LoginPageLocators();
		AjaxElementLocatorFactory factory = new AjaxElementLocatorFactory(driver, 20);
		PageFactory.initElements(factory, this.LoginPageLocators);

	}

		
	public LoginPageActions Username(String Data) throws IOException {
		type(LoginPageLocators.txtUsername, Data,"Enter Username");
		return this;
	}
	
	public LoginPageActions Password(String Data) throws IOException {
		type(LoginPageLocators.txtPassword, Data,"Enter password");
		return this;
	}
	
	public LoginPageActions clicklogin() throws IOException {
		click(LoginPageLocators.btnLogin,"Click on Login button");
		return this;
	}
	
	
	
}
