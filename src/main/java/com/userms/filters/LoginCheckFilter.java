package com.userms.filters;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.userms.service.JwtService;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Filter for Checking User Login (JWT)
 * <p>
 * This filter class checks for the user login using the JWT token provided 
 * </p>
 *
 */
@Component
public class LoginCheckFilter extends OncePerRequestFilter{
	
	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private ApplicationContext context;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String path = request.getServletPath();
		if (path.equals("/users/login") || path.equals("/users/login/activate") || path.equals("/users/register") || path.equals("/users/auth/validate")) {
		    filterChain.doFilter(request, response); // skip JWT check
		    return;
		}
		
		String authHeader = request.getHeader("Authorization");
		String token = null;
		String username = null;
		try {
			
			if (authHeader == null || !authHeader.startsWith("Bearer ")) {
		        // No token provided
		        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		        response.setContentType("application/json");
		        response.getWriter().write("{\"error\": \"JWT token is missing\"}");
		        return; // stop filter chain
		    }else{
				token = authHeader.substring(7);
				username = jwtService.extractUsername(token);
			}
			
			if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null) {
				UserDetails userDetails = context.getBean(UserDetailsService.class).loadUserByUsername(username);
				
				if(jwtService.validateToken(token, userDetails)) {
					UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
					authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authToken);
				}
			}
		}
		 catch (ExpiredJwtException ex) {
			    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			    response.setContentType("application/json");
			    response.getWriter().write("{\"error\":\"JWT token has expired\"}");
			    return; // stop further filter chain
			} catch (Exception ex) {
			    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			    response.setContentType("application/json");
			    response.getWriter().write("{\"error\":\"Invalid JWT token\"}");
			    return;
			}
		
		filterChain.doFilter(request, response);
		
	}

}
