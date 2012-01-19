/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.burritopos.server.domain;

import java.io.Serializable;
//import org.apache.log4j.*;
//import java.util.Date;

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
	protected String username;
        protected String password;

        public User() {
            super();
        }

        public User(Integer id, String username, String password) {
            super();
            this.id = id;
            this.username = username;
            this.password = password;
        }

        public boolean validate() {
            if(this.id != null || this.username != null || this.password != null)
                return true;
            else
                return false;
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
	    if (this.id != other.id || this.username != other.username || this.password != other.password)
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
