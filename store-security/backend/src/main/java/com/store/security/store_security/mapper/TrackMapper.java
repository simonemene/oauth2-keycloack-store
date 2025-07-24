package com.store.security.store_security.mapper;

import com.store.security.store_security.dto.TrackDto;
import com.store.security.store_security.entity.TrackEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {OrderMapper.class})
public interface TrackMapper {

	TrackDto toDto(TrackEntity trackEntity);

	TrackEntity toEntity(TrackDto trackDto);
}
