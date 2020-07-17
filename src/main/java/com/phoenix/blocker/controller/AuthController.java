package com.phoenix.blocker.controller;

import com.phoenix.blocker.dao.User;
import com.phoenix.blocker.security.JwtTokenProvider;
import com.phoenix.blocker.service.UserFirebaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import payloads.ApiResponse;
import payloads.JwtAuthenticationResponse;
import payloads.LoginRequest;
import payloads.SignUpRequest;

import javax.validation.Valid;
import java.net.URI;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
    AuthenticationManager authenticationManager;

	@Autowired
    PasswordEncoder passwordEncoder;

	@Autowired
	JwtTokenProvider tokenProvider;

	@Autowired
	UserFirebaseService userFirebaseService;

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsernameOrEmail(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		String jwt = tokenProvider.generateToken(authentication);
		return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) throws ExecutionException, InterruptedException {

		// Creating user's account
		User user = new User(signUpRequest.getName(), signUpRequest.getUsername(), signUpRequest.getEmail(),
				signUpRequest.getPassword());

		user.setPassword(passwordEncoder.encode(user.getPassword()));

		user.setRole("ROLE_USER");

		User result;

		result = userFirebaseService.saveUserWithCollectionId(user.getUserName(),user);

		URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/users/{username}")
				.buildAndExpand(result.getUserName()).toUri();

		return ResponseEntity.created(location).body(new ApiResponse(true, "User registered successfully"));
	}
}
