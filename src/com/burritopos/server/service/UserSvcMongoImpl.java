/**
 * 
 */
package com.burritopos.server.service;

import java.util.Date;

import org.apache.log4j.Logger;

import com.burritopos.server.domain.User;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.mongodb.WriteResult;

/**
 * @author james.bloom
 *
 */
public class UserSvcMongoImpl implements IUserSvc {

	private static Logger dLog = Logger.getLogger(UserSvcMongoImpl.class);
	
	/* (non-Javadoc)
	 * @see com.burritopos.server.service.IUserSvc#storeUser(com.burritopos.server.domain.User)
	 */
	@Override
	public boolean storeUser(User u) throws Exception {
		dLog.info(new Date() + " | Entering method storeUser | Employee ID: "+u.getId());
		boolean result = false;
		
		try {
			Mongo m = new Mongo();
	        DB db = m.getDB("neatoBurrito");
	        DBCollection coll = db.getCollection("user");
	        
	        BasicDBObject query = new BasicDBObject();
	        query.put("id", u.getId());
	        
	        dLog.trace(new Date() + " | Finding if user exists");
	        BasicDBObject myDoc = (BasicDBObject) coll.findOne(query);
	        
	        query.put("password", u.getPassword());
	        query.put("userName", u.getUserName());

	        //ensure we were passed a valid object before attempting to write
	        dLog.trace(new Date() + " | myDoc: " + myDoc);
            if(myDoc == null) {
            	dLog.trace(new Date() + " | Inserting User");
            	coll.insert(query);
            	
            	result = true;
            }
            else {
            	dLog.trace(new Date() + " | Updating User");
            	WriteResult wr = coll.update(new BasicDBObject().append("id", u.getId()), query);
            	
            	dLog.trace(new Date() + " | WriteResult: " + wr.getN());
    	        if(wr.getError() == null && wr.getN() == 1) {
    	        	result = true;
    	        }
    	        else {
    	        	dLog.trace(new Date() + " | WriteResult: " + wr.getError());
    	        }
            }
		}
		catch(Exception e) {
			dLog.error(new Date() + " | Exception in storeUser: "+e.getMessage());
			result = false;
		}
		
		return result;
	}

	/* (non-Javadoc)
	 * @see com.burritopos.server.service.IUserSvc#getUser(java.lang.Integer)
	 */
	@Override
	public User getUser(Integer id) throws Exception {
		dLog.info(new Date() + " | Entering method getEmployee | Employee ID: "+id);
		User u = new User();
		
		try {
			Mongo m = new Mongo();
	        DB db = m.getDB("neatoBurrito");
	        DBCollection coll = db.getCollection("user");
	        
	        BasicDBObject query = new BasicDBObject();
	        query.put("id", id);
	        
	        BasicDBObject myDoc = (BasicDBObject) coll.findOne(query);

	        //ensure we were passed a valid object before attempting to write
	        dLog.trace(new Date() + " | myDoc: " + myDoc);
            if(myDoc != null) {
                u.setId(id);
                u.setUserName(myDoc.getString("userName"));
                u.setPassword(myDoc.getString("password"));
            }
            dLog.trace(new Date() + " | Finished setting user");
		}
		catch(Exception e1) {
			dLog.error(new Date() + " | Exception in getUser: "+e1.getMessage());
		}

		return u;
	}

	/* (non-Javadoc)
	 * @see com.burritopos.server.service.IUserSvc#deleteUser(java.lang.Integer)
	 */
	@Override
	public boolean deleteUser(Integer id) throws Exception {
		dLog.info(new Date() + " | Entering method deleteUser | Employee ID:"+id);
		boolean result = false;
		
		try {
			Mongo m = new Mongo();
	        DB db = m.getDB("neatoBurrito");
	        DBCollection coll = db.getCollection("user");
	        
	        BasicDBObject query = new BasicDBObject();
	        query.put("id", id);
	        
	        WriteResult wr = coll.remove(query);
	        
	        dLog.trace(new Date() + " | WriteResult: " + wr.getN());
	        if(wr.getN() == 1) {
	        	result = true;
	        }
	        else {
	        	dLog.trace(new Date() + " | WriteResult: " + wr.getError());
	        }
		}
		catch(Exception e) {
			dLog.error(new Date() + " | Exception in deleteUser: "+e.getMessage());
			result = false;
		}

		return result;
	}

}
