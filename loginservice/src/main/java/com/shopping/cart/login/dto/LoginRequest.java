package com.shopping.cart.login.dto;

import java.io.Serializable;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3131032576361031294L;
	
	@JsonProperty(required=true)
	private String email;
	
	@JsonProperty(required=true)
	private String password;
	
	private Set<String> roles;
}
