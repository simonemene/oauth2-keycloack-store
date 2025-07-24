package com.store.security.store_security.service;

import com.store.security.store_security.dto.AllUserDto;
import com.store.security.store_security.dto.UserDto;

public interface IUserService {

	public UserDto findUser(Long id);

	public AllUserDto allUser();

	public UserDto updateUser(Long id,UserDto userDto);

	public UserDto findUserByUsername(String username);
}
