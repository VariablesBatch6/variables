package com.var.var.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.var.var.model.User;



public interface UserDao extends JpaRepository<User, Long> {

	User findByUsername(String username);
	User findByEmail(String email);
	User findByPrimaryAccountId(Long PrimaryAccountID);
	User findBySavingsAccountId(Long SavingsAccountId);
	
	@Query(value="update user set first_name=? where username=?", nativeQuery = true)
	User updateFirstName(String firstName, String username);
	
	@Query(value="update user set last_name=? where username=?", nativeQuery = true)
	User updateLastName(String lastName, String username);
	
	@Query(value="update user set phone=? where username=?", nativeQuery = true)
	User updatePhone(String phone, String username);
	
}
