package com.burritopos.server.test;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	com.burritopos.server.domain.test.AllTests.class,
	com.burritopos.server.service.test.AllTests.class,
	com.burritopos.server.business.test.AllTests.class,
    })
public class ApplicationTestSuite {

    @BeforeClass
    public static void setUpClass() {
        System.out.println("Master setup");

    }

    @AfterClass
    public static void tearDownClass() {
        System.out.println("Master tearDown");
    }

}