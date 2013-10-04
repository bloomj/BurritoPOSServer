/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.burritopos.server.presentation;

import javax.swing.JFrame;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import javax.swing.*;
import org.apache.log4j.*;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.burritopos.server.business.IConnectionManager;

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
	private JList<String> statusMsgs = new JList<String>();
	private Vector<String> statString = new Vector<String>();
	private JScrollPane statusPane = new JScrollPane();
	private JButton startBtn = new JButton("Start Server");
	private JButton stopBtn = new JButton("Stop Server");
	private IConnectionManager neatoServer;
	private Thread neatoServerThread;

	// Spring configuration
	private static final String SPRING_CONFIG_DEFAULT = "applicationContext.xml";

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
		statString.add("Burrito POS Server UI Started");
		statusMsgs.setListData(statString);

		Container container = getContentPane();
		container.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		//c.anchor = GridBagConstraints.PAGE_START;
		Container tContainer = new Container();
		tContainer.setLayout(new FlowLayout());
		c.fill = GridBagConstraints.CENTER;
		c.gridx = 0;
		c.gridy = 0;
		stopBtn.setEnabled(false);
		tContainer.add(startBtn);
		tContainer.add(stopBtn);
		container.add(tContainer, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(0,0,0,0);
		c.gridx = 0;
		c.gridy = 1;
		//c.ipady = 300;
		//c.ipadx = 500;
		tContainer = new Container();
		tContainer.setLayout(new FlowLayout());
		statusMsgs.setPreferredSize(new Dimension(500, 400));
		statusPane.getViewport().setView(statusMsgs);
		statusPane.setPreferredSize(new Dimension(500, 400));
		tContainer.add(statusLbl);
		tContainer.add(statusPane);
		container.add(tContainer, c);

		this.addWindowListener(new FrameListener(this));
	}

	private void startBtnOnClick() {
		dLog.trace("Start Server button has been clicked");

		try {
			if(neatoServer == null) {
				//Spring Framework IoC
				ClassPathXmlApplicationContext beanfactory = null;
				try {
					beanfactory = new ClassPathXmlApplicationContext(SPRING_CONFIG_DEFAULT);
					neatoServer = (IConnectionManager)beanfactory.getBean("secureConnectionManager");

				} catch (Exception e) {
					dLog.error("Unable to configure Spring beans", e);
				} finally {
					if (beanfactory != null) {
						beanfactory.close();
					}
				}

				// TODO: figure out why Spring isn't setting the singleton properly
				neatoServer.setParent(this);
			}

			neatoServerThread = new Thread(neatoServer);
			neatoServerThread.start();

			startBtn.setEnabled(false);
			stopBtn.setEnabled(true);
		}
		catch(Exception e) {
			dLog.error("Exception in startBtnOnClick", e);
		}
	}

	private void stopBtnOnClick() {
		dLog.trace("Stop Button button has been clicked");

		try {
			stopServer();
			startBtn.setEnabled(true);
			stopBtn.setEnabled(false);
		}
		catch(Exception e) {
			dLog.error("Exception in stopBtnOnClick", e);
		}
	}

	// invoke status updates via SwingUtilities.invokeLater so it's put on the EDT
	public void updateStatus(final String newMsg) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				dLog.trace("Updating Status Messages: " + newMsg);

				try {
					statString.add(newMsg);
					statusMsgs.setListData(statString);

					statusMsgs.repaint();
				}
				catch(Exception e) {
					dLog.error("Exception in updateStatus", e);
				}
			}
		});
	}

	public void stopServer() {
		dLog.trace("Stopping server");

		try {
			if(neatoServer != null) {
				neatoServer.setExit(true);
			}

			if(neatoServerThread != null && neatoServerThread.isAlive()) {
				neatoServerThread.join();
			}
		}
		catch(Exception e) {
			dLog.error("Exception in stopServer", e);
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
