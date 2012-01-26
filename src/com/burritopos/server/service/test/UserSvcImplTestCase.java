/**
 * 
 */
package com.burritopos.server.service.test;

//import static org.junit.Assert.*;
import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.burritopos.server.service.*;
import com.burritopos.server.domain.User;

/**
 * @author james.bloom
 *
 */
public class UserSvcImplTestCase extends TestCase {
	private Factory factory;
	private User u;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		super.setUp();
		
		super.setUp();
		factory = Factory.getInstance();
		u = new User(new Integer("1"), "james.bloom", "password");
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Test method for {@link com.burritopos.server.service.UserSvcHibernateImpl#storeUser(com.burritopos.server.domain.User)}.
	 */
	@Test
	public void testStoreUser() {
		try {
			//week 3
			//IEmployeeSvc ics = factory.getEmployeeSvc();
			
			//week 4
			IUserSvc ics = (IUserSvc) factory.getService(IUserSvc.NAME);
			
			// First let's store the Employee
			assertTrue(ics.storeUser(u));
			
			// Then let's read it back in
			u = ics.getUser(u.getId());
			assertTrue(u.validate());
			
			// Finally, let's cleanup the file that was created
			assertTrue(ics.deleteUser(u.getId()));
		}
		catch(Exception e) {
			System.out.println("Exception in testStoreUser: " + e.getMessage());
			fail(e.getMessage());
		}
	}

}
