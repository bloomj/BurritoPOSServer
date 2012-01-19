package com.burritopos.server.business.test;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for ecom.burritopos.server.business.test");
		//$JUnit-BEGIN$
		suite.addTestSuite(ConnectionManagerTestCase.class);
		suite.addTestSuite(SocketManagerTestCase.class);
		suite.addTestSuite(SocketThreadTestCase.class);
		//$JUnit-END$
		return suite;
	}

}
