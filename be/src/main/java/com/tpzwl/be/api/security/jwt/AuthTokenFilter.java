package com.tpzwl.be.api.security.jwt;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.tpzwl.be.api.security.service.UserDetailsServiceImpl;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;

public class AuthTokenFilter extends OncePerRequestFilter {
	@Autowired
	private JwtUtils jwtUtils;

	@Autowired
	private UserDetailsServiceImpl userDetailsService;

	private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String jwt = parseJwt(request);
		if (jwt != null) {
			boolean validToken = false;
			try {
				validToken = jwtUtils.validateJwtToken(jwt);
		    } catch (MalformedJwtException e) {
		        logger.error("Invalid JWT token: {}", e.getMessage());
	            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	            PrintWriter writer = response.getWriter();
	            writer.println("Invalid JWT token");	
	            return;
			} catch (ExpiredJwtException e) {
				logger.error("JWT token is expired: {}", e.getMessage());
	            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	            PrintWriter writer = response.getWriter();
	            writer.println("JWT token is expired");
	            return;
			} catch (SignatureException e) {
				logger.error("JWT token signature is invalid: {}", e.getMessage());
	            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	            PrintWriter writer = response.getWriter();
	            writer.println("JWT token signature is invalid");
	            return;
			} catch (UnsupportedJwtException e) {
				logger.error("JWT token is unsupported: {}", e.getMessage());
	            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	            PrintWriter writer = response.getWriter();
	            writer.println("JWT token is unsupported");	
	            return;
			} catch (IllegalArgumentException e) {
				logger.error("JWT claims string is empty: {}", e.getMessage());
	            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	            PrintWriter writer = response.getWriter();
	            writer.println("JWT claims string is empty");
	            return;
			}
			if (validToken) {
				String username = jwtUtils.getUserNameFromJwtToken(jwt);
				UserDetails userDetails = userDetailsService.loadUserByUsername(username);
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authentication);				
			}
		}

		filterChain.doFilter(request, response);
	}

//  @Override
//  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//      throws ServletException, IOException {
//    try {
//      String jwt = parseJwt(request);
//      if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
//        String username = jwtUtils.getUserNameFromJwtToken(jwt);
//
//        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//        
//        UsernamePasswordAuthenticationToken authentication = 
//            new UsernamePasswordAuthenticationToken(userDetails,
//                                                    null,
//                                                    userDetails.getAuthorities());
//        
//        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//      }
//    } catch (Exception e) {
//      logger.error("Cannot set user authentication: {}", e);
//    }
//
//    filterChain.doFilter(request, response);
//  }

	private String parseJwt(HttpServletRequest request) {
		String jwt = jwtUtils.getJwtFromCookies(request);
		return jwt;
	}
}
