package com.burritopos.server.business;

import com.burritopos.server.presentation.StatusUI;

public interface IConnectionManager extends Runnable {
	public boolean getExit();
	public void run();
	public void setExit(boolean exit);
	public void setParent(StatusUI statUI);
}
