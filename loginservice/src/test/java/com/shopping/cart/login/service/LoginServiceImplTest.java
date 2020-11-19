package com.shopping.cart.login.service;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.shopping.cart.login.dto.LoginRequest;
import com.shopping.cart.login.dto.UserRequest;
import com.shopping.cart.login.entity.Role;
import com.shopping.cart.login.entity.util.ERole;
import com.shopping.cart.login.exception.UserLoginException;
import com.shopping.cart.login.repository.RoleRepository;
import com.shopping.cart.login.repository.UserRepository;
import com.shopping.cart.login.security.jwt.JwtUtils;

@ExtendWith(MockitoExtension.class)
public class LoginServiceImplTest {

	@Mock
	UserRepository userRepository;

	@Mock
	RoleRepository roleRepository;

	@Mock
	PasswordEncoder encoder;
	
	@Mock
	AuthenticationManager authenticationManager;
	
	@Mock
	JwtUtils jwtUtils;

	UserRequest userRequest;
	LoginServiceImpl loginServiceImpl;

	@BeforeEach
	public void setUp() {
		userRequest = new UserRequest("test@gg.com", "123","","", new HashSet<String>(Arrays.asList("admin")));
		loginServiceImpl = new LoginServiceImpl(userRepository, roleRepository, encoder, authenticationManager, jwtUtils);
	}

	@Test
	void createUser_When_User_Exist_Exception() {
		when(userRepository.existsByEmail(anyString())).thenReturn(Boolean.TRUE);
		UserLoginException userNotFound = assertThrows(UserLoginException.class,
				() -> loginServiceImpl.createUser(userRequest));
		assertEquals("User exist with the same mail", userNotFound.getMessage());
	}

	@Test
	void createUser_When_No_User_Exist_Create_With_Default_Role() {
		when(userRepository.existsByEmail(anyString())).thenReturn(Boolean.FALSE);
		Role customerRole = new Role(ERole.ROLE_CUSTOMER);
		when(roleRepository.findByName(ERole.ROLE_CUSTOMER)).thenReturn(Optional.of(customerRole));
		userRequest.setRoles(null);
		boolean result = loginServiceImpl.createUser(userRequest);
		assertEquals(true, result);
	}

	@Test
	void createUser_When_No_User_Exist_Create_With_Admin_Role() {
		when(userRepository.existsByEmail(anyString())).thenReturn(Boolean.FALSE);
		Role adminRole = new Role(ERole.ROLE_ADMIN);
		when(roleRepository.findByName(ERole.ROLE_ADMIN)).thenReturn(Optional.of(adminRole));
		boolean result = loginServiceImpl.createUser(userRequest);
		assertEquals(true, result);
	}

	@Test
	void createUser_When_No_User_Exist_No_Role_Exist_Exception() {
		when(userRepository.existsByEmail(anyString())).thenReturn(Boolean.FALSE);
		when(roleRepository.findByName(ERole.ROLE_ADMIN)).thenReturn(Optional.ofNullable(null));

		UserLoginException roleNotFound = assertThrows(UserLoginException.class,
				() -> loginServiceImpl.createUser(userRequest));
		assertEquals("Role not found", roleNotFound.getMessage());

	}

	@Test
	void createUser_When_No_User_Exist_Create_With_Supplier_Role() {
		userRequest.setRoles(new HashSet<String>(Arrays.asList("supplier")));
		when(userRepository.existsByEmail(anyString())).thenReturn(Boolean.FALSE);
		Role adminRole = new Role(ERole.ROLE_SUPPLIER);
		when(roleRepository.findByName(ERole.ROLE_SUPPLIER)).thenReturn(Optional.of(adminRole));
		boolean result = loginServiceImpl.createUser(userRequest);
		assertEquals(true, result);
	}

	@Test
	void createUser_When_No_User_Exist_No_Supplier_Role_Exist_Exception() {
		when(userRepository.existsByEmail(anyString())).thenReturn(Boolean.FALSE);
		when(roleRepository.findByName(ERole.ROLE_ADMIN)).thenReturn(Optional.ofNullable(null));

		UserLoginException roleNotFound = assertThrows(UserLoginException.class,
				() -> loginServiceImpl.createUser(userRequest));
		assertEquals("Role not found", roleNotFound.getMessage());

	}

	@Test
	void createUser_When_No_User_Exist_No_Matching_Role_Create_With_Default_Role() {
		userRequest.setRoles(new HashSet<String>(Arrays.asList("someother")));
		when(userRepository.existsByEmail(anyString())).thenReturn(Boolean.FALSE);
		Role adminRole = new Role(ERole.ROLE_CUSTOMER);
		when(roleRepository.findByName(ERole.ROLE_CUSTOMER)).thenReturn(Optional.of(adminRole));
		boolean result = loginServiceImpl.createUser(userRequest);
		assertEquals(true, result);
	}

	@Test
	void createUser_When_No_User_Exist_No_Matching_Role_Exist_Exception() {
		when(userRepository.existsByEmail(anyString())).thenReturn(Boolean.FALSE);
		when(roleRepository.findByName(any(ERole.class))).thenReturn(Optional.ofNullable(null));

		UserLoginException roleNotFound = assertThrows(UserLoginException.class,
				() -> loginServiceImpl.createUser(userRequest));
		assertEquals("Role not found", roleNotFound.getMessage());

	}
}
