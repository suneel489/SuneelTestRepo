package com.qa.base;

import org.testng.TestNG;

import java.util.Collections;

public class TestRunner {
	
    public static String SuiteFolder = System.getProperty("user.dir");
    public static void main(String[] args){

        TestNG testNG = new TestNG();
        testNG.setTestSuites(Collections.singletonList(SuiteFolder+"\\master-testng.xml"));
        testNG.run();
       
        
    }

}
