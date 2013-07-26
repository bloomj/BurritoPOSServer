/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.burritopos.server.business;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.log4j.*;
import org.springframework.beans.factory.annotation.Autowired;

import com.burritopos.server.presentation.StatusUI;


/**
 *
 * @author james.bloom
 */
public class ConnectionManager implements Runnable {
    private static Logger dLog = Logger.getLogger(ConnectionManager.class);
    private ServerSocket server = null;
    private boolean exit = false;
    private static ArrayList<SocketManager> smanagers = new ArrayList<SocketManager>();
    private static ArrayList<Thread> sthreads = new ArrayList<Thread>();
    @Autowired
    private StatusUI parent;

    private static int BURRITOPOS_SERVER_PORT;
    
    public ConnectionManager(StatusUI parent) throws IOException {
        super();

    	// manually load properties
        Properties propList = new Properties();
        dLog.trace("Loading burritopos.properties");

        propList.load(this.getClass().getClassLoader().getResourceAsStream("burritoposserver.properties"));
        BURRITOPOS_SERVER_PORT = Integer.parseInt(propList.getProperty("burritopos.server.port"));     
        dLog.trace("Got BURRITOPOS_SERVER_PORT value: " + BURRITOPOS_SERVER_PORT);
        
        this.parent = parent;
    }
    
    public ConnectionManager(StatusUI parent, String port) {
        super();

        // allow Spring to inject properties
    	BURRITOPOS_SERVER_PORT = Integer.parseInt(port);
        
        this.parent = parent;
    }

    @Override
    public void run() {
        try {
            startServer();
        }
        catch(Exception e) {
            dLog.error("Exception in ConnectionManager run", e);
        }
    }

    private void startServer() throws Exception {
        try {
        	dLog.info("Initializing server");
        	parent.updateStatus("Initializing Server");
            server = new ServerSocket(BURRITOPOS_SERVER_PORT, 100);
            // timeout blocking accept method every half second
            server.setSoTimeout(500);
            dLog.info("Server started on port: " + BURRITOPOS_SERVER_PORT);
            parent.updateStatus("Server started on port: " + BURRITOPOS_SERVER_PORT);
            while (!this.exit) {
            	try {
            		boolean result = smanagers.add(new SocketManager(server.accept(), parent));  // accept the next connection request
            		dLog.trace("Added new connection: " + result);
            		dLog.trace("Current connections: " + smanagers.size());
            		parent.updateStatus("Current connections: " + smanagers.size());
            		sthreads.add(new Thread(((SocketManager)smanagers.get(smanagers.size()-1))));
            		((Thread)sthreads.get(sthreads.size()-1)).start();
            	}
            	catch(SocketTimeoutException se) {}
            	
            	// clean up any finished threads
            	for(int n=0; n<sthreads.size(); n++) {
    				if(!((Thread)sthreads.get(n)).isAlive()) {
    					((Thread)sthreads.get(n)).join();
    					sthreads.remove(n);
    					
    					smanagers.remove(n);
    					
    					parent.updateStatus("Current connections: " + smanagers.size());
    				}
            	}
            }
        }
        catch(Exception e) {
            dLog.error("Exception in startServer", e);
        }
        finally {
            //close stop all child threads
            for(int n=0; n<smanagers.size(); n++) {
                ((SocketManager)smanagers.get(n)).setExit(true);
                
                smanagers.remove(n);
            }
            
            for(int n=0; n<sthreads.size(); n++) {
				if((Thread)sthreads.get(n) != null && ((Thread)sthreads.get(n)).isAlive()) {
					((Thread)sthreads.get(n)).join();
				}
				
				sthreads.remove(n);
            }

            server.close();
            dLog.info("Server stopped on port: " + BURRITOPOS_SERVER_PORT);
            parent.updateStatus("Server stopped on port: " + BURRITOPOS_SERVER_PORT);
            
            // reset Exit
            exit = false;
        }
    }

    public void setExit(boolean exit) {
        dLog.trace("setExit: " + exit);
        this.exit = exit;
    }
    
    public boolean getExit() {
    	return this.exit;
    }
    
    public void setParent(StatusUI parent) {
    	this.parent = parent;
    }
}
