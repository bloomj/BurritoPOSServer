/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.burritopos.server.business;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.*;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Properties;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.log4j.*;
import org.springframework.beans.factory.annotation.Autowired;

import com.burritopos.server.presentation.StatusUI;


/**
 *
 * @author james.bloom
 */
public class SecureConnectionManager implements IConnectionManager {
    private static Logger dLog = Logger.getLogger(SecureConnectionManager.class);
    private SSLServerSocketFactory factory = null;
    private ServerSocket server = null;
    private boolean exit = false;
    private static ArrayList<SocketManager> smanagers = new ArrayList<SocketManager>();
    private static ArrayList<Thread> sthreads = new ArrayList<Thread>();
    @Autowired
    private StatusUI parent;

    private static int BURRITOPOS_SERVER_PORT;
    private static String KEYSTORE_PATH;
    private static String KEYSTORE_PASSWORD;
    
    public SecureConnectionManager(StatusUI parent) throws IOException, UnrecoverableKeyException, KeyManagementException, NoSuchAlgorithmException, CertificateException, KeyStoreException {
        super();

    	// manually load properties
        Properties propList = new Properties();
        dLog.trace("Loading burritopos.properties");

        propList.load(this.getClass().getClassLoader().getResourceAsStream("burritoposserver.properties"));
        BURRITOPOS_SERVER_PORT = Integer.parseInt(propList.getProperty("burritopos.server.port"));     
        dLog.trace("Got BURRITOPOS_SERVER_PORT value: " + BURRITOPOS_SERVER_PORT);
        KEYSTORE_PATH = propList.getProperty("ssl.keystore.path");     
        dLog.trace("Got KEYSTORE_PATH value: " + KEYSTORE_PATH);
        KEYSTORE_PASSWORD = propList.getProperty("ssl.keystore.password");     
        dLog.trace("Got KEYSTORE_PASSWORD value: " + KEYSTORE_PASSWORD);
        
        this.parent = parent;
        
        initSSL();
    }
    
    public SecureConnectionManager(StatusUI parent, String port, String keystorePath, String keystorePassword) throws UnrecoverableKeyException, KeyManagementException, NoSuchAlgorithmException, CertificateException, FileNotFoundException, KeyStoreException, IOException {
        super();

        // allow Spring to inject properties
    	BURRITOPOS_SERVER_PORT = Integer.parseInt(port);
    	dLog.trace("Got BURRITOPOS_SERVER_PORT value: " + BURRITOPOS_SERVER_PORT);
    	KEYSTORE_PATH = keystorePath;
    	dLog.trace("Got KEYSTORE_PATH value: " + KEYSTORE_PATH);
    	KEYSTORE_PASSWORD = keystorePassword;
    	dLog.trace("Got KEYSTORE_PASSWORD value: " + KEYSTORE_PASSWORD);
        
        this.parent = parent;
        
        initSSL();
    }
    
    private void initSSL() throws NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException, KeyStoreException, UnrecoverableKeyException, KeyManagementException {
    	KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
    	dLog.trace("Loading keystore");
        keyStore.load(new FileInputStream(KEYSTORE_PATH), KEYSTORE_PASSWORD.toCharArray());

        dLog.trace("Initializing KeyManagerFactory");
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(keyStore, KEYSTORE_PASSWORD.toCharArray());
        dLog.trace("Initializing SSL Context");
        System.setProperty("https.protocols", "TLSv1");
        SSLContext ctx = SSLContext.getInstance("TLS");
        // Note: only care about encryption at this time (Confidentiality), will add trust later (Integrity)
        ctx.init(kmf.getKeyManagers(), trustAllCerts, new java.security.SecureRandom());
        dLog.trace("Retreiving SSL socket factory");
        factory = ctx.getServerSocketFactory();
    }

    @Override
    public void run() {
        try {
            startServer();
        }
        catch(Exception e) {
            dLog.error("Exception in SecureConnectionManager run", e);
        }
    }

    private void startServer() throws Exception {
        try {
        	dLog.info("Initializing server");
        	parent.updateStatus("Initializing Server");
            server = factory.createServerSocket(BURRITOPOS_SERVER_PORT);
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
            	dLog.trace("Setting exit for socket manager: " + n);
                ((SocketManager)smanagers.get(n)).setExit(true);
                ((SocketManager)smanagers.get(n)).close();
                
                dLog.trace("Removing socket manager: " + n);
                smanagers.remove(n);
            }
            
            for(int n=0; n<sthreads.size(); n++) {
				if((Thread)sthreads.get(n) != null && ((Thread)sthreads.get(n)).isAlive()) {
					dLog.trace("Setting removing socket thread: " + n);
					((Thread)sthreads.get(n)).join(1000);
					dLog.trace("Socket thread: " + n + " successfully ended via join");
				}
				
				dLog.trace("Removing socket thread: " + n);
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
    
    // Create a trust manager that does not validate certificate chains
    TrustManager[] trustAllCerts = new TrustManager[] { 
        new X509TrustManager() {     
            public java.security.cert.X509Certificate[] getAcceptedIssuers() { 
                return null;
            } 
            public void checkClientTrusted( 
                java.security.cert.X509Certificate[] certs, String authType) {
                } 
            public void checkServerTrusted( 
                java.security.cert.X509Certificate[] certs, String authType) {
            }
        } 
    }; 
}
