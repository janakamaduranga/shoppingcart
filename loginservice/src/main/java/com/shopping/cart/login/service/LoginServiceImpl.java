package com.shopping.cart.login.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shopping.cart.login.dto.JWTResponse;
import com.shopping.cart.login.dto.LoginRequest;
import com.shopping.cart.login.dto.UserRequest;
import com.shopping.cart.login.entity.Role;
import com.shopping.cart.login.entity.User;
import com.shopping.cart.login.entity.util.ERole;
import com.shopping.cart.login.exception.ErrorCodes;
import com.shopping.cart.login.exception.UserLoginException;
import com.shopping.cart.login.repository.RoleRepository;
import com.shopping.cart.login.repository.UserRepository;
import com.shopping.cart.login.security.jwt.JwtUtils;

@Service
public class LoginServiceImpl implements LoginService {
	UserRepository userRepository;

	RoleRepository roleRepository;

	PasswordEncoder encoder;
	
	AuthenticationManager authenticationManager;
	
	JwtUtils jwtUtils;
	
	public LoginServiceImpl(UserRepository userRepository,
			RoleRepository roleRepository,
			PasswordEncoder encoder,
			AuthenticationManager authenticationManager,
			JwtUtils jwtUtils) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.encoder = encoder;
		this.authenticationManager = authenticationManager;
		this.jwtUtils = jwtUtils;
	}

	@Override
	public boolean createUser(UserRequest userRequest) {

		if (userRepository.existsByEmail(userRequest.getEmail()).booleanValue()) {
			throw new UserLoginException(ErrorCodes.USER_EXIST);
		}

		// Create new user's account
		User user = new User(userRequest.getEmail(), 
				encoder.encode(userRequest.getPassword()),
				userRequest.getMobile(),
				userRequest.getAddress());

		Set<String> strRoles = userRequest.getRoles();
		Set<Role> roles = new HashSet<>();

		if (strRoles == null) {
			Role userRole = roleRepository.findByName(ERole.ROLE_CUSTOMER)
					.orElseThrow(() -> new UserLoginException(ErrorCodes.ROLE_NOT_FOUND));
			roles.add(userRole);
		} else {
			strRoles.forEach(role -> {
				switch (role) {
				case "admin":
					Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
							.orElseThrow(() -> new UserLoginException(ErrorCodes.ROLE_NOT_FOUND));
					roles.add(adminRole);

					break;
				case "supplier":
					Role modRole = roleRepository.findByName(ERole.ROLE_SUPPLIER)
							.orElseThrow(() -> new UserLoginException(ErrorCodes.ROLE_NOT_FOUND));
					roles.add(modRole);

					break;
				default:
					Role userRole = roleRepository.findByName(ERole.ROLE_CUSTOMER)
							.orElseThrow(() -> new UserLoginException(ErrorCodes.ROLE_NOT_FOUND));
					roles.add(userRole);
				}
			});
		}

		user.setRoles(roles);
		try {
			userRepository.save(user);
		} catch (Exception e) {
			throw new UserLoginException(e, ErrorCodes.USER_CREATION_ERROR);
		}

		return true;
	}

	@Override
	public JWTResponse logInUser(LoginRequest loginRequest) {
		
		Authentication authentication = null;;
		try {
			authentication = authenticationManager.authenticate(
						new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
		} catch(AuthenticationException e) {
			throw new UserLoginException(e, ErrorCodes.INVALID_LOGIN);
		}
		

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);
		
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();		
		List<String> roles = userDetails.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.toList());
		return new JWTResponse(jwt, 
				 userDetails.getEmail(), 
				 roles);
	}
}
