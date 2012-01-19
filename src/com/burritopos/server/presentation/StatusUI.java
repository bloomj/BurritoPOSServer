/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.burritopos.server.presentation;

import javax.swing.JFrame;
import java.awt.*;
import java.awt.event.*;
import java.util.Date;
import java.util.Vector;
import javax.swing.*;
import org.apache.log4j.*;
import com.burritopos.server.business.ConnectionManager;

/**
 *
 * @author james.bloom
 */
public class StatusUI extends JFrame  {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Logger dLog = Logger.getLogger(StatusUI.class);
    private JLabel statusLbl = new JLabel("Status: ");
    private JList statusMsgs = new JList();
    private Vector<String> statString = new Vector<String>();
    private JScrollPane statusPane = new JScrollPane();
    private JButton startBtn = new JButton("Start Server");
    private JButton stopBtn = new JButton("Stop Server");
    private ConnectionManager neatoServer;

    public StatusUI() {
		super("Burrito POS Server");

		startBtn.addActionListener (
			new ActionListener () {
				public void actionPerformed (ActionEvent event)
					{startBtnOnClick();}
			}
		);

		stopBtn.addActionListener (
			new ActionListener () {
				public void actionPerformed (ActionEvent event)
					{stopBtnOnClick();}
			}
		);

		//Initialize JList
		statString.add("Burrito POS Server UI Started 12312414");
		statusMsgs.setListData(statString);
		
        Container container = getContentPane();
		container.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.PAGE_START;
        Container tContainer = new Container();
		tContainer.setLayout(new FlowLayout());
        c.insets = new Insets(5,5,5,5);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		tContainer = new Container();
		tContainer.setLayout(new FlowLayout());
		stopBtn.setEnabled(false);
		tContainer.add(startBtn);
		tContainer.add(stopBtn);
		container.add(tContainer, c);

        c.fill = GridBagConstraints.CENTER;
		c.insets = new Insets(0,0,0,0);
		c.gridx = 0;
		c.gridy = 1;
        tContainer = new Container();
		tContainer.setLayout(new FlowLayout());
		statusPane.getViewport().setView(statusMsgs);
		tContainer.add(statusLbl);
        tContainer.add(statusPane);
        container.add(tContainer, c);
        
        this.addWindowListener(new FrameListener(this));
   }

   private void startBtnOnClick() {
		dLog.trace(new Date() + " | Start Server button has been clicked");

		try {
			if(neatoServer == null || !neatoServer.isAlive())
				neatoServer = new ConnectionManager();
			neatoServer.start();
			startBtn.setEnabled(false);
        	stopBtn.setEnabled(true);
		}
		catch(Exception e) {
			dLog.error(new Date() + " | Exception in startBtnOnClick: "+e.getMessage());
		}
	}

	private void stopBtnOnClick() {
		dLog.trace(new Date() + " | Stop Button button has been clicked");

        try {
        	neatoServer.setExit(true);
            startBtn.setEnabled(true);
            stopBtn.setEnabled(false);
        }
        catch(Exception e) {
        	dLog.error(new Date() + " | Exception in stopBtnOnClick: "+e.getMessage());
        }
	}
	
	public void updateStatus(String newMsg) {
		dLog.trace(new Date() + " | Updating Status Messages");

        try {
        	statString.add(newMsg);
    		statusMsgs.setListData(statString);
        }
        catch(Exception e) {
        	dLog.error(new Date() + " | Exception in updateStatus: "+e.getMessage());
        }
	}
	
	public void stopServer() {
		dLog.trace(new Date() + " | Stopping server");
		
		try {
			neatoServer.setExit(true);
		}
        catch(Exception e) {
        	dLog.error(new Date() + " | Exception in stopServer: "+e.getMessage());
        }
	}

}

class FrameListener extends WindowAdapter
{
	private StatusUI par;
	
	public FrameListener(StatusUI statusUI) {
		this.par = statusUI;
	}

	public void windowClosing(WindowEvent e) {
		this.par.stopServer();
		
		System.exit(0);
	}
}
