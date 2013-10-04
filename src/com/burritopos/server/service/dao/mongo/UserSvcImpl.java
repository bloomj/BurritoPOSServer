/**
 * 
 */
package com.burritopos.server.service.dao.mongo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.burritopos.server.domain.User;
import com.burritopos.server.service.dao.IUserSvc;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.WriteResult;

/**
 * @author james.bloom
 *
 */
public class UserSvcImpl extends BaseSvcImpl implements IUserSvc {
	private static Logger dLog = Logger.getLogger(UserSvcImpl.class);
	protected static final String USER_COLLECTION = "user";
	
	/**
	 * Standard Constructor
	 * @throws IOException
	 */
	public UserSvcImpl() throws IOException {
    	super();
	}
	
	/**
	 * Constructor for Spring
	 * @param mongodb
	 */
	public UserSvcImpl(String mongoip, String mongodb) {
		super(mongoip, mongodb);
	}
	
	/* (non-Javadoc)
	 * @see com.burritopos.server.service.IUserSvc#storeUser(com.burritopos.server.domain.User)
	 */
	@Override
	public boolean storeUser(User u) throws Exception {
		dLog.info("Entering method storeUser | User ID: "+u.getId());
		boolean result = false;

		DBCollection coll = getCollection(USER_COLLECTION);

		BasicDBObject query = new BasicDBObject();
		query.put("id", u.getId());

		dLog.trace("Finding if user exists");
		BasicDBObject myDoc = (BasicDBObject) coll.findOne(query);

		BasicDBList groups = new BasicDBList();
		for(Integer i : u.getGroupId()){
			groups.add(i);
		}
		query.append("Groups", groups);
		
		query.put("password", u.getPassword());
		query.put("userName", u.getUserName());

		//ensure we were passed a valid object before attempting to write
		dLog.trace("myDoc: " + myDoc);
		if(myDoc == null) {
			dLog.trace("Inserting User");
			coll.insert(query);

			result = true;
		}
		else {
			dLog.trace("Updating User");
			WriteResult wr = coll.update(new BasicDBObject().append("id", u.getId()), query);

			dLog.trace("Write Result: " + wr.getN());
			if(wr.getError() == null && wr.getN() == 1) {
				result = true;
			}
			else {
				dLog.trace("Write Error: " + wr.getError());
			}
		}

		return result;
	}

	/* (non-Javadoc)
	 * @see com.burritopos.server.service.IUserSvc#getUser(java.lang.Integer)
	 */
	@Override
	public User getUser(Integer id) throws Exception {
		dLog.info("Entering method getUser | User ID: "+id);

		BasicDBObject query = new BasicDBObject();
		query.put("id", id);

		return initializeUser(findOne(query, USER_COLLECTION));
	}
	
	@Override
	public User getUser(String userName) throws Exception {
		dLog.info("Entering method getUser | User name: "+userName);

		BasicDBObject query = new BasicDBObject();
		query.put("userName", userName);

		return initializeUser(findOne(query, USER_COLLECTION));
	}
	
	@Override
	public List<User> getUsers(Integer groupid) throws Exception {
		dLog.info("Entering method getUser | Group ID: "+groupid);
		List<User> users = new ArrayList<User>();
		
		BasicDBObject query = new BasicDBObject();
		query.append("Groups", groupid);

		DBCursor results = findAll(query, USER_COLLECTION);
		while(results.hasNext()) {
			BasicDBObject user = (BasicDBObject) results.next();
			users.add(initializeUser(user));
		}
				
		return users;
	}

	/* (non-Javadoc)
	 * @see com.burritopos.server.service.IUserSvc#deleteUser(java.lang.Integer)
	 */
	@Override
	public boolean deleteUser(Integer id) throws Exception {
		dLog.info("Entering method deleteUser | User ID:"+id);
		boolean result = false;

		DBCollection coll = getCollection(USER_COLLECTION);

		BasicDBObject query = new BasicDBObject();
		query.put("id", id);

		WriteResult wr = coll.remove(query);

		dLog.trace("WriteResult: " + wr.getN());
		if(wr.getN() == 1) {
			result = true;
		}
		else {
			dLog.trace("Write Error: " + wr.getError());
		}

		return result;
	}

	//TODO: come back and reduce number of reads on DB
	@Override
	public ArrayList<User> getAllUsers() throws Exception {
		dLog.info("Entering method getAllUsers");
		ArrayList<User> result = new ArrayList<User>();

		DBCollection coll = getCollection(USER_COLLECTION);

		DBCursor cur = coll.find();

		//add this order 
		while(cur.hasNext()) {
			result.add(getUser(((BasicDBObject)cur.next()).getInt("id")));
		}

		return result;
	}

	/**
	 * Initializes a User object from a Mongo query
	 * @param myDoc
	 * @return
	 */
	private User initializeUser(BasicDBObject myDoc) {
		User u = new User();

		//ensure we were passed a valid object before attempting to write
		dLog.trace("myDoc: " + myDoc);
		if(myDoc != null) {
			u.setId(myDoc.getInt("id"));

			BasicDBList groups = (BasicDBList) myDoc.get("Groups");
			for(Object obj : groups) {
				u.addGroupId(Integer.parseInt(obj.toString()));
			}
			u.setUserName(myDoc.getString("userName"));
			u.setPassword(myDoc.getString("password"));
		}
		dLog.trace("Finished setting user");

		return u;
	}
	
}
