package com.userms.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.userms.dto.UserPrincipal;
import com.userms.entity.MyUser;
import com.userms.repository.UserRepository;


/**
 * Service Class for UserDetails
 * <p>
 * This class provides the implementation for UserDetailsService Interface which helps in validating UserDetails for authentication
 * </p>
 *
 */
@Service
public class MyUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Optional<MyUser> optional = userRepo.findByUsername(username);
		
		if(optional.isEmpty()) {
			System.out.println("User not found!");
			throw new UsernameNotFoundException("User not found");
		}
		
		return new UserPrincipal(optional.get());
	}
	

}
