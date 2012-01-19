/**
 * 
 */
package com.burritopos.server.domain.test;

//import static org.junit.Assert.*;
import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
//import org.junit.Test;

import com.burritopos.server.domain.User;

/**
 * @author james.bloom
 *
 */
public class UserTestCase extends TestCase {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		super.setUp();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * User Validate Unit Test
	 */
	public void testValidUser() throws AssertionFailedError {
		try {
			User u = new User();
			u.setId(new Integer("1"));
			u.setUserName("Jim");
			u.setPassword("password");
			
			assertTrue(u.validate());
		}
		catch(Exception e) {
			System.out.println("Exception in testValidUser: " + e.getMessage());
			fail(e.getMessage());
		}
	}

	/**
	 * User Invalid Unit Test
	 */
	public void testInvalidUser() throws AssertionFailedError {
		try {
			User u = new User();
			
			assertFalse(u.validate());
		}
		catch(Exception e) {
			System.out.println("Exception in testInvalidUser: " + e.getMessage());
			fail(e.getMessage());
		}
	}
	
	/**
	 * User Equals Unit Test
	 */
	public void testEqualsUser() throws AssertionFailedError {
		try {
			User u = new User();
			u.setId(new Integer("1"));
			u.setUserName("Jim");
			u.setPassword("password");
			User x = u;
			
			assertTrue(u.equals(x));
		}
		catch(Exception e) {
			System.out.println("Exception in testEqualsUser: " + e.getMessage());
			fail(e.getMessage());
		}
	}
	
	/**
	 * User Not Equals Unit Test
	 */
	public void testNotEqualsUser() throws AssertionFailedError {
		try {
			User u = new User();
			u.setId(new Integer("1"));
			u.setUserName("Jim");
			u.setPassword("password");
			User x = new User();
			
			assertFalse(u.equals(x));
		}
		catch(Exception e) {
			System.out.println("Exception in testNotEqualsUser: " + e.getMessage());
			fail(e.getMessage());
		}
	}
}
