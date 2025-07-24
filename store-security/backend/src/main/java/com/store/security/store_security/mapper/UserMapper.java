package com.store.security.store_security.mapper;

import com.store.security.store_security.dto.UserDto;
import com.store.security.store_security.entity.AuthoritiesEntity;
import com.store.security.store_security.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "authoritiesList", source = "authoritiesList", qualifiedByName = "mapAuthoritiesToStrings")
    UserDto toDto(UserEntity userEntity);

    @Mapping(target = "authoritiesList", ignore = true)
    UserEntity toEntity(UserDto userDto);

    @Named("mapAuthoritiesToStrings")
    static List<String> mapAuthoritiesToStrings(Set<AuthoritiesEntity> authorities) {
        if (authorities == null) return null;
        return authorities.stream()
                .map(AuthoritiesEntity::getAuthority)
                .toList();
    }

}

