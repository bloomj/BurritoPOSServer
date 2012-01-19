package com.burritopos.server.service.test;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for ecom.burritopos.server.service.test");
		//$JUnit-BEGIN$
		suite.addTestSuite(UserSvcImplTestCase.class);
		//$JUnit-END$
		return suite;
	}

}
