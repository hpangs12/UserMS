package com.userms.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.userms.entity.MyUser;

/**
 * Repository Class for User
 * <p>
 * This repository class facilitates ORM operations with the Database
 * </p>
 *
 */
public interface UserRepository extends JpaRepository<MyUser, Integer>{

	Optional<MyUser> findByUsername(String username);
	
}
