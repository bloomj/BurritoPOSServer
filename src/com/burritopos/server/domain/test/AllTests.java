package com.burritopos.server.domain.test;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for ecom.burritopos.server.domain.test");
		//$JUnit-BEGIN$
		suite.addTestSuite(UserTestCase.class);
		//$JUnit-END$
		return suite;
	}

}
