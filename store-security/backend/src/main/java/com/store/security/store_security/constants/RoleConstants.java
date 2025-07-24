package com.store.security.store_security.constants;


import lombok.Getter;

@Getter
public enum RoleConstants {

    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN"),
    TRACK("ROLE_TRACK");

    private final String role;

    RoleConstants(String role) {
        this.role = role;
    }
}
