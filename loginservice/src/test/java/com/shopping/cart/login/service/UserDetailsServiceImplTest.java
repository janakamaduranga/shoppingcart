package com.shopping.cart.login.service;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.shopping.cart.login.entity.Role;
import com.shopping.cart.login.entity.User;
import com.shopping.cart.login.entity.util.ERole;
import com.shopping.cart.login.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceImplTest {
	
	@Mock
	private UserRepository userRepository;
	
	@Mock
	private User user;
	
	UserDetailsServiceImpl userDetailsServiceImpl;
	
	@BeforeEach
	public void setUp() {
		userDetailsServiceImpl = new UserDetailsServiceImpl(userRepository);
	}

	@Test
	void loadUserByUsername_When_User_Found_Success() {
		when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
		Role role = new Role();
		role.setName(ERole.ROLE_ADMIN);
		Set<Role> roles = new HashSet<>();
		roles.add(role);
		final Long id = 200L;
		final String email = "jan@tst.com";
		final String pwd = "2323";
		when(user.getRoles()).thenReturn(roles);
		when(user.getId()).thenReturn(200L);
		when(user.getEmail()).thenReturn(email);
		when(user.getPassword()).thenReturn(pwd);
		
		UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsServiceImpl.loadUserByUsername("test");
		assertEquals(id, userDetails.getId());
		assertEquals(email, userDetails.getEmail());
		assertEquals(ERole.ROLE_ADMIN.name(), 
				((SimpleGrantedAuthority)userDetails.getAuthorities().toArray()[0]).getAuthority());
		
		
	}
	
	@Test
	void loadUserByUsername_When_User_Not_Found_Exception() {
		when(userRepository.findByEmail(anyString())).thenReturn(Optional.ofNullable(null));
		
		UsernameNotFoundException userNotFound = assertThrows(UsernameNotFoundException.class,
                () -> userDetailsServiceImpl.loadUserByUsername("test"));
        assertEquals("User Not Found with username: test", userNotFound.getMessage());
	}

}
