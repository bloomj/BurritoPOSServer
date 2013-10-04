/**
 * 
 */
package com.burritopos.server.service.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.burritopos.server.domain.Group;
import com.burritopos.server.service.dao.IGroupSvc;
import com.burritopos.server.test.BurritoPOSTestCase;

/**
 * @author james.bloom
 *
 */
public class GroupSvcImplTestCase extends BurritoPOSTestCase {
	@Autowired
	private IGroupSvc ics;
	private Group g;
	
	public GroupSvcImplTestCase() {
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
		
		g = new Group(1, "ROLE_USER");
	}

	/**
	 * Test method for {@link com.burritopos.server.service.dao.hibernate.GroupSvcImpl#storeGroup(com.burritopos.server.domain.Group)}.
	 * @throws Exception 
	 */
	@Test
	public void testStoreGroup() throws Exception {
		// First let's store the group
		assertTrue(ics.storeGroup(g));

		// Then let's read it back in
		g = ics.getGroup(g.getId());
		assertTrue(g.validate());

		// Update group
		assertEquals(g.getName(), "ROLE_USER");

		g.setName("ROLE_ADMIN");
		assertTrue(ics.storeGroup(g));
		
		Group g2 = ics.getGroup(g.getName());
		assertEquals(g.getName(), g2.getName());

		// Finally, let's cleanup the file that was created
		assertTrue(ics.deleteGroup(g.getId()));
	}

}
