package com.shopping.cart.login.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.shopping.cart.login.dto.JWTResponse;
import com.shopping.cart.login.dto.LoginRequest;
import com.shopping.cart.login.dto.UserRequest;
import com.shopping.cart.login.service.LoginService;

@RestController
@CrossOrigin(maxAge=3600)
public class LoginController {
	
	LoginService loginService;
	
	public LoginController(LoginService loginService) {
		this.loginService = loginService;
	}
	
	@PostMapping(path="/signIn")
	public ResponseEntity<JWTResponse> logInUser(@RequestBody LoginRequest loginRequest){

		return ResponseEntity.ok(loginService.logInUser(loginRequest));
	}
	
	@PostMapping("/signup")
	public ResponseEntity<String> registerUser(@RequestBody UserRequest loginRequest) {
		if(loginService.createUser(loginRequest)) {
			return ResponseEntity.ok("User created successfully");
		} else {
			return new ResponseEntity<>("User creation failure", HttpStatus.FAILED_DEPENDENCY);
		}
	}
	
	@GetMapping("/test")
	public ResponseEntity<String> test() {
			return ResponseEntity.ok("login test success");
		
	}

}
