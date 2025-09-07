package com.userms.exception;

/**
 * Custom Exception Class for Invalid Password
 * <p>
 * This class defines the Invalid Password Exception for when the user enters an invalid password
 * </p>
 *
 */
public class InvalidPasswordException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidPasswordException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public InvalidPasswordException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public InvalidPasswordException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public InvalidPasswordException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public InvalidPasswordException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
