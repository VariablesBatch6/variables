package com.var.var.service;

import java.util.List;
import java.util.Set;

import com.var.var.model.User;
import com.var.var.model.UserRole;



public interface UserService {
	
	User findByUsername(String username);
	User findByEmail(String email);
	
	boolean checkUserExists(String username, String email);
	boolean checkUsernameExists(String username);
	boolean checkEmailExists(String email);
	
	void save(User user);
	User createUser(User user, Set<UserRole> userRoles);
	User saveUser(User user);
	
	List<User> findUserList();

    void enableUser (String username);

    void disableUser (String username);
}
