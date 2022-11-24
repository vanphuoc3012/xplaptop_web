package com.xplaptop.admin.user;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.xplaptop.common.entity.User;

@Repository
public interface UserRepository extends CrudRepository<User, Integer>{

}
