/**
 * 
 */
package com.burritopos.server.service.dao.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.*;
import org.apache.log4j.*;

import com.burritopos.server.domain.Group;
import com.burritopos.server.service.dao.IGroupSvc;

/**
 * @author james.bloom
 *
 */
public class GroupSvcImpl extends BaseSvcImpl implements IGroupSvc {
	private static Logger dLog = Logger.getLogger(GroupSvcImpl.class);

	@Override
	public Group getGroup(Integer id) throws Exception {
		dLog.info("Entering method getGroup | Group ID: "+id);
		Group u = null;
		Session session = null;

		try {
			session = getSession();
			Transaction tranx = session.beginTransaction();
			u = (Group)session.get(Group.class, id);
			tranx.commit();
		} 
		catch(Exception e) {
			dLog.error("Exception in getGroup", e);
		}
		finally {
			//ensure that session is close regardless of the errors in try/catch
			if(session != null) {
				session.close();
			}
		}

		return u;
	}
	
	@Override
	public Group getGroup(String groupName) throws Exception {
		dLog.info("Entering method getGroup | Group Name: "+groupName);
		Group u = null;
		Session session = null;

		try {
			session = getSession();
			Transaction tranx = session.beginTransaction();
			u = (Group)session.get(Group.class, groupName);
			tranx.commit();
		} 
		catch(Exception e) {
			dLog.error("Exception in getGroup", e);
		}
		finally {
			//ensure that session is close regardless of the errors in try/catch
			if(session != null) {
				session.close();
			}
		}

		return u;
	}

	@Override
	public boolean storeGroup(Group u) throws Exception {
		dLog.info("Entering method storeGroup | Group ID: "+u.getId());
		boolean result = false;
		Session session = null;

		try {
			//ensure we were passed a valid object before attempting to write
			if(u.validate()) {
				session = getSession();
				Transaction tranx = session.beginTransaction();
				session.save(u);
				tranx.commit();
				result = tranx.wasCommitted();
				dLog.info("Was committed: " + result);
			}
		} 
		catch(Exception e) {
			dLog.error("Exception in storeGroup", e);
		}
		finally {
			//ensure that session is close regardless of the errors in try/catch
			if(session != null) {
				session.close();
			}
		}

		return result;
	}

	@Override
	public boolean deleteGroup(Integer id) throws Exception {
		dLog.info("Entering method deleteGroup | Group ID:"+id);
		boolean result = false;
		Session session = null;

		try {
			session = getSession();
			String hql = "delete from Group where id = " + id.toString();
			Query query = session.createQuery(hql);
			int row = query.executeUpdate();

			if (row > 0) {
				result = true;
			}
		}
		catch(Exception e) {
			dLog.error("Exception in deleteGroup", e);
		}
		finally {
			//ensure that session is close regardless of the errors in try/catch
			if(session != null) {
				session.close();
			}
		}

		return result;
	}

	@Override
	public ArrayList<Group> getAllGroups() throws Exception {
		dLog.info("Entering method getAllGroups");
		ArrayList<Group> result = new ArrayList<Group>();
		Session session = null;

		try {
			session = getSession();
			session.clear();
			Transaction tranx = session.beginTransaction();
			
			String hql = "select from Group";
			Query query = session.createQuery(hql);
			List<?> list = query.list();
			
			for(int n=0; n<list.size(); n++){
				result.add((Group)list.get(n));
			}
			
			tranx.commit();
			session.flush();
			session.evict(Group.class);
		} 
		catch(Exception e) {
			dLog.error("Exception in getAllGroups", e);
		}
		finally {
			//ensure that session is close regardless of the errors in try/catch
			if(session != null) {
				session.close();
			}
		}

		return result;
	}
}
