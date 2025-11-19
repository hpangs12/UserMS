package com.userms.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.userms.entity.MyUser;
import com.userms.entity.UserStatus;

import jakarta.transaction.Transactional;

import com.userms.utility.UserUtility;
import com.userms.dto.UserDTO;
import com.userms.exception.InvalidPasswordException;
import com.userms.exception.UsernameNotUniqueException;
import com.userms.repository.UserRepository;

/**
 * Implementation Class for UserService
 * <p>
 * This class provides the implementations for the methods defined in the UserService Interface, with some extra utility methods.
 * </p>
 *
 */
@Service(value = "userService")
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserRepository repository;
	
	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private AuthenticationManager authManager;
	
	@Autowired
	private UserRepository userRepository;

	private static BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);
	
	@Override
	@Transactional
	public UserDTO register(UserDTO userDTO) throws UsernameNotUniqueException{
		
		Optional<MyUser> optional = repository.findByUsername(userDTO.getUsername());
		
		if(optional.isPresent()) {
			throw new UsernameNotUniqueException("Username already exists. Please try again with another username!");
		}
		
		MyUser user = UserUtility.dtoToEntity(userDTO);		
		Timestamp creationTime = Timestamp.valueOf(LocalDateTime.now());
		user.setRoles(new HashSet<>(Arrays.asList("ROLE_USER")));
		user.setPassword(new BCryptPasswordEncoder(12).encode(user.getPassword()));
		user.setCreatedAt(creationTime);
		user.setUpdatedAt(creationTime);
		if(user.getAddress() == null) user.setAddress(new ArrayList<>());
		
		return UserUtility.entityToDto(userRepository.save(user));
	}
	
	@Override
	public ResponseEntity<?> verify(UserDTO user) {
		
		authManager.authenticate(
				new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
	    );
	    
	    MyUser myUser = userRepository.findByUsername(user.getUsername()).get();
	    
	    if (myUser.getStatus()==UserStatus.DELETED) {
	    	return ResponseEntity.status(HttpStatus.FORBIDDEN).body("The account is scheduled for deletion on: " + myUser.getUpdatedAt().toLocalDateTime().plusDays(30)+". Please log in to cancel deletion.");
	    }
	    
		myUser.setLastLoginAt(LocalDateTime.now());
		userRepository.save(myUser);
		
	    return ResponseEntity.status(HttpStatus.OK).body(jwtService.generateToken(user.getUsername(), myUser.getRoles()));
		
	}

	@Override
	public Boolean validateToken(String token) {
		// TODO Auto-generated method stub
		return jwtService.validateToken(token, null);
	}
	
	@Override
	public UserDTO getUserByUsername(String username) {
		
		Optional<MyUser> optional = userRepository.findByUsername(username);
		
		if(optional.isEmpty()) {
			throw new UsernameNotFoundException("User not found!");
		}
		UserDTO userDto = UserUtility.entityToDto(optional.get());
		userDto.setUserId(optional.get().getId());
		return userDto;
	}
	
	@Override
	public UserDTO getUserById(Integer userId) {
		
		Optional<MyUser> optional = userRepository.findById(userId);
		
		if(optional.isEmpty()) {
			throw new UsernameNotFoundException("User not found!");
		}
		
		return UserUtility.entityToDto(optional.get());
	}
	
	
	@Override
	public void deactivateOwnAccount(String username, String password) throws InvalidPasswordException {
		
		authManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
			
		MyUser dbUser = userRepository.findByUsername(username).get();
		dbUser.setStatus(UserStatus.INACTIVE);
		dbUser.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
		userRepository.save(dbUser);
		
	}
	
	@Override
	public void reactivateOwnAccount(String username, String password) throws InvalidPasswordException {
		
		MyUser dbUser = userRepository.findByUsername(username)
		        .orElseThrow(() -> new UsernameNotFoundException("User not found"));

		    // Step 1: Verify password manually
		    if (!passwordEncoder.matches(password, dbUser.getPassword())) {
		        throw new InvalidPasswordException("Invalid password");
		    }

		    // Step 2: Reactivate account
		    dbUser.setStatus(UserStatus.ACTIVE);
		    dbUser.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
		    userRepository.save(dbUser);
		
	}
	
	@Override
	public void deleteOwnAccount(String username, String password) throws InvalidPasswordException {
		
		authManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
			
		MyUser dbUser = userRepository.findByUsername(username).get();
		dbUser.setStatus(UserStatus.DELETED);
		dbUser.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
		userRepository.save(dbUser);
		
	}
	
	@Override
	public Boolean banAccount(String targetUsername) {
		
	    MyUser target = userRepository.findByUsername(targetUsername)
	            .orElseThrow(() -> new UsernameNotFoundException("Target user not found"));
	    
	    target.setStatus(UserStatus.BANNED);
	    
	    userRepository.save(target);
	    
	    return true;
	}
	
	@Override
	public Boolean unbanAccount(String targetUsername) {
		
	    MyUser target = userRepository.findByUsername(targetUsername)
	            .orElseThrow(() -> new UsernameNotFoundException("Target user not found"));
		
	    target.setStatus(UserStatus.ACTIVE);
	    userRepository.save(target);

	    return true;
	}

	@Override
	public UserDTO createAdmin(UserDTO userDTO) throws UsernameNotUniqueException {


		Optional<MyUser> optional = repository.findByUsername(userDTO.getUsername());
		
		if(optional.isPresent()) {
			throw new UsernameNotUniqueException();
		}
		
		MyUser user = UserUtility.dtoToEntity(userDTO);		
		Timestamp creationTime = Timestamp.valueOf(LocalDateTime.now());
		user.setRoles(new HashSet<>(Arrays.asList("ROLE_USER", "ROLE_ADMIN")));
		user.setPassword(new BCryptPasswordEncoder(12).encode(user.getPassword()));
		user.setCreatedAt(creationTime);
		user.setUpdatedAt(creationTime);
		if(user.getAddress() == null) user.setAddress(new ArrayList<>());
		
		return UserUtility.entityToDto(userRepository.save(user));
	}
}
