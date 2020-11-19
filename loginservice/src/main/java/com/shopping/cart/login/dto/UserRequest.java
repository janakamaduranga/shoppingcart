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
public class UserRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6780322413879533836L;

	@JsonProperty(required = true)
	private String email;

	@JsonProperty(required = true)
	private String password;

	private String mobile;

	private String address;
	
	private Set<String> roles;
}
