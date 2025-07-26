package com.store.security.store_security.service;

import com.store.security.store_security.dto.RoleDto;
import com.store.security.store_security.dto.UserDto;

import java.util.Map;

public interface IRegistrationService {

    public UserDto registrationUser(UserDto userDto);
    public UserDto registrationUserKeycloack(UserDto userDto);
    public RoleDto registrationRole(RoleDto roleDto);
}
