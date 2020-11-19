package com.shopping.cart.login.service;

import com.shopping.cart.login.dto.JWTResponse;
import com.shopping.cart.login.dto.LoginRequest;
import com.shopping.cart.login.dto.UserRequest;

public interface LoginService {
	public boolean createUser(UserRequest loginRequest);
	public JWTResponse logInUser(LoginRequest loginRequest);
}
