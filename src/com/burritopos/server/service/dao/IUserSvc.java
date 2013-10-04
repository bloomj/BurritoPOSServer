/**
 * 
 */
package com.burritopos.server.service.dao;

import java.util.List;

import com.burritopos.server.domain.User;
import com.burritopos.server.service.IService;

/**
 * @author james.bloom
 *
 */
public interface IUserSvc extends IService {
	public final String NAME = "IUserSvc";
	
	public boolean storeUser(User u) throws Exception;
	public User getUser(Integer id) throws Exception;
	public User getUser(String userName) throws Exception;
	public List<User> getUsers(Integer groupid) throws Exception;
	public boolean deleteUser(Integer id) throws Exception;
	public List<User> getAllUsers() throws Exception;
}
