package com.store.security.store_security.service.impl;

import com.store.security.store_security.annotation.LogExecutionTime;
import com.store.security.store_security.constants.RoleConstants;
import com.store.security.store_security.dto.RoleDto;
import com.store.security.store_security.dto.UserDto;
import com.store.security.store_security.entity.AuthoritiesEntity;
import com.store.security.store_security.entity.UserEntity;
import com.store.security.store_security.exceptions.UserException;
import com.store.security.store_security.mapper.UserMapper;
import com.store.security.store_security.mapper.keycloack.UserKeycloackMapper;
import com.store.security.store_security.properties.KeycloackProperties;
import com.store.security.store_security.repository.AuthoritiesRepository;
import com.store.security.store_security.repository.UserRepository;
import com.store.security.store_security.service.IRegistrationService;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RoleResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
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

    private final Keycloak keycloak;

    private final KeycloackProperties keycloackProperties;

    private final UserKeycloackMapper userKeycloackMapper;

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

    @LogExecutionTime
    @Override
    public UserDto registrationUserKeycloack(UserDto userDto) {
        UserEntity userRegister = null;
        if (userDto.getUsername() != null && userDto.getPassword() != null && !userDto.getUsername()
                .isEmpty()) {
            userDto.setTmstInsert(LocalDateTime.now());
            RoleResource role = keycloak.realm(keycloackProperties.realm()).roles()
                    .get(RoleConstants.USER.getRole());
            if (null != role) {
                UserRepresentation user = userKeycloackMapper.userKeycloackMapper(userDto);
                try(Response response = keycloak.realm(keycloackProperties.realm()).users().create(user)){
                    switch(response.getStatus())
                    {
                        case 201:
                            log.info("User {} saved", userDto.getUsername());
                            String id = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");
                            UserResource userResource = keycloak.realm(keycloackProperties.realm()).users().get(id);
                            List<RoleRepresentation> rolesToAssign = new ArrayList<>();
                            for (String roleName : userDto.getAuthoritiesList()) {
                                RoleRepresentation roleUser = keycloak.realm(keycloackProperties.realm())
                                        .roles()
                                        .get(roleName)
                                        .toRepresentation();
                                rolesToAssign.add(roleUser);
                            }
                            userResource.roles().realmLevel().add(rolesToAssign);

                            break;
                        case 409:
                            throw new UserException(String.format("User %s already exist", userDto.getUsername()));
                    default:
                        throw new UserException(String.format("User %s not saved", userDto.getUsername()));

                    }
                }catch(Exception e)
                {
                    throw new UserException(String.format("User %s not saved, message: %s", userDto.getUsername(),e.getMessage()));
                }
            }
        }
        return userDto;
    }

    @Override
    public RoleDto registrationRole(RoleDto roleDto) {
        RoleRepresentation role = new RoleRepresentation();
        role.setName(roleDto.getName());
        role.setDescription(roleDto.getDescription());
        keycloak.realm(keycloackProperties.realm()).roles().create(role);
        RoleRepresentation roleCreated = keycloak.realm(keycloackProperties.realm()).roles().get(roleDto.getName()).toRepresentation();
        return RoleDto.builder().name(roleCreated.getName()).description(roleCreated.getDescription()).build();
    }

}
