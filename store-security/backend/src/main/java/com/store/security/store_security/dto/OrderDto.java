package com.store.security.store_security.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class OrderDto {

	private Long id;

	private UserDto user;

	private LocalDateTime tmstInsert;
}
