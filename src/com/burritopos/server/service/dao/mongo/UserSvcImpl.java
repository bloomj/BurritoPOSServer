/**
 * 
 */
package com.burritopos.server.service.dao.mongo;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.burritopos.server.domain.User;
import com.burritopos.server.service.dao.IUserSvc;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.Mongo;
import com.mongodb.WriteResult;

/**
 * @author james.bloom
 *
 */
public class UserSvcImpl implements IUserSvc {
	private static Logger dLog = Logger.getLogger(UserSvcImpl.class);
	
	/* (non-Javadoc)
	 * @see com.burritopos.server.service.IUserSvc#storeUser(com.burritopos.server.domain.User)
	 */
	@Override
	public boolean storeUser(User u) throws Exception {
		dLog.info("Entering method storeUser | User ID: "+u.getId());
		boolean result = false;

		Mongo m = new Mongo();
		DB db = m.getDB("neatoBurrito");
		DBCollection coll = db.getCollection("user");

		BasicDBObject query = new BasicDBObject();
		query.put("id", u.getId());

		dLog.trace("Finding if user exists");
		BasicDBObject myDoc = (BasicDBObject) coll.findOne(query);

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
		User u = new User();

		Mongo m = new Mongo();
		DB db = m.getDB("neatoBurrito");
		DBCollection coll = db.getCollection("user");

		BasicDBObject query = new BasicDBObject();
		query.put("id", id);

		BasicDBObject myDoc = (BasicDBObject) coll.findOne(query);

		//ensure we were passed a valid object before attempting to write
		dLog.trace("myDoc: " + myDoc);
		if(myDoc != null) {
			u.setId(id);
			u.setUserName(myDoc.getString("userName"));
			u.setPassword(myDoc.getString("password"));
		}
		dLog.trace("Finished setting user");

		return u;
	}

	/* (non-Javadoc)
	 * @see com.burritopos.server.service.IUserSvc#deleteUser(java.lang.Integer)
	 */
	@Override
	public boolean deleteUser(Integer id) throws Exception {
		dLog.info("Entering method deleteUser | Employee ID:"+id);
		boolean result = false;

		Mongo m = new Mongo();
		DB db = m.getDB("neatoBurrito");
		DBCollection coll = db.getCollection("user");

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

		Mongo m = new Mongo();
		DB db = m.getDB("neatoBurrito");
		DBCollection coll = db.getCollection("user");

		DBCursor cur = coll.find();

		//add this order 
		while(cur.hasNext()) {
			result.add(getUser(((BasicDBObject)cur.next()).getInt("id")));
		}

		return result;
	}

}
