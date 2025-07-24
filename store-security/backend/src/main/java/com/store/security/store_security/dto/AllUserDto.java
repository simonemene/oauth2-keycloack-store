package com.store.security.store_security.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class AllUserDto {

	public List<UserDto> users;

	public void addUser(UserDto user)
	{
		if(null == users)
		{
			users = new ArrayList<>();
			users.add(user);
		}else
		{
			users.add(user);
		}
	}
}
