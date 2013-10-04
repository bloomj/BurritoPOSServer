package com.burritopos.server.domain;

import java.io.Serializable;
//import org.apache.log4j.*;
//import java.util.Date;
import java.util.Random;

/**
 *
 * @author james.bloom
 */
public class Group implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//private static Logger dLog = Logger.getLogger(User.class);
	protected Integer id;
	protected String name;
	protected static final Random rand = new Random();

	public Group() {
		super();
		
		this.id = rand.nextInt();
	}

	public Group(Integer id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public boolean validate() {
		if(this.id == null || this.name == null)
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

		final Group other = (Group) obj;
		if (this.id != other.id || this.name != other.name)
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
	 * @return the name

	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
}
