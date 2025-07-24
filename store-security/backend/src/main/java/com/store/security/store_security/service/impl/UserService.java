package com.store.security.store_security.service.impl;

import com.store.security.store_security.dto.AllUserDto;
import com.store.security.store_security.dto.UserDto;
import com.store.security.store_security.entity.UserEntity;
import com.store.security.store_security.exceptions.UserException;
import com.store.security.store_security.mapper.UserMapper;
import com.store.security.store_security.repository.UserRepository;
import com.store.security.store_security.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService implements IUserService {

	private final UserRepository userRepository;

	private final UserMapper userMapper;

	@Transactional(readOnly = true)
	@Override
	public UserDto findUser(Long id) {
		Optional<UserEntity> userEntity = userRepository.findById(id);
		if(userEntity.isPresent())
		{
			return userMapper.toDto(userEntity.get());
		}
		throw new UserException(String.format("User %s not found", id));
	}

	@Transactional(readOnly = true)
	@Override
	public UserDto findUserByUsername(String username) {
		Optional<UserEntity> userEntity = userRepository.findByUsername(username);
		if(userEntity.isPresent())
		{
			return userMapper.toDto(userEntity.get());
		}
		throw new UserException(String.format("User %s not found", username));
	}



	@Transactional(readOnly = true)
	@Override
	public AllUserDto allUser() {
		List<UserEntity> users = userRepository.findAll();
		AllUserDto allUser = AllUserDto.builder().build();
		if (!users.isEmpty()) {
			for(UserEntity user : users)
			{
				allUser.addUser(userMapper.toDto(user));
			}
		}else
		{
			throw new UserException("No user found");
		}
		return allUser;
	}

	@Transactional
	@Override
	public UserDto updateUser(Long id,UserDto userDto) {
		UserEntity userEntity = userMapper.toEntity(userDto);
		Optional<UserEntity> userCheck = userRepository.findById(id);
		UserEntity update = UserEntity.builder().build();
		if(userCheck.isPresent() && userCheck.get().getId()>0)
		{
			update = userCheck.get();
			update.setUsername(userDto.getUsername());
			update.setAge(userDto.getAge());
			update = userRepository.save(update);
		}else
		{
			throw new UserException(String.format("User %s not updated",userDto.getUsername()));
		}

		if(update.getId() <= 0)
		{
			throw new UserException(String.format("User %s not updated",userDto.getUsername()));
		}
		return userMapper.toDto(update);
	}

}
