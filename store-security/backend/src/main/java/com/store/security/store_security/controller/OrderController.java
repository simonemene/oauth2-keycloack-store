package com.store.security.store_security.controller;

import com.store.security.store_security.dto.AllOrderDto;
import com.store.security.store_security.dto.ArticlesOrderDto;
import com.store.security.store_security.exceptions.OrderException;
import com.store.security.store_security.service.IOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(
		name = "Order management",
		description = "REST API to manage orders"
)
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/orders")
public class OrderController {

	private final IOrderService orderService;


	@Operation(
			summary = "Create an order",
			description = "REST API to create an order"
	)
	@ApiResponse(
			responseCode = "201",
			description  = "HTTP Status 201 : CREATED"
	)
	@PreAuthorize("(#articlesOrderDto.username == authentication.name && hasRole('ROLE_USER')) || hasRole('ROLE_ADMIN')")
	@PostMapping
	public ResponseEntity<ArticlesOrderDto> createOrder(@Valid @RequestBody ArticlesOrderDto articlesOrderDto)
			throws OrderException {
		return ResponseEntity.status(HttpStatus.CREATED).body(orderService.orderArticles(articlesOrderDto));
	}


	@Operation(
			summary = "Get all orders by user",
			description = "REST API to get all orders by user"
	)
	@ApiResponse(
			responseCode = "200",
			description  = "HTTP Status 200 : OK"
	)
	@PreAuthorize("hasRole('ROLE_USER') || hasRole('ROLE_ADMIN') || hasRole('ROLE_TRACK')")
	@GetMapping("/{id}")
	public ResponseEntity<AllOrderDto> getAllOrders(@PathVariable("id") Long id)
			throws OrderException {
		return ResponseEntity.status(HttpStatus.OK).body(orderService.allOrderByUser(id));
	}




}