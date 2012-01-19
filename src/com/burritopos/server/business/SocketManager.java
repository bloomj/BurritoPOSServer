/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.burritopos.server.business;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import org.apache.log4j.*;
import java.util.Date;
import com.burritopos.server.domain.User;
import com.burritopos.server.presentation.StatusUI;

/**
 *
 * @author james.bloom
 */
public class SocketManager extends Thread {

    private static Logger dLog = Logger.getLogger(SocketManager.class);
    private Socket socket = null;
    private ObjectInputStream in = null;
    private ObjectOutputStream out = null;
    private boolean exit = false;
    private boolean auth = false;
    private User tUser;
    private ArrayList<User> users;
    //private StatusUI par;

    public SocketManager (Socket socket) {
    	this.socket = socket;

        users = new ArrayList<User>();
        users.add(new User(1,"Jim","password"));
        tUser = new User();
    }
    
    public SocketManager (Socket socket, StatusUI parVal) {
    	this(socket);
    	//this.par = parVal;
    }

    @Override
    public void run() {
        try {
            while (!exit) {
                out = new ObjectOutputStream(socket.getOutputStream());
                in  = new ObjectInputStream(socket.getInputStream());

                String inputStr = "";
                out.writeObject("Burrito POS Server Connected. Enter Username: ");
                //par.updateStatus("OUTPUT | Burrito POS Server Connected. Enter Username: ");
                dLog.trace(new Date() + " | OUTPUT | Burrito POS Server Connected. Enter Username: ");
                inputStr = (String)in.readObject();
                dLog.trace(new Date() + " | INPUT | " + inputStr);
                //par.updateStatus("INPUT | " + inputStr);

                while(!inputStr.equals("exit") && !this.exit) {

                    if(tUser.getUserName() == null) {
                        tUser.setUserName(inputStr);
                        out.writeObject("OK User " + inputStr + ", enter password: ");
                        dLog.trace(new Date() + " | OUTPUT | OK User " + inputStr + ", enter password: ");
                    }
                    else if(tUser.getPassword() == null) {
                        dLog.trace(new Date() + " | Username: " + tUser.getUserName() + " | Password: " + inputStr);
                        for(int n=0; n<users.size(); n++) {
                        	dLog.trace(new Date() + " | stored user: " + users.get(n).getUserName() + " | stored pass: " + users.get(n).getPassword());
                            if(((User)users.get(n)).getUserName().equals(tUser.getUserName()) && ((User)users.get(n)).getPassword().equals(inputStr)) {
                                //set password
                                tUser.setPassword(inputStr);
                                break;
                            }
                        }

                        if(tUser.getPassword() != null) {
                            out.writeObject("OK User verified. Enter command: ");
                            dLog.trace(new Date() + " | OUTPUT | OK User verified. Enter command: ");
                            auth = true;
                        }
                        else {
                            tUser.setUserName(null);
                            tUser.setPassword(null);
                            out.writeObject("ERROR Invalid Credentials. Enter Username: ");
                            dLog.trace(new Date() + " | OUTPUT | ERROR Invalid Credentials. Enter Username: ");
                        }
                    }
                    else if(auth) {
                        if(!inputStr.equals("exit")) {
                            out.writeObject("OK Command " + inputStr + " entered. Enter command: ");
                            dLog.trace(new Date() + " | OUTPUT | OK Command " + inputStr + " entered. Enter command: ");
                        }
                    }

                    inputStr = (String)in.readObject();
                    dLog.trace(new Date() + " | INPUT | " + inputStr);
                }

                exit = true;
                try {
                    auth = false;
                    out.writeObject("Exiting");
                    dLog.trace(new Date() + " | Exiting");
                    in.close();
                    out.close();
                    socket.close();
                }
                catch(Exception e1) {
                    dLog.error(new Date() + " | Exception in closing socket: "+e1.getMessage());
                }
            }
        }
        catch(Exception e) {
            dLog.error(new Date() + " | Exception in SocketManager run: "+e.getMessage());
        }
    }

    public void setExit(boolean exit) {
        dLog.trace(new Date() + " | setExit: " + exit);
        this.exit = exit;
    }
}
