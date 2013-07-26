/**
 * 
 */
package com.burritopos.server.test;

// extending test case infers Junit3 when we want Junit4
//import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author james.bloom
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public abstract class BurritoPOSTestCase {
	private static Logger dLog = Logger.getLogger(BurritoPOSTestCase.class);
	
	@Rule
	public TestWatcher watcher = new TestWatcher() {
		protected void starting(Description description) {
			dLog.info(String.format("*** Starting test: %s() ***", description.getMethodName()));
		};
	};

	public BurritoPOSTestCase() {
		String propertiesFile = "log4j.properties";
		PropertyConfigurator.configure(propertiesFile);
	}
	
	/**
	 * Sets up the necessary code to run the tests.
	 *
	 * @throws Exception if it cannot set up the test.
	 */
	@Before
	public void initCommonResources() throws Exception {
		dLog.info("*** Initializing common resources ***");
	}

	/**
	 * Tears down common setup
	 *
	 * @throws Exception if it cannot tear down the test.
	 */
	@After
	public void tearDownCommonResources() throws Exception {
		dLog.info("*** Cleaning up common resources ***");
	}
}