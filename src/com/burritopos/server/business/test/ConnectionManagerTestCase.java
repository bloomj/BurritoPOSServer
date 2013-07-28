/**
 * 
 */
package com.burritopos.server.business.test;

import static org.junit.Assert.*;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.burritopos.server.business.IConnectionManager;
import com.burritopos.server.presentation.StatusUI;
import com.burritopos.server.test.BurritoPOSTestCase;

/**
 * @author james.bloom
 *
 */
public class ConnectionManagerTestCase extends BurritoPOSTestCase {
    @SuppressWarnings("unused")
	private static Logger dLog = Logger.getLogger(ConnectionManagerTestCase.class);
    @Autowired
    private IConnectionManager server;

	public ConnectionManagerTestCase() {
		super();
	}

	/**
	 * Test method for {@link com.burritopos.server.business.ConnectionManager#run()}.
	 * @throws InterruptedException 
	 */
	@Test
	public void testRun() throws InterruptedException {
		assertNotNull(server);
		
		Thread serverThread = new Thread(server);
		serverThread.start();
		
		Thread.sleep(1000);
		
		server.setExit(true);
		
		assertTrue(server.getExit());
	}

	/**
	 * Test method for {@link com.burritopos.server.business.ConnectionManager#setExit(boolean)}.
	 */
	@Test
	public void testSetExit() {
		assertNotNull(server);
		
		//assertFalse(server.getExit());
		
		server.setExit(true);
		
		assertTrue(server.getExit());
	}

	/**
	 * Test method for {@link com.burritopos.server.business.ConnectionManager#setParent(com.burritopos.server.presentation.StatusUI)}.
	 */
	@Test
	public void testSetParent() {
		assertNotNull(server);
		
		StatusUI statUI = new StatusUI();
		
		assertNotNull(statUI);
		
		server.setParent(statUI);
		
		server.setExit(true);
		
		assertTrue(server.getExit());
	}

}
