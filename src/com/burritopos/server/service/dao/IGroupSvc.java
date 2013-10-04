/**
 * 
 */
package com.burritopos.server.service.dao;

import java.util.ArrayList;

import com.burritopos.server.domain.Group;
import com.burritopos.server.service.IService;


/**
 * @author james.bloom
 *
 */
public interface IGroupSvc extends IService {
	public final String NAME = "IGroupSvc";
	
	public boolean storeGroup(Group u) throws Exception;
	public Group getGroup(Integer id) throws Exception;
	public Group getGroup(String groupName) throws Exception;
	public boolean deleteGroup(Integer id) throws Exception;
	public ArrayList<Group> getAllGroups() throws Exception;
}
