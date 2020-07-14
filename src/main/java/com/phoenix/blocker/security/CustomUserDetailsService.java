package com.phoenix.blocker.security;

import com.phoenix.blocker.dao.User;
import com.phoenix.blocker.service.UserFirebaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.ExecutionException;


@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	UserFirebaseService userFirebaseService;

	@Override
//	@Transactional
	public UserDetails loadUserByUsername(String username)  {
		// Let people login with either username or email
//		User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail).orElseThrow(
//				() -> new UsernameNotFoundException("User not found with username or email : " + usernameOrEmail));

		User user = null;

		try {
			user = userFirebaseService.getUser(username);
			return UserPrincipal.create(user);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

		return UserPrincipal.create(user);
	}

	// This method is used by JWTAuthenticationFilter
//	@Transactional
	public UserDetails loadUserById(String id) throws ExecutionException, InterruptedException {
//		User user = userRepository.findById(id)
//				.orElseThrow(() -> new UsernameNotFoundException("User not found with id : " + id));
		User user = userFirebaseService.getUser(id);

		return UserPrincipal.create(user);
	}
}
