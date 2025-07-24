package com.store.security.store_security.enums;

import lombok.Getter;

@Getter
public enum StatusTrackEnum {

	ORDER_PLACED("ORDER PLACED"),
	LOST("LOST"),
	DELIVERED("DELIVERED"),
	OUT_FOR_DELIVERY("OUT FOR DELIVERY");


	private final String track;

	StatusTrackEnum(String track) {
		this.track = track;
	}
}
