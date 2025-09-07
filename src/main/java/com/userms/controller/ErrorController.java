package com.userms.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Internal Controller to handle Authentication/Authorization Exceptions
 * <p>
 * This controller handles the authorization and authentication exceptions while using Authentication Manager.
 * </p>
 *
 */
@RestController
@RequestMapping("/error")
public class ErrorController {

    @RequestMapping("/auth")
    public ResponseEntity<?> handleAuth(HttpServletRequest request) {
        Exception ex = (Exception) request.getAttribute("authEx");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                             .body(Map.of("error", ex.getMessage()));
    }

    @RequestMapping("/denied")
    public ResponseEntity<?> handleDenied(HttpServletRequest request) {
        Exception ex = (Exception) request.getAttribute("accessEx");
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                             .body(Map.of("error", ex.getMessage()));
    }
}
