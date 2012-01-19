/**
 * 
 */
package com.burritopos.server.service;

import com.burritopos.server.domain.User;


/**
 * @author james.bloom
 *
 */
public interface IUserSvc extends IService {
	public final String NAME = "IUserSvc";
	
	public boolean storeUser(User u) throws Exception;
	public User getUser(Integer id) throws Exception;
	public boolean deleteUser(Integer id) throws Exception;
}
