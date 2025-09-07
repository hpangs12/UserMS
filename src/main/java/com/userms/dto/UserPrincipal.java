package com.userms.dto;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.userms.entity.MyUser;
import com.userms.entity.UserStatus;


/**
 * Custom UserDetails Implementation
 * <p>
 * This class implements the UserDetails interface to facilitate the Authentication/Authorization.
 * </p>
 *
 */
public class UserPrincipal implements UserDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MyUser user;
	
	
	public UserPrincipal() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UserPrincipal(MyUser user) {
		super();
		this.user = user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		return user.getRoles().stream()
	            .map(SimpleGrantedAuthority::new)
	            .collect(Collectors.toSet());
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return user.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {

		return user.getStatus()!=UserStatus.BANNED;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		
		return user.getStatus()==UserStatus.ACTIVE;
	}

}
