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

		User user = userFirebaseService.getUser(id);

		return UserPrincipal.create(user);
	}
}
