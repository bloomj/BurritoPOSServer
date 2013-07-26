package com.burritopos.server.business;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Date;

import org.apache.log4j.*;
// TODO: figure out how to clean up Spring
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.burritopos.server.domain.User;
import com.burritopos.server.presentation.StatusUI;
import com.burritopos.server.service.crypto.BCrypt;
import com.burritopos.server.service.dao.IUserSvc;

/**
 *
 * @author james.bloom
 */
public class SocketManager implements Runnable {
    private static Logger dLog = Logger.getLogger(SocketManager.class);
    private Socket socket = null;
    private ObjectInputStream in = null;
    private ObjectOutputStream out = null;
    private boolean exit = false;
    private boolean auth = false;
    private User tUser;
    private ArrayList<User> users;
    private static String SOCKET_PORT;
    private StatusUI parent;
    private IUserSvc userSvc;

	// Spring configuration
    private static final String SPRING_CONFIG_DEFAULT = "applicationContext.xml";
    
    public SocketManager (Socket socket) {
    	this.socket = socket;

        users = new ArrayList<User>();
        tUser = new User();
        
		//Spring Framework IoC
		ClassPathXmlApplicationContext beanfactory = null;
		try {
			beanfactory = new ClassPathXmlApplicationContext(SPRING_CONFIG_DEFAULT);
			userSvc = (IUserSvc)beanfactory.getBean("userSvc");

		} catch (Exception e) {
			dLog.error("Unable to configure Spring beans", e);
		} finally {
			if (beanfactory != null) {
				beanfactory.close();
			}
		}
        
        dLog.trace("In constructor");
    }
    
    public SocketManager (Socket socket, StatusUI parent) {
    	this(socket);
    	this.parent = parent;
    }

    @Override
    public void run() {
    	SOCKET_PORT = String.valueOf(socket.getRemoteSocketAddress());
    	dLog.trace("Starting to run server");
    	// get all users
    	try {
			users = userSvc.getAllUsers();
		} catch (Exception e2) {
			dLog.error("Unable to get all users", e2);
		}
    	dLog.trace("Got " + users.size() + " users");
    	
        try {
            while (!exit) {
                out = new ObjectOutputStream(socket.getOutputStream());
                in  = new ObjectInputStream(socket.getInputStream());

                String inputStr = "";
                out.writeObject("Burrito POS Server Connected. Enter Username: ");
                dLog.trace("OUTPUT | Burrito POS Server Connected. Enter Username: ");
                parent.updateStatus(appendInfo("OUTPUT | Burrito POS Server Connected. Enter Username: "));
                
                inputStr = (String)in.readObject();
                dLog.trace("INPUT | " + inputStr);
                parent.updateStatus(appendInfo("INPUT | " + inputStr));

                while(!inputStr.equals("exit") && !this.exit) {

                    if(tUser.getUserName() == null) {
                        tUser.setUserName(inputStr);
                        out.writeObject("OK User " + inputStr + ", enter password: ");
                        dLog.trace("OUTPUT | OK User " + inputStr + ", enter password: ");
                        parent.updateStatus(appendInfo("OUTPUT | Burrito POS Server Connected. Enter Username: "));
                    }
                    else if(tUser.getPassword() == null) {
                        dLog.trace("Username: " + tUser.getUserName() + " | Password: " + inputStr);
                        for(int n=0; n<users.size(); n++) {
                        	User curUser = users.get(n);
                        	dLog.trace("Stored user: " + curUser.getUserName() + " | stored pass: " + curUser.getPassword());
                            if(curUser.getUserName().equals(tUser.getUserName()) && BCrypt.checkpw(inputStr, curUser.getPassword())) {
                                //set password
                                tUser.setPassword(BCrypt.hashpw(inputStr, BCrypt.gensalt()));
                                break;
                            }
                        }

                        if(tUser.getPassword() != null) {
                            out.writeObject("OK User verified. Enter command: ");
                            dLog.trace("OUTPUT | OK User verified. Enter command: ");
                            parent.updateStatus(appendInfo("OUTPUT | OK User verified. Enter command: "));
                            auth = true;
                        }
                        else {
                            tUser.setUserName(null);
                            tUser.setPassword(null);
                            out.writeObject("ERROR Invalid Credentials. Enter Username: ");
                            dLog.trace("OUTPUT | ERROR Invalid Credentials. Enter Username: ");
                            parent.updateStatus(appendInfo("OUTPUT | ERROR Invalid Credentials. Enter Username: "));
                        }
                    }
                    else if(auth) {
                        if(!inputStr.equals("exit")) {
                            out.writeObject("OK Command " + inputStr + " entered. Enter command: ");
                            dLog.trace("OUTPUT | OK Command " + inputStr + " entered. Enter command: ");
                            parent.updateStatus(appendInfo("OUTPUT | OK Command " + inputStr + " entered. Enter command: "));
                        }
                    }

                    inputStr = (String)in.readObject();
                    dLog.trace("INPUT | " + inputStr);
                    parent.updateStatus(appendInfo("INPUT | " + inputStr));
                }

                exit = true;
                try {
                    auth = false;
                    out.writeObject("Exiting");
                    dLog.trace("Exiting");
                    parent.updateStatus(appendInfo("Exiting"));
                    in.close();
                    out.close();
                    socket.close();
                }
                catch(Exception e1) {
                    dLog.error("Exception in closing socket", e1);
                }
            }
        }
        catch(Exception e) {
            dLog.error("Exception in SocketManager run", e);
        }
    }

    public void setExit(boolean exit) {
        dLog.trace("setExit: " + exit);
        this.exit = exit;
    }
    
    public boolean getExit() {
    	return this.exit;
    }
    
    /**
     * Append some additional information to info messages to help better track requests as they come across the UI
     * @param msg
     * @return
     */
    private String appendInfo(String msg) {
    	return new Date() + " | " + SOCKET_PORT + " | " + msg;
    }
}
