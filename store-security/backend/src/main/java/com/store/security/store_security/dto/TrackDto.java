package com.store.security.store_security.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TrackDto {

	private long id;

	private OrderDto order;

	private String status;
}
