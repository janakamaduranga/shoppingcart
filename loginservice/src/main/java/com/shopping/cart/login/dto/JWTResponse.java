package com.shopping.cart.login.dto;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JWTResponse implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2355297075135429455L;
	
	private String token;
	private String email;
	private List<String> roles;
}
