/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.burritopos.server.business;

import java.net.*;
import java.util.ArrayList;
import org.apache.log4j.*;
import com.burritopos.server.presentation.StatusUI;

import java.util.Date;

/**
 *
 * @author james.bloom
 */
public class ConnectionManager extends Thread {

    private static Logger dLog = Logger.getLogger(ConnectionManager.class);
    private ServerSocket server = null;
    private boolean exit = false;
    private static ArrayList<SocketManager> sthreads = new ArrayList<SocketManager>();
    private StatusUI par;

    public ConnectionManager() {
        super();

        sthreads = new ArrayList<SocketManager>();
    }

    @Override
    public void run() {
        try {
            startServer();
        }
        catch(Exception e) {
            dLog.info(new Date() + " | Exception in ConnectionManager run: " + e.getMessage());
        }
    }

    public void startServer() throws Exception {
        try {
        	dLog.info(new Date() + " | Initializing server");
            server = new ServerSocket(8000, 100);
            // timeout blocking accept method every second
            server.setSoTimeout(1000);
            dLog.info(new Date() + " | Server started on port 8000");
            while (!this.exit) {
            	try {
            		sthreads.add(new SocketManager(server.accept(), par));  // accept the next connection request
            		((SocketManager)sthreads.get(sthreads.size()-1)).start();
            	}
            	catch(SocketTimeoutException se) {}
            }
        }
        catch(Exception e) {
            dLog.error(new Date() + " | Exception in startServer: "+e.getMessage());
        }
        finally {
            //close stop all child threads
            for(int n=0; n<sthreads.size(); n++)
                ((SocketManager)sthreads.get(n)).setExit(true);

            server.close();
            dLog.info(new Date() + " | Server stopped on port 8000");
        }
    }

    public void setExit(boolean exit) {
        dLog.trace(new Date() + " | setExit: " + exit);
        this.exit = exit;
    }
    
    public void setParent(StatusUI parVal) {
    	dLog.trace(new Date() + " | setParent");
    	this.par = parVal;
    }
}
