package com.restapi.devicemanagement.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class AutheticationFilter extends OncePerRequestFilter {

	private final String secretKey = "mySecretKey";
	private final String tokenPrefix = "Bearer ";

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		try {
			if (checkJWTToken(request)) {
				Claims claims = validateToken(request);
				setUpSpringAuthentication(claims);
			} else {
				SecurityContextHolder.clearContext();
			}
			chain.doFilter(request, response);
		} catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException e) {
			log.error("Token validation failed !");
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			((HttpServletResponse) response).sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
			return;
		}
	}

	public String createToken(String username) {
		log.info("Token generating for the username {}", username);
		Map<String, Object> claim = new HashMap<String, Object>();

		String token = Jwts.builder().setClaims(claim).setSubject(username)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
				.signWith(SignatureAlgorithm.HS512, secretKey.getBytes()).compact();
		log.info("Token generated successfully for the username {}", username);
		return tokenPrefix + token;
	}

	private void setUpSpringAuthentication(Claims claims) {
		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(claims.getSubject(), null,
				new ArrayList<>());
		SecurityContextHolder.getContext().setAuthentication(auth);
	}

	private boolean checkJWTToken(HttpServletRequest request) {
		String authenticationHeader = request.getHeader("Authorization");
		if (authenticationHeader == null || !authenticationHeader.startsWith(tokenPrefix))
			return false;
		return true;
	}

	private Claims validateToken(HttpServletRequest request) {
		String jwtToken = request.getHeader("Authorization").replace(tokenPrefix, "");
		return Jwts.parser().setSigningKey(secretKey.getBytes()).parseClaimsJws(jwtToken).getBody();
	}
}
