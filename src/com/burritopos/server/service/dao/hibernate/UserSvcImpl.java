/**
 * 
 */
package com.burritopos.server.service.dao.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.*;
import org.apache.log4j.*;

import com.burritopos.server.domain.User;
import com.burritopos.server.service.dao.IUserSvc;

/**
 * @author james.bloom
 *
 */
public class UserSvcImpl extends BaseSvcImpl implements IUserSvc {
	private static Logger dLog = Logger.getLogger(UserSvcImpl.class);

	@Override
	public User getUser(Integer id) throws Exception {
		dLog.info("Entering method getUser | User ID: "+id);
		User u = null;
		Session session = null;

		try {
			session = getSession();
			Transaction tranx = session.beginTransaction();
			u = (User)session.get(User.class, id);
			tranx.commit();
		} 
		catch(Exception e) {
			dLog.error("Exception in getUser", e);
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
	public User getUser(String userName) throws Exception {
		dLog.info("Entering method getUser | User name: "+userName);
		User u = null;
		Session session = null;

		try {
			session = getSession();
			Transaction tranx = session.beginTransaction();
			u = (User)session.get(User.class, userName);
			tranx.commit();
		} 
		catch(Exception e) {
			dLog.error("Exception in getUser", e);
		}
		finally {
			//ensure that session is close regardless of the errors in try/catch
			if(session != null) {
				session.close();
			}
		}

		return u;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<User> getUsers(Integer groupid) throws Exception {
		dLog.info("Entering method getUser | Group ID: "+groupid);
		List<User> users = new ArrayList<User>();
		Session session = null;

		try {
			session = getSession();
			String hql = "from User where groupid = " + groupid.toString();
			users = session.createQuery(hql).list();
		} 
		catch(Exception e) {
			dLog.error("Exception in getUser", e);
		}
		finally {
			//ensure that session is close regardless of the errors in try/catch
			if(session != null) {
				session.close();
			}
		}

		return users;
	}

	@Override
	public boolean storeUser(User u) throws Exception {
		dLog.info("Entering method storeUser | User ID: "+u.getId());
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
			dLog.error("Exception in storeUser", e);
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
	public boolean deleteUser(Integer id) throws Exception {
		dLog.info("Entering method deleteUser | User ID:"+id);
		boolean result = false;
		Session session = null;

		try {
			session = getSession();
			String hql = "delete from User where id = " + id.toString();
			Query query = session.createQuery(hql);
			int row = query.executeUpdate();

			if (row > 0) {
				result = true;
			}
		}
		catch(Exception e) {
			dLog.error("Exception in deleteUser", e);
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
	public ArrayList<User> getAllUsers() throws Exception {
		dLog.info("Entering method getAllUsers");
		ArrayList<User> result = new ArrayList<User>();
		Session session = null;

		try {
			session = getSession();
			session.clear();
			Transaction tranx = session.beginTransaction();
			
			String hql = "select from User";
			Query query = session.createQuery(hql);
			List<?> list = query.list();
			
			for(int n=0; n<list.size(); n++){
				result.add((User)list.get(n));
			}
			
			tranx.commit();
			session.flush();
			session.evict(User.class);
		} 
		catch(Exception e) {
			dLog.error("Exception in getAllUsers", e);
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
