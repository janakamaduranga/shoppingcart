package com.shopping.cart.login.security.jwt;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.shopping.cart.login.entity.util.ERole;
import com.shopping.cart.login.service.UserDetailsImpl;

class JwtUtilsTest {
	JwtUtils jwtUtils = new JwtUtils("Test", 36000);
	
	@Test
	void generateJwtToken() {
		final String EMAIL = "test@gg.com";
		
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		SimpleGrantedAuthority adminAuthority = new SimpleGrantedAuthority(ERole.ROLE_ADMIN.name());
		authorities.add(adminAuthority);
		UserDetailsImpl userDetails = new UserDetailsImpl(200L, EMAIL, "12", authorities);
		UsernamePasswordAuthenticationToken authentication = 
				new UsernamePasswordAuthenticationToken(userDetails, adminAuthority);
		String token = jwtUtils.generateJwtToken(authentication);
		assertEquals(EMAIL, jwtUtils.getEmail(token));
		assertEquals(ERole.ROLE_ADMIN.name(), jwtUtils.getRoles(token).get(0));
	}

}
