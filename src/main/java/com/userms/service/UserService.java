package com.userms.service;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.userms.dto.UserDTO;
import com.userms.exception.InvalidPasswordException;
import com.userms.exception.UsernameNotUniqueException;


/**
 * Interface for User Service
 * <p>
 * This interface declares all the public User Service methods which interface the Controller and Repository.
 * </p>
 *
 */
public interface UserService {

	ResponseEntity<?> verify(UserDTO user);

	UserDTO register(UserDTO user) throws UsernameNotUniqueException;

	Boolean validateToken(String token);

	UserDTO getUserByUsername(String username) throws UsernameNotFoundException;

	void deactivateOwnAccount(String username, String password) throws InvalidPasswordException;
	
	void reactivateOwnAccount(String username, String password) throws InvalidPasswordException;

	Boolean banAccount(String targetUsername);
	
	Boolean unbanAccount(String targetUsername);

	UserDTO createAdmin(UserDTO userDTO) throws UsernameNotUniqueException;

	void deleteOwnAccount(String username, String password) throws InvalidPasswordException;
}
