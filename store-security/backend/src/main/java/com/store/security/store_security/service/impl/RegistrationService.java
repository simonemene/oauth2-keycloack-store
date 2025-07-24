package com.store.security.store_security.service.impl;

import com.store.security.store_security.annotation.LogExecutionTime;
import com.store.security.store_security.constants.RoleConstants;
import com.store.security.store_security.dto.UserDto;
import com.store.security.store_security.entity.AuthoritiesEntity;
import com.store.security.store_security.entity.UserEntity;
import com.store.security.store_security.exceptions.UserException;
import com.store.security.store_security.mapper.UserMapper;
import com.store.security.store_security.repository.AuthoritiesRepository;
import com.store.security.store_security.repository.UserRepository;
import com.store.security.store_security.service.IRegistrationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class RegistrationService implements IRegistrationService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    private final AuthoritiesRepository authoritiesRepository;

    @LogExecutionTime
    @Override
    public UserDto registrationUser(UserDto userDto) {
        log.info("registration {}", userDto.getUsername());

        UserEntity userRegister = null;
            if (userDto.getUsername() != null && userDto.getPassword() != null && !userDto.getUsername().isEmpty()) {
                Optional<UserEntity> userCheck = userRepository.findByUsername(userDto.getUsername());
                if (userCheck.isPresent() && userCheck.get().getId()>0) {
                    throw new UserException("User already exist");
                }
                userDto.setTmstInsert(LocalDateTime.now());
                Optional<AuthoritiesEntity> authorities = authoritiesRepository.findByAuthority(RoleConstants.USER.getRole());
                if(authorities.isPresent()) {
                    userDto.setAuthoritiesList(List.of(authorities.get().getAuthority()));
                }else {
                    throw new UserException("Authorization USER not found");
                }

                UserEntity userEntity = userMapper.toEntity(userDto);
                userEntity.setPassword(passwordEncoder.encode(userDto.getPassword()));
                AuthoritiesEntity authoritiesEntity = authoritiesRepository.findByAuthority(RoleConstants.USER.getRole()).orElseThrow(() -> new UserException("Authorization USER not found"));
                userEntity.setAuthoritiesList(Set.of(authoritiesEntity));
                userRegister = userRepository.save(userEntity);
            }
            else
            {
                throw new UserException("Registration failed");
            }

        return userMapper.toDto(userRegister);
    }
}
