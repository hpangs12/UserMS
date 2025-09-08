package com.userms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.userms.dto.UserDTO;
import com.userms.exception.InvalidPasswordException;
import com.userms.exception.UsernameNotUniqueException;
import com.userms.service.UserService;

/**
 * Controller for User Endpoints
 * <p>
 * This class provides request mappings for user and admin operations like login, register and maintaining the accounts.
 * </p>
 *
 */
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("users")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@PostMapping("register")
	public ResponseEntity<UserDTO> register(@RequestBody UserDTO userDTO) throws UsernameNotUniqueException {
		
		return new ResponseEntity<>(userService.register(userDTO), HttpStatus.CREATED);
	}
	
	@PostMapping("register/admin")
	public ResponseEntity<UserDTO> createAdmin(@RequestBody UserDTO userDTO) throws UsernameNotUniqueException {
		
		return new ResponseEntity<>(userService.createAdmin(userDTO), HttpStatus.CREATED);
	}
	
	@GetMapping("/{username}")
	public ResponseEntity<UserDTO> getUser(@PathVariable String username) throws UsernameNotFoundException {
		
		return new ResponseEntity<>(userService.getUserByUsername(username), HttpStatus.OK);
	}
	
	@GetMapping("/id/{userId}")
	public ResponseEntity<UserDTO> getUserById(@PathVariable Integer userId) throws UsernameNotFoundException {
		
		return new ResponseEntity<>(userService.getUserById(userId), HttpStatus.OK);
	}
	
	@PostMapping("login")
	public ResponseEntity<?> login(@RequestBody UserDTO user) {
		
		return userService.verify(user);
	}
	
	@PostMapping("/auth/validate")
	public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String authHeader) {
		
		String token = authHeader.substring(7);
		
		return new ResponseEntity<>(userService.validateToken(token), HttpStatus.OK);
	}
	
	@PostMapping("deactivate")
	public ResponseEntity<?> deactivate(@RequestBody UserDTO user) throws InvalidPasswordException {
		
		userService.deactivateOwnAccount(user.getUsername(), user.getPassword());
		
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		
	}
	
	@PostMapping("delete")
	public ResponseEntity<?> deleteAccount(@RequestBody UserDTO user) throws InvalidPasswordException {
		
		userService.deleteOwnAccount(user.getUsername(), user.getPassword());
		
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		
	}
	
	@PostMapping("login/activate")
	public ResponseEntity<?> reactivate(@RequestBody UserDTO user) throws InvalidPasswordException {
		
		userService.reactivateOwnAccount(user.getUsername(), user.getPassword());
		
		return userService.verify(user);
		
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("admin/ban/{username}")
	public ResponseEntity<?> banAccount(@PathVariable String username) 
			throws InvalidPasswordException {
		
		userService.banAccount(username);
		
		return new ResponseEntity<>(HttpStatus.NO_CONTENT); 
		
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("admin/unban/{username}")
	public ResponseEntity<?> unbanAccount(@PathVariable String username) 
			throws InvalidPasswordException {
		
		userService.unbanAccount(username);
		
		return new ResponseEntity<>(HttpStatus.NO_CONTENT); 
		
	}

}
