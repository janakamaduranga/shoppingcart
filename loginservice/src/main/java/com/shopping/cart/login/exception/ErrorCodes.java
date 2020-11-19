package com.shopping.cart.login.exception;

import lombok.Getter;

@Getter
public enum ErrorCodes {

	USER_EXIST(300, "User exist with the same mail"),
	ROLE_NOT_FOUND(301, "Role not found"),
	USER_NOT_FOUND(302, "User not found"),
	USER_CREATION_ERROR(303, "User creation error"),
	INVALID_LOGIN(304, "Invalid login");
	
	private int id;
	private String description;
	
	private ErrorCodes(int id, String description) {
		this.id = id;
		this.description = description;
	}
}
