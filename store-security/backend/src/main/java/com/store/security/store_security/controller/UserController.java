package com.store.security.store_security.controller;

import com.store.security.store_security.dto.AllUserDto;
import com.store.security.store_security.dto.UserDto;
import com.store.security.store_security.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(
		name = "User management",
		description = "REST API for user management"
)
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController {

	private final IUserService userService;


	@Operation(
			summary = "Get user details",
			description = "REST API to get user details"
	)
	@ApiResponse(
			responseCode = "200",
			description  = "HTTP Status 200 : OK"
	)
	@PreAuthorize("hasRole('ROLE_USER') || hasRole('ROLE_ADMIN')")
	@GetMapping("/{id}")
	public ResponseEntity<UserDto> userDetails(@PathVariable("id") Long username)
	{
		return ResponseEntity.status(HttpStatus.OK).body(userService.findUser(username));
	}

	@Operation(
			summary = "Get all users",
			description = "REST API to get all users"
	)
	@ApiResponse(
			responseCode = "200",
			description  = "HTTP Status 200 : OK"
	)
	@GetMapping
	public ResponseEntity<AllUserDto> allUser()
	{
		return ResponseEntity.status(HttpStatus.OK).body(userService.allUser());
	}

	@Operation(
			summary = "Update user",
			description = "REST API to update user"
	)
	@ApiResponse(
			responseCode = "200",
			description  = "HTTP Status 200 : OK"
	)
	@PutMapping("/{id}")
	public ResponseEntity<UserDto> updateUser(@PathVariable("id") Long id,@RequestBody UserDto userDto)
	{
		return ResponseEntity.status(HttpStatus.OK).body(userService.updateUser(id,userDto));
	}



}
