package com.var.var.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.var.var.model.Role;
import com.var.var.model.UserRole;


public interface RoleDao extends JpaRepository<Role, Integer> {

	Role findByName(String name);
}
