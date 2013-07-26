/**
 * 
 */
package com.burritopos.server.business.test;

import static org.junit.Assert.*;

import java.net.Socket;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import com.burritopos.server.business.SocketManager;
import com.burritopos.server.test.BurritoPOSTestCase;

/**
 * @author james.bloom
 *
 */
public class SocketManagerTestCase extends BurritoPOSTestCase {
    @SuppressWarnings("unused")
	private static Logger dLog = Logger.getLogger(SocketManagerTestCase.class);
    private SocketManager socket;

	public SocketManagerTestCase() {
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
		
		socket = new SocketManager(new Socket());
	}

	/**
	 * Test method for {@link com.burritopos.server.business.SocketManager#run()}.
	 * @throws InterruptedException 
	 */
	@Test
	public void testRun() throws InterruptedException {
		assertNotNull(socket);
		
		Thread serverThread = new Thread(socket);
		serverThread.start();
		
		Thread.sleep(1000);
		
		socket.setExit(true);
		
		assertTrue(socket.getExit());
	}

	/**
	 * Test method for {@link com.burritopos.server.business.SocketManager#setExit(boolean)}.
	 */
	@Test
	public void testSetExit() {
		assertNotNull(socket);
		
		assertFalse(socket.getExit());
		
		socket.setExit(true);
		
		assertTrue(socket.getExit());
	}

}
