package com.burritopos.server.service.test;

import org.apache.log4j.PropertyConfigurator;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		String propertiesFile = "log4j.properties";
        PropertyConfigurator.configure(propertiesFile);
		
		TestSuite suite = new TestSuite("Test for com.burritopos.server.service.test");
		//$JUnit-BEGIN$
		suite.addTest(com.burritopos.server.domain.test.AllTests.suite());
		suite.addTestSuite(UserSvcImplTestCase.class);
		//$JUnit-END$
		return suite;
	}

}
