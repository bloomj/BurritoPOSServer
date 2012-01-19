package com.burritopos.server.test;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class ApplicationTestSuite extends TestCase {

	public ApplicationTestSuite(String name) {
		super(name);
	}
	
	public static Test suite() {
		TestSuite suite = new TestSuite("Test for Burrito POS Server");
		//$JUnit-BEGIN$
                suite.addTest(com.burritopos.server.domain.test.AllTests.suite());
                suite.addTest(com.burritopos.server.service.test.AllTests.suite());
                suite.addTest(com.burritopos.server.business.test.AllTests.suite());
		//$JUnit-END$
		return suite;
	}

}
