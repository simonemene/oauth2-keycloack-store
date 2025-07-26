package com.store.security.store_security.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class RoleDto {

    private String name;

    private String description;
}
