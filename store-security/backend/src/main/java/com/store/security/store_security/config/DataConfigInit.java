package com.store.security.store_security.config;

import com.store.security.store_security.constants.RoleConstants;
import com.store.security.store_security.dto.RoleDto;
import com.store.security.store_security.dto.StockDto;
import com.store.security.store_security.dto.UserDto;
import com.store.security.store_security.entity.AuthoritiesEntity;
import com.store.security.store_security.entity.StockEntity;
import com.store.security.store_security.entity.UserEntity;
import com.store.security.store_security.repository.AuthoritiesRepository;
import com.store.security.store_security.repository.StockRepository;
import com.store.security.store_security.repository.UserRepository;
import com.store.security.store_security.service.IRegistrationService;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataConfigInit implements CommandLineRunner {

    private final IRegistrationService registrationService;

    @Override
    public void run(String... args) throws Exception {
        RoleDto roleDto = registrationService.registrationRole(RoleDto.builder()
                        .name(RoleConstants.ADMIN.getRole())
                        .description(RoleConstants.ADMIN_DESCRIPTION.getRole())
                .build());
        UserDto userDto = registrationService.registrationUserKeycloack(UserDto.builder()
                        .username("admin@example.it")
                        .password("admin")
                        .authoritiesList(List.of(roleDto.getName()))
                .build());
        RoleDto track = registrationService.registrationRole(RoleDto.builder()
                        .name(RoleConstants.TRACK.getRole())
                        .description(RoleConstants.TRACK_DESCRIPTION.getRole())
                .build());
        registrationService.registrationUserKeycloack(UserDto.builder()
                        .username("track@example.it")
                        .password("track")
                        .authoritiesList(List.of(track.getName()))
                .build());
    }
}

