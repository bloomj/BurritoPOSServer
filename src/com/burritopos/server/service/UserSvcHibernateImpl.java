/**
 * 
 */
package com.burritopos.server.service;

import org.hibernate.*;
import org.apache.log4j.*;
import java.util.Date;

import com.burritopos.server.domain.User;

/**
 * @author james.bloom
 *
 */
public class UserSvcHibernateImpl extends BaseSvcHibernateImpl implements IUserSvc {

        private static Logger dLog = Logger.getLogger(UserSvcHibernateImpl.class);

	@Override
	public User getUser(Integer id) throws Exception {
		dLog.info(new Date() + " | Entering method getUser | User ID: "+id);
		User u = null;
                Session session = null;
		
		try {
                    session = getSession();
                    Transaction tranx = session.beginTransaction();
                    u = (User)session.get(User.class, id);
                    tranx.commit();
		} 
		catch(Exception e) {
			dLog.error(new Date() + " | Exception in getUser: "+e.getMessage());
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
	public boolean storeUser(User u) throws Exception {
		dLog.info(new Date() + " | Entering method storeUser | User ID: "+u.getId());
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
                            dLog.info(new Date() + " | Was committed: "+result);
			}
		} 
		catch(Exception e) {
			dLog.error(new Date() + " | Exception in storeUser: "+e.getMessage());
			result = false;
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
		dLog.info(new Date() + " | Entering method deleteUser | User ID:"+id);
		boolean result = false;
                Session session = null;
		
		try {
                    session = getSession();
                    String hql = "delete from User where id = " + id.toString();
                    Query query = session.createQuery(hql);
                    int row = query.executeUpdate();
                    
                    if (row == 0)
                        result = false;
                    else
                        result = true;
		}
		catch(Exception e) {
			dLog.error(new Date() + " | Exception in deleteUser: "+e.getMessage());
			result = false;
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
