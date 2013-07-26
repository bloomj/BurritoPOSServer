/**
 * 
 */
package com.burritopos.server.presentation;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
//import java.util.UUID;
import org.apache.log4j.*;

/**
 * @author james.bloom
 *
 */
public class MainUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2404746632782793567L;
    private static Logger dLog = Logger.getLogger(MainUI.class);
	private JDesktopPane theDesktop = new JDesktopPane();
	private JMenuBar menubar = new JMenuBar();
	
	public MainUI() {
		super("Neato Burrito");

        dLog.trace("In MainUI: " + MainUI.class.getCanonicalName());
		
		this.setBounds(0, 0, 500, 500);
		Container container = getContentPane();
		container.add(theDesktop);
		setJMenuBar(menubar);
		JMenu fileMenu = new JMenu("File");
		JMenuItem exitItem = new JMenuItem("Exit");
		exitItem.setMnemonic('X');
		exitItem.addActionListener ( new ActionListener () {
				public void actionPerformed(ActionEvent e) {
					System.exit(0);
				}
			}
		);
		fileMenu.add(exitItem);
		menubar.add(fileMenu);

		StatusUI statUI = new StatusUI();
		statUI.setVisible(true);
		theDesktop.add(statUI);

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}

}
