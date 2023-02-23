package com.xplaptop.admin.user;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.xplaptop.common.entity.user.Role;

@Repository
public interface RoleRepository extends CrudRepository<Role, Integer>{
	
}
