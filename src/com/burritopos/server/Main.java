/*
 * 
 * Copyright (c) 2011, James Bloom
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer 
 *   in the documentation and/or other materials provided with the distribution.
 *  
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, 
 *  THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR 
 *  CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, 
 *  PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF 
 *  LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS 
 *  SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.burritopos.server;

import org.apache.log4j.*;
//import com.burritopos.server.presentation.MainUI;
import com.burritopos.server.presentation.StatusUI;

import java.util.Date;
//import com.burritopos.server.business.ConnectionManager;

/**
 *
 * @author james.bloom
 */
public class Main {

    private static Logger dLog = Logger.getLogger(Main.class);

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
		try {
			String propertiesFile = "log4j.properties";
            PropertyConfigurator.configure(propertiesFile);

            dLog.info(new Date() + " | Starting Burrito POS Server execution");

            //main entry point into our server here
            //ConnectionManager neatoServer = new ConnectionManager();
            //neatoServer.start();
            
            // new entry point
            StatusUI statUI = new StatusUI();
            statUI.setBounds(0, 0, 500, 500);
			statUI.setVisible(true);
            //MainUI mainUI = new MainUI();
         	//mainUI.setBounds(0, 0, 500, 500);
         	//mainUI.setVisible(true);

            dLog.info(new Date() + " | Finishing Burrito POS Server execution");
		}
		catch(Exception e) {
			dLog.error(new Date() + " | Exception in Main: "+e.getMessage());
		}
    }
}
