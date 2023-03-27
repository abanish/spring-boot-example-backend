package com.uber.driverselfservice.interfaces;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uber.driverselfservice.domain.identityaccess.UserLoginEntity;
import com.uber.driverselfservice.infrastructure.UserLoginRepository;
import com.uber.driverselfservice.interfaces.dtos.identityaccess.UserResponseDto;


@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserLoginRepository userLoginRepository;

	@Operation(summary = "Get the user details for the currently logged-in user.")
	@PreAuthorize("isAuthenticated()")
	@GetMapping
	public ResponseEntity<UserResponseDto> getCurrentUser(Authentication authentication) {
		String username = authentication.getName();
		UserLoginEntity user = userLoginRepository.findByEmail(username);
		String email = user.getEmail();
		String driverId = user.getDriverId() != null ? user.getDriverId().getId() : null;
		UserResponseDto response = new UserResponseDto(email, driverId);
		return ResponseEntity.ok(response);
	}
}
