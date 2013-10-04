/**
 * 
 */
package com.burritopos.server.service.dao.mongo;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.burritopos.server.domain.Group;
import com.burritopos.server.service.dao.IGroupSvc;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.WriteResult;

/**
 * @author james.bloom
 *
 */
public class GroupSvcImpl extends BaseSvcImpl implements IGroupSvc {
	private static Logger dLog = Logger.getLogger(GroupSvcImpl.class);
	protected static final String GROUP_COLLECTION = "group";	
	
	/**
	 * Standard Constructor
	 * @throws IOException
	 */
	public GroupSvcImpl() throws IOException {
    	super();
	}
	
	/**
	 * Constructor for Spring
	 * @param mongodb
	 */
	public GroupSvcImpl(String mongoip, String mongodb) {
		super(mongoip, mongodb);
	}
	
	/* (non-Javadoc)
	 * @see com.burritopos.server.service.IGroupSvc#storeGroup(com.burritopos.server.domain.Group)
	 */
	@Override
	public boolean storeGroup(Group g) throws Exception {
		dLog.info("Entering method storeGroup | Group ID: "+g.getId());
		boolean result = false;

		DBCollection coll = getCollection(GROUP_COLLECTION);

		BasicDBObject query = new BasicDBObject();
		query.put("id", g.getId());

		dLog.trace("Finding if group exists");
		BasicDBObject myDoc = findOne(query, GROUP_COLLECTION);

		query.put("name", g.getName());

		//ensure we were passed a valid object before attempting to write
		dLog.trace("myDoc: " + myDoc);
		if(myDoc == null) {
			dLog.trace("Inserting Group: " + query.toString());
			coll.insert(query);

			result = true;
		}
		else {
			dLog.trace("Updating Group: " + query.toString());
			WriteResult wr = coll.update(myDoc, query);

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
	 * @see com.burritopos.server.service.IGroupSvc#getGroup(java.lang.Integer)
	 */
	@Override
	public Group getGroup(Integer id) throws Exception {
		dLog.info("Entering method getGroup | Group ID: "+id);

		BasicDBObject query = new BasicDBObject();
		query.put("id", id);

		return initializeGroup(findOne(query, GROUP_COLLECTION));
	}
	
	/* (non-Javadoc)
	 * @see com.burritopos.server.service.IGroupSvc#getGroup(java.lang.String)
	 */
	@Override
	public Group getGroup(String groupName) throws Exception {
		dLog.info("Entering method getGroup | Group name: "+groupName);

		BasicDBObject query = new BasicDBObject();
		query.put("name", groupName);

		return initializeGroup(findOne(query, GROUP_COLLECTION));
	}

	/* (non-Javadoc)
	 * @see com.burritopos.server.service.IGroupSvc#deleteGroup(java.lang.Integer)
	 */
	@Override
	public boolean deleteGroup(Integer id) throws Exception {
		dLog.info("Entering method deleteGroup | Employee ID:"+id);
		boolean result = false;

		DBCollection coll = getCollection(GROUP_COLLECTION);

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
	public ArrayList<Group> getAllGroups() throws Exception {
		dLog.info("Entering method getAllGroups");
		ArrayList<Group> result = new ArrayList<Group>();

		DBCollection coll = getCollection(GROUP_COLLECTION);

		DBCursor cur = coll.find();

		//add this group 
		while(cur.hasNext()) {
			result.add(getGroup(((BasicDBObject)cur.next()).getInt("id")));
		}

		return result;
	}

	/**
	 * Initializes a Group object from a Mongo query
	 * @param myDoc
	 * @return
	 */
	private Group initializeGroup(BasicDBObject myDoc) {
		Group g = new Group();

		//ensure we were passed a valid object before attempting to write
		dLog.trace("myDoc: " + myDoc);
		if(myDoc != null) {
			g.setId(myDoc.getInt("id"));
			g.setName(myDoc.getString("name"));
		}
		dLog.trace("Finished setting group");

		return g;
	}
}
