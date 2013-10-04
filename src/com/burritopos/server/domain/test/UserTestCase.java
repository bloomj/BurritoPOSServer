/**
 * 
 */
package com.burritopos.server.domain.test;

import static org.junit.Assert.*;
import junit.framework.AssertionFailedError;

import org.apache.log4j.Logger;
import org.junit.Test;
//import org.junit.Test;

import com.burritopos.server.domain.User;
import com.burritopos.server.service.crypto.*;
import com.burritopos.server.test.BurritoPOSTestCase;

/**
 * @author james.bloom
 *
 */
public class UserTestCase extends BurritoPOSTestCase {
    @SuppressWarnings("unused")
	private static Logger dLog = Logger.getLogger(UserTestCase.class);

	public UserTestCase() {
		super();
	}

	/**
	 * User Validate Unit Test
	 */
	@Test
	public void testValidUser() throws AssertionFailedError {
		User u = new User();
		u.setId(new Integer("1"));
		u.addGroupId(new Integer("1"));
		u.setUserName("Jim");
		u.setPassword(BCrypt.hashpw("password", BCrypt.gensalt()));

		assertTrue(u.validate());
	}

	/**
	 * User Invalid Unit Test
	 */
	@Test
	public void testInvalidUser() throws AssertionFailedError {
		User u = new User();

		assertFalse(u.validate());
	}

	/**
	 * User Equals Unit Test
	 */
	@Test
	public void testEqualsUser() throws AssertionFailedError {
		User u = new User();
		u.setId(new Integer("1"));
		u.addGroupId(new Integer("1"));
		u.setUserName("Jim");
		u.setPassword(BCrypt.hashpw("password", BCrypt.gensalt()));
		User x = u;

		assertTrue(u.equals(x));
	}

	/**
	 * User Not Equals Unit Test
	 */
	@Test
	public void testNotEqualsUser() throws AssertionFailedError {
		User u = new User();
		u.setId(new Integer("1"));
		u.addGroupId(new Integer("1"));
		u.setUserName("Jim");
		u.setPassword(BCrypt.hashpw("password", BCrypt.gensalt()));
		User x = new User();

		assertFalse(u.equals(x));
	}
}
