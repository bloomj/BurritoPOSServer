/**
 * 
 */
package com.burritopos.server.service.test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.burritopos.server.domain.User;
import com.burritopos.server.service.crypto.*;
import com.burritopos.server.service.dao.IUserSvc;
import com.burritopos.server.test.BurritoPOSTestCase;

/**
 * @author james.bloom
 *
 */
public class UserSvcImplTestCase extends BurritoPOSTestCase {
	@Autowired
	private IUserSvc ics;
	private User u;
	
	public UserSvcImplTestCase() {
		super();
	}
	
	/**
	 * Sets up the necessary code to run the tests.
	 *
	 * @throws Exception if it cannot set up the test.
	 */
	@Before
	public void initCommonResources() throws Exception {
		super.initCommonResources();
		
		u = new User(1, 1, "james.bloom", BCrypt.hashpw("password", BCrypt.gensalt()));
		
		// add a few more groups
		u.addGroupId(3);
		u.addGroupId(56);
	}

	/**
	 * Test method for {@link com.burritopos.server.service.dao.hibernate.UserSvcImpl#storeUser(com.burritopos.server.domain.User)}.
	 * @throws Exception 
	 */
	@Test
	public void testStoreUser() throws Exception {
		// First let's store the user
		assertTrue(ics.storeUser(u));

		// Then let's read it back in
		u = ics.getUser(u.getId());
		assertTrue(u.validate());

		// Update user
		assertTrue(BCrypt.checkpw("password", u.getPassword()));

		u.setPassword(BCrypt.hashpw("password123", BCrypt.gensalt()));
		assertTrue(ics.storeUser(u));

		User u2 = ics.getUser(u.getUserName());
		assertEquals(u.getPassword(), u2.getPassword());
		
		List<User> users = ics.getUsers(56);
		
		System.out.println("users found: " + users.size());
		for(User tUser : users) {
			System.out.println("Found user: " + tUser.getUserName());
		}
		
		assertEquals(1, users.size());
		
		// Finally, let's cleanup the file that was created
		assertTrue(ics.deleteUser(u.getId()));
	}

}
