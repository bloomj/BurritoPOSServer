/**
 * 
 */
package com.burritopos.server.service.dao.mongo;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

/**
 * @author james.bloom
 *
 */
public abstract class BaseSvcImpl {
	private static Logger dLog = Logger.getLogger(BaseSvcImpl.class);
	protected static String MONGO_IP;
	protected static String MONGO_DB;	
	
	/**
	 * Standard Constructor
	 * @throws IOException
	 */
	public BaseSvcImpl() throws IOException {
    	// manually load properties
        Properties propList = new Properties();
        dLog.trace("Loading burritopos.properties");

        propList.load(this.getClass().getClassLoader().getResourceAsStream("burritoposserver.properties"));
        MONGO_IP = propList.getProperty("mongo.ip");     
        dLog.trace("Got MONGO_IP value: " + MONGO_IP);
        MONGO_DB = propList.getProperty("mongo.databasename");     
        dLog.trace("Got MONGO_DB value: " + MONGO_DB);
        
	}
	
	/**
	 * Constructor for Spring
	 * @param mongodb
	 */
	public BaseSvcImpl(String mongoip, String mongodb) {
		MONGO_IP = mongoip;
		MONGO_DB = mongodb;
	}
	
	/**
	 * Access Mongo and get specified collection
	 * @param collection
	 * @return
	 * @throws UnknownHostException
	 * @throws MongoException
	 */
	protected DBCollection getCollection(String collection) throws UnknownHostException, MongoException {
		Mongo m = new Mongo(MONGO_IP);
		DB db = m.getDB(MONGO_DB);
		return db.getCollection(collection);
	}
	
	/**
	 * Finds group based on variable query parameters
	 * @param query
	 * @return
	 * @throws UnknownHostException
	 * @throws MongoException
	 */
	protected BasicDBObject findOne(BasicDBObject query, String collection) throws UnknownHostException, MongoException {
		dLog.trace("Entering method doQuery: " + query.toString());

		Mongo m = new Mongo(MONGO_IP);
		DB db = m.getDB(MONGO_DB);
		DBCollection coll = db.getCollection(collection);

		BasicDBObject myDoc = (BasicDBObject) coll.findOne(query);
		dLog.trace("Returned: " + myDoc);

		return myDoc;
	}
	
	/**
	 * Finds group based on variable query parameters
	 * @param query
	 * @return
	 * @throws UnknownHostException
	 * @throws MongoException
	 */
	protected DBCursor findAll(BasicDBObject query, String collection) throws UnknownHostException, MongoException {
		dLog.trace("Entering method doQuery: " + query.toString());

		Mongo m = new Mongo(MONGO_IP);
		DB db = m.getDB(MONGO_DB);
		DBCollection coll = db.getCollection(collection);

		return coll.find(query);
	}
}
