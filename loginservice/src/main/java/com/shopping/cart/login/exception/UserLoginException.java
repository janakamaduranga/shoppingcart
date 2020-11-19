package com.shopping.cart.login.exception;

import lombok.Getter;

@Getter
public class UserLoginException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6527864018406515246L;
	private final ErrorCodes errorCodes;
	
	public UserLoginException(Exception e, ErrorCodes errorCodes) {
		super(errorCodes.getDescription(), e);
		this.errorCodes = errorCodes;
	}
	
	public UserLoginException(ErrorCodes errorCodes) {
		super(errorCodes.getDescription());
		this.errorCodes = errorCodes;
	}
}
