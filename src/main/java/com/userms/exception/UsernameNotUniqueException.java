package com.userms.exception;


/**
 * Custom Exception Class for Duplicate Username
 * <p>
 * This class provides the custom exception for when the username is already present
 * </p>
 *
 */
public class UsernameNotUniqueException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UsernameNotUniqueException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UsernameNotUniqueException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public UsernameNotUniqueException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public UsernameNotUniqueException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public UsernameNotUniqueException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}	
	
	
	
}
