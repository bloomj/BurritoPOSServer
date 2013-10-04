/**
 * 
 */
package com.burritopos.server.domain.test;

import static org.junit.Assert.*;
import junit.framework.AssertionFailedError;

import org.apache.log4j.Logger;
import org.junit.Test;
//import org.junit.Test;

import com.burritopos.server.domain.Group;
import com.burritopos.server.test.BurritoPOSTestCase;

/**
 * @author james.bloom
 *
 */
public class GroupTestCase extends BurritoPOSTestCase {
    @SuppressWarnings("unused")
	private static Logger dLog = Logger.getLogger(GroupTestCase.class);

	public GroupTestCase() {
		super();
	}

	/**
	 * Group Validate Unit Test
	 */
	@Test
	public void testValidGroup() throws AssertionFailedError {
		Group u = new Group();
		u.setId(new Integer("1"));
		u.setName("ROLE_USER");

		assertTrue(u.validate());
	}

	/**
	 * Group Invalid Unit Test
	 */
	@Test
	public void testInvalidGroup() throws AssertionFailedError {
		Group u = new Group();

		assertFalse(u.validate());
	}

	/**
	 * Group Equals Unit Test
	 */
	@Test
	public void testEqualsGroup() throws AssertionFailedError {
		Group u = new Group();
		u.setId(new Integer("1"));
		u.setName("ROLE_USER");
		Group x = u;

		assertTrue(u.equals(x));
	}

	/**
	 * Group Not Equals Unit Test
	 */
	@Test
	public void testNotEqualsGroup() throws AssertionFailedError {
		Group u = new Group();
		u.setId(new Integer("1"));
		u.setName("ROLE_USER");

		Group x = new Group();

		assertFalse(u.equals(x));
	}
}
