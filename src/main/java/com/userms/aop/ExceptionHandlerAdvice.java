package com.userms.aop;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.userms.exception.InvalidPasswordException;
import com.userms.exception.UsernameNotUniqueException;

import io.jsonwebtoken.ExpiredJwtException;


/**
 * Controller Advice for Centralized Exception Handling
 * <p>
 * This class provides centralized exception handling to ensure the user gets a proper output for every request.
 * </p>
 *
 */
@RestControllerAdvice
public class ExceptionHandlerAdvice {

	@ExceptionHandler(InvalidPasswordException.class)
	public ResponseEntity<?> invalidPasswordException(InvalidPasswordException ex){
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(UsernameNotUniqueException.class)
	public ResponseEntity<?> usernameNotUniqueException(UsernameNotUniqueException ex){
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
	}
	

    @ExceptionHandler({BadCredentialsException.class, UsernameNotFoundException.class})
    public ResponseEntity<?> handleBadCredentials(Exception ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                             .body(Map.of("error", "Invalid username or password"));
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<?> handleDisabled(DisabledException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                             .body(Map.of("error", "This account is deactivated. Please log in to activate it again."));
    }
    
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<?> handleExpiredJwt(ExpiredJwtException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                             .body(Map.of("error", "The token has expired. Please log in again."));
    }
    
    @ExceptionHandler(LockedException.class)
    public ResponseEntity<?> handleLocked(LockedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                             .body(Map.of("error", "This user has been banned. Please contact support @ admin@gmail.com."));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleOtherExceptions(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body(Map.of("error", ex.getMessage()));
    }
}
