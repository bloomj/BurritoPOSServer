/**
 * 
 */
package com.burritopos.server.service.test;

import static org.junit.Assert.*;

import org.apache.log4j.*;
import org.junit.Test;

import com.burritopos.server.service.Factory;
import com.burritopos.server.service.dao.IUserSvc;
import com.burritopos.server.test.BurritoPOSTestCase;

/**
 * @author james.bloom
 *
 */
public class FactoryTestCase extends BurritoPOSTestCase {
	private Factory factory;
	@SuppressWarnings("unused")
	private static Logger dLog = Logger.getLogger(FactoryTestCase.class);

	public FactoryTestCase() {
		super();
		
		factory = Factory.getInstance();
	}

	/**
	 * Test method for {@link com.burritopos.server.service.Factory#getService()}.
	 */
    @Test
	public void testGetService() throws Exception {
    	IUserSvc userSvc = (IUserSvc) factory.getService(IUserSvc.NAME);
    	assertNotNull(userSvc);
	}

}
