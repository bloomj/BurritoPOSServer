package com.burritopos.server.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
//import org.apache.log4j.*;
//import java.util.Date;
import java.util.Random;

/**
 *
 * @author james.bloom
 */
public class User implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//private static Logger dLog = Logger.getLogger(User.class);
	protected Integer id;
	protected List<Integer> groupid = new ArrayList<Integer>();
	protected String username;
	protected String password;
	protected static final Random rand = new Random();

	public User() {
		super();
		
		this.id = rand.nextInt();
	}

	public User(Integer id, Integer groupid, String username, String password) {
		super();
		this.id = id;
		this.groupid.add(groupid);
		this.username = username;
		this.password = password;
	}

	public boolean validate() {
		if(this.id == null || this.groupid == null || this.username == null || this.password == null)
			return false;
		else
			return true;
	}

	/**
	 * Checks if the objects are equal
	 * @return success or failure
	 */
	@Override
	public boolean equals(Object obj) {
		if(this == obj)
			return true;

		if(obj == null || getClass() != obj.getClass())
			return false;

		final User other = (User) obj;
		if (this.id != other.id || this.groupid != other.groupid || this.username != other.username || this.password != other.password)
			return false;

		return true;
	}

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	
	/**
	 * @return the groupid
	 */
	public List<Integer> getGroupId() {
		return groupid;
	}

	/**
	 * @param group id to add
	 */
	public boolean addGroupId(Integer groupid) {
		return this.groupid.add(groupid);
	}
	
	/**
	 * group id to remove
	 * @param groudid
	 */
	public boolean removeGroupId(Integer groudid) {
		return this.groupid.remove(groupid);
	}

        /**
	 * @return the username

	 */
	public String getUserName() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUserName(String username) {
		this.username = username;
	}

        /**
	 * @return the password

	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
}
