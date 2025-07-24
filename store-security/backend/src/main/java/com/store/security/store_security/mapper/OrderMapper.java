package com.store.security.store_security.mapper;

import com.store.security.store_security.dto.OrderDto;
import com.store.security.store_security.entity.OrderEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface OrderMapper {

	OrderEntity toEntity(OrderDto orderDto);

	OrderDto toDto(OrderEntity orderEntity);
}
