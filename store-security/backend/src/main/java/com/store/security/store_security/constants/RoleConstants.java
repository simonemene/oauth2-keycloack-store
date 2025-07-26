package com.store.security.store_security.constants;


import lombok.Getter;

@Getter
public enum RoleConstants {

    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN"),
    TRACK("ROLE_TRACK"),
    USER_DESCRIPTION("Rapresent an user"),
    ADMIN_DESCRIPTION("Rapresent an admin"),
    TRACK_DESCRIPTION("Rapresent an track");

    private final String role;

    RoleConstants(String role) {
        this.role = role;
    }
}
