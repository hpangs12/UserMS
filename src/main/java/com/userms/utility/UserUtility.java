package com.userms.utility;

import java.util.List;

import com.userms.dto.UserDTO;
import com.userms.entity.MyUser;

/**
 * Utility class for converting between {@link MyUser} entity objects and {@link UserDTO} data transfer objects.
 * <p>
 * This class provides static methods to facilitate transformation between persistence-layer entities and
 * presentation-layer DTOs, ensuring separation of concerns and clean data exposure.
 * </p>
 *
 * <p><strong>Usage:</strong></p>
 * <pre>{@code
 * UserDTO dto = UserUtility.entityToDto(myUser);
 * MyUser entity = UserUtility.dtoToEntity(userDTO);
 * }</pre>
 *
 * <p>Note: This class assumes that {@code MyUser} and {@code UserDTO} have compatible fields such as
 * {@code id}, {@code username}, {@code email}, etc.
 */
public class UserUtility {

	public static UserDTO entityToDto(MyUser user) {

		UserDTO userDTO = new UserDTO();
		
		userDTO.setUsername(user.getUsername());
		userDTO.setEmailId(user.getEmailId());
		userDTO.setFirstName(user.getFirstName());
		userDTO.setLastName(user.getLastName());
		userDTO.setPassword("");
		userDTO.setAddress(user.getAddress() == null ? List.of():user.getAddress());
		
		return userDTO;
	}
	
	public static MyUser dtoToEntity(UserDTO userDTO) {

		MyUser user = new MyUser();
		
		user.setUsername(userDTO.getUsername());
		user.setEmailId(userDTO.getEmailId());
		user.setFirstName(userDTO.getFirstName());
		user.setLastName(userDTO.getLastName());
		user.setPassword(userDTO.getPassword());
		
		return user;
	}

}
